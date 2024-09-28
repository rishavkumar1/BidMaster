package utils;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class JsonUtil {
    public static String toJson(Object object) {
        return Json.toJson(object).toString();
    }

    public static <A> A fromJson(String json, Class<A> clazz) {
        try {
            JsonNode jsonNode = Json.parse(json);
            return Json.fromJson(jsonNode, clazz);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
