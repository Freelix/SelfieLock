package com.selfielock.serverCommunication;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.selfielock.location.GeoLocation;

public class LocationAdapter {

    public static class UpdateLocationAdapter implements JsonSerializer<GeoLocation> {
        public JsonElement serialize(GeoLocation location, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "updateLocations");
          jsonObject.addProperty("email", location.getEmail());
          jsonObject.addProperty("lat", location.getLatitude());
          jsonObject.addProperty("lng", location.getLongitude());
          
          return jsonObject;      
        }
    }
}
