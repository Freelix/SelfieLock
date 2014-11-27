package com.selfielock.serverCommunication;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.selfielock.database.StatsEntity;

public class StatsAdapter {

    public static class UpdateStatsAdapter implements JsonSerializer<StatsEntity> {
        public JsonElement serialize(StatsEntity stats, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "updateStats");
          jsonObject.addProperty("email", stats.getUserEmail());
          jsonObject.addProperty("numbersOfWin", stats.getNumberOfWin());
          jsonObject.addProperty("numbersOfFail", stats.getNumberOfFail());
          jsonObject.addProperty("timesPlayed", stats.getTimesPlayed());
          jsonObject.addProperty("achivementsUnlocked", stats.getAchievementsUnlocked());
          
          return jsonObject;      
        }
    }
    
    public static class SelectStatsAdapter implements JsonSerializer<String> {
        public JsonElement serialize(String email, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "getStats");
          jsonObject.addProperty("email", email);

          return jsonObject;      
        }
    }

}
