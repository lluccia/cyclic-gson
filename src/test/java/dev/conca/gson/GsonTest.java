package dev.conca.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GsonTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    void canSerializeEmptyObject() {
        CyclicObject rootObject = new CyclicObject();
        assertThat(gson.toJson(rootObject)).isEqualTo("{}");
    }

    @Test
    void canSerializeSimpleFields() {
        CyclicObject rootObject = new CyclicObject();
        rootObject.setField1("value1");
        assertThat(gson.toJson(rootObject)).isEqualTo(
                "{\"field1\":\"value1\"}");
    }

    @Test
    void canGetUniqueInstanceIdentifiers() {
        CyclicObject object1 = new CyclicObject();
        object1.setField1("value1");

        CyclicObject object2 = object1;

        CyclicObject object3 = new CyclicObject();
        object3.setField1("value1");

        assertThat(System.identityHashCode(object1))
                .isEqualTo(System.identityHashCode(object2))
                .isNotEqualTo(System.identityHashCode(object3));
    }

    @Test
    void shouldSerializeIdentifiersForReferencedObjects() {
        JsonSerializer<CyclicObject> serializer = new CyclicObjectSerializer();

        Gson customGson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(CyclicObject.class, serializer)
                .create();

        CyclicObject object1 = new CyclicObject();
        object1.setField1("value1");

        CyclicObject object2 = new CyclicObject();
        object2.setField1("value1");

        object1.setCyclicObject(object2);
        object2.setCyclicObject(object1);

        Approvals.verify(customGson.toJson(object1));
    }

}