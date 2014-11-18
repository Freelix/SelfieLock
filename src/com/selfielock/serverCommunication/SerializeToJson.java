package com.selfielock.serverCommunication;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.selfielock.database.UserEntity;
import com.selfielock.utils.Constants;

public class SerializeToJson {

    private Object data;
    
    public SerializeToJson(Object data)
    {
        this.data = data;
    }
    
    public void toJson() 
    {  
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();
        
        if (data instanceof UserEntity)
        {
            gson = gsonBuilder.registerTypeAdapter(UserEntity.class, new SerializeToJson.UserEntityAdapter()).create();
        }
        
        sendToServer(gson.toJson(data));
    }
    
    private void sendToServer(String jsonString)
    {
        new PostRequest(Constants.SERVER_URL + Constants.POST_SIGNUP, jsonString).execute();
    }
    
    private static class UserEntityAdapter implements JsonSerializer<UserEntity> {
        public JsonElement serialize(UserEntity user, Type type, JsonSerializationContext jsc) {
          JsonObject jsonObject = new JsonObject();
          /*jsonObject.addProperty("user_firstName", user.getFirstName());
          jsonObject.addProperty("user_lastName", user.getLastName());
          jsonObject.addProperty("user_gender", user.getGender());
          jsonObject.addProperty("user_email", user.getEmail());
          jsonObject.addProperty("user_image", new String(user.getImage()));
          jsonObject.addProperty("user_password", user.getPassword());*/
          jsonObject.addProperty("query", "signup");
          jsonObject.addProperty("name", user.getLastName());
          jsonObject.addProperty("surname", user.getLastName());
          jsonObject.addProperty("image", Base64.encodeToString(user.getImage(), Base64.NO_WRAP));
          jsonObject.addProperty("password", user.getPassword());
          jsonObject.addProperty("email", user.getEmail());
          
          return jsonObject;      
        }
    }

}
