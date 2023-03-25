package dev.conca.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.IdentityHashMap;
import java.util.Map;

public class CyclicObjectSerializer implements JsonSerializer<CyclicObject> {

    private CyclicObject firstObject;
    private JsonObject jsonRoot;
    private Map<Object, JsonObject> seen = new IdentityHashMap<>();

    @Override
    public JsonElement serialize(CyclicObject src, Type type, JsonSerializationContext context) {

        if (firstObject == null) {
            firstObject = src;
            jsonRoot = new JsonObject();
        }
        JsonElement jsonElement = seen.get(src);
        if (jsonElement != null) {
            return jsonElement;
        }
        seen.put(src, createObjectRef(src, type));

        String objectId = createObjectId(src, type);

        JsonObject object = new JsonObject();
        object.addProperty("field1", src.getField1());
        object.add("cyclicObject", createObjectRef(src.getCyclicObject(), type));

        context.serialize(src.getCyclicObject());

        jsonRoot.add(objectId, object);

        if (src == firstObject)
            return jsonRoot;
        else
            return object;
    }

    private static String createObjectId(CyclicObject src, Type type) {
        return type.getTypeName() + "#" + System.identityHashCode(src);
    }

    private JsonObject createObjectRef(CyclicObject src, Type type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("$ref", "/" + createObjectId(src, type));
        return jsonObject;
    }
}
