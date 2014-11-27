package com.selfielock.serverCommunication;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AchievementsAdapter {

    public static class GetAchievementsAdapter implements JsonSerializer<String> {
        public JsonElement serialize(String email, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "getAchivements");
          jsonObject.addProperty("email", email);
          
          return jsonObject;      
        }
    }

}
