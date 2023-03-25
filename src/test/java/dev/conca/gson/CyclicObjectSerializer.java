package dev.conca.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class CyclicObjectSerializer implements JsonSerializer<CyclicObject> {

    private CyclicObject rootObject;
    private JsonArray jsonObjects;

    private Set<Integer> alreadySerializedIds = new HashSet<>();

    @Override
    public JsonElement serialize(CyclicObject cyclicObject, Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        if (rootObject == null) {
            rootObject = cyclicObject;
            jsonObjects = new JsonArray();
        }

        int objectId = System.identityHashCode(cyclicObject);

        if (alreadySerializedIds.contains(objectId)) {
            return null;
        }

        JsonObject object = new JsonObject();
        object.addProperty("_id", objectId);
        object.addProperty("field1", cyclicObject.getField1());
        object.addProperty("cyclicObject_id", System.identityHashCode(cyclicObject.getCyclicObject()));

        alreadySerializedIds.add(objectId);
        jsonObjects.add(object);

        jsonSerializationContext.serialize(cyclicObject.getCyclicObject());

        if (rootObject == cyclicObject)
            return jsonObjects;
        else
            return object;
    }
}
