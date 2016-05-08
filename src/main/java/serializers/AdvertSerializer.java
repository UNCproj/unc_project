package serializers;

import beans.AdvertBean;
import com.google.gson.*;

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
            String attrValue = attr.getValue();

            if (attrsPseudonims != null && attrsPseudonims.containsKey(attrKey)) {
                attrKey = attrsPseudonims.get(attrKey);
            }

            if (attrKey.equals("object_id")) {
                attrKey = "id";
            }

            if (attrKey.equals("object_name")) {
                attrKey = "name";
            }

            if (attrKey.equals("map_coordinates") && attrValue != null) {
                JsonElement attrValueElem = new JsonParser().parse(attrValue);
                jsonObject.add(attrKey, attrValueElem);
            }
            else {
                jsonObject.addProperty(attrKey, attrValue);
            }
        }

        return jsonObject;
    }
}
