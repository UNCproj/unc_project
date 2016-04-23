package serializers;

import beans.AdvertBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Денис on 23.04.2016.
 */
public class AdvertSerializer implements JsonSerializer<AdvertBean> {
    private HashMap<String, String> attrsPseudonims;

    public AdvertSerializer() {
        attrsPseudonims = new HashMap<>();
    }

    public AdvertSerializer(HashMap<String, String> attrsPseudonims) {
        this.attrsPseudonims = attrsPseudonims;
    }

    @Override
    public JsonElement serialize(AdvertBean src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, String> attr: src.getAttributesMap().entrySet()) {
            String attrKey = attr.getKey();

            if (attrsPseudonims != null && attrsPseudonims.containsKey(attrKey)) {
                attrKey = attrsPseudonims.get(attrKey);
            }

            if (attrKey.equals("object_id")) {
                attrKey = "id";
            }

            if (attrKey.equals("object_name")) {
                attrKey = "name";
            }

            jsonObject.addProperty(attrKey, attr.getValue());
        }

        return jsonObject;
    }
}
