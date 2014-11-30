package com.selfielock.serverCommunication;

import java.lang.reflect.Type;
import android.util.Base64;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.selfielock.database.UserEntity;

public class UserAdapter {

    public static class CreateUserAdapter implements JsonSerializer<UserEntity> {
        public JsonElement serialize(UserEntity user, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "createSignup");
          jsonObject.addProperty("name", user.getLastName());
          jsonObject.addProperty("surname", user.getFirstName());
          jsonObject.addProperty("image", Base64.encodeToString(user.getImage(), Base64.NO_WRAP));
          jsonObject.addProperty("password", user.getPassword());
          jsonObject.addProperty("email", user.getEmail());
          jsonObject.addProperty("gender", user.getGender());
          
          return jsonObject;      
        }
    }
    
    public static class UpdateUserAdapter implements JsonSerializer<UserEntity> {
        public JsonElement serialize(UserEntity user, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "updateProfile");
          jsonObject.addProperty("name", user.getLastName());
          jsonObject.addProperty("surname", user.getFirstName());
          jsonObject.addProperty("image", Base64.encodeToString(user.getImage(), Base64.NO_WRAP));
          jsonObject.addProperty("password", user.getPassword());
          jsonObject.addProperty("email", user.getEmail());
          jsonObject.addProperty("gender", user.getGender());
          
          return jsonObject;      
        }
    }
    
    public static class DeleteUserAdapter implements JsonSerializer<UserEntity> {
        public JsonElement serialize(UserEntity user, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "deleteSignup");
          jsonObject.addProperty("email", user.getEmail());
          
          return jsonObject;      
        }
    }
    
    public static class SelectUserAdapter implements JsonSerializer<String> {
        public JsonElement serialize(String email, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "selectSignup");
          jsonObject.addProperty("email", email);
          
          return jsonObject;      
        }
    }
    
    public static class CheckIfUserExistAdapter implements JsonSerializer<String> {
        public JsonElement serialize(String email, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();

          jsonObject.addProperty("query", "signupExist");
          jsonObject.addProperty("email", email);
          
          return jsonObject;      
        }
    }
}
