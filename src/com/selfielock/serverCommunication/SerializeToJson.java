package com.selfielock.serverCommunication;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selfielock.achievement.Achievement;
import com.selfielock.database.StatsEntity;
import com.selfielock.database.UserEntity;
import com.selfielock.location.LocationObject;
import com.selfielock.utils.Constants;

public class SerializeToJson {

    private Object data;
    private String request;
    private String jsonStr;
    private PostRequest pr;
    private String objectType;
    
    private enum ObjectType {
        
        UserEntity("1"),
        StatsEntity("2"),
        Achievements("3");
        
        private String type;
        
        public String getType()
        {
            return type;
        }
        
        ObjectType(String type)
        {
            this.type = type;
        }
    }
    
    public SerializeToJson(Object data, String action)
    {
        this.data = data;
        this.request = action;
        this.jsonStr = "";
        this.pr = null;
        this.objectType = null;
    }
    
    public String getJsonStr()
    {
        return jsonStr;
    }
    
    public void toJson() 
    {  
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();

        if (request.equals(RequestConstants.CREATE_USER))
            gson = gsonBuilder.registerTypeAdapter(UserEntity.class, new UserAdapter.CreateUserAdapter()).create();
        else if (request.equals(RequestConstants.UPDATE_USER))
            gson = gsonBuilder.registerTypeAdapter(UserEntity.class, new UserAdapter.UpdateUserAdapter()).create();
        else if (request.equals(RequestConstants.DELETE_USER))
            gson = gsonBuilder.registerTypeAdapter(UserEntity.class, new UserAdapter.DeleteUserAdapter()).create();
        else if (request.equals(RequestConstants.SELECT_USER)) {
            objectType = ObjectType.UserEntity.getType();
            gson = gsonBuilder.registerTypeAdapter(String.class, new UserAdapter.SelectUserAdapter()).create();
        }
        else if (request.equals(RequestConstants.UPDATE_STATS))
            gson = gsonBuilder.registerTypeAdapter(StatsEntity.class, new StatsAdapter.UpdateStatsAdapter()).create();
        else if (request.equals(RequestConstants.SELECT_STATS)) {
            objectType = ObjectType.StatsEntity.getType();
            gson = gsonBuilder.registerTypeAdapter(String.class, new StatsAdapter.SelectStatsAdapter()).create();
        }
        else if (request.equals(RequestConstants.EXIST_USER)) 
            gson = gsonBuilder.registerTypeAdapter(String.class, new UserAdapter.CheckIfUserExistAdapter()).create();
        else if (request.equals(RequestConstants.SELECT_ACHIEVEMENTS)) {
            objectType = ObjectType.Achievements.getType();
            gson = gsonBuilder.registerTypeAdapter(String.class, new AchievementsAdapter.GetAchievementsAdapter()).create();
        }
        else if (request.equals(RequestConstants.UPDATE_LOCATION))
            gson = gsonBuilder.registerTypeAdapter(LocationObject.class, new LocationAdapter.UpdateLocationAdapter()).create();
        
        sendToServer(gson.toJson(data));
    }
    
    public Object toObject()
    {       
        if (ObjectType.UserEntity.getType().equals(objectType)) {
            try {
                JSONObject json = new JSONObject(jsonStr);
                String lastName = json.getString("name");
                String firstName = json.getString("surname");
                String gender = json.getString("gender");
                String password = json.getString("password");
                String email = json.getString("email");
                String image = json.getString("image");
                byte[] imageByte = null;
                
                try {
                    imageByte = image.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                
                return new UserEntity(firstName, lastName, gender, email, imageByte, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return null;
        }
        else if (ObjectType.StatsEntity.getType().equals(objectType)) {
            jsonStr = jsonStr.replace("signup_id", "userEmail");
            jsonStr = jsonStr.replace("numbersOfWin", "numberOfWin");
            jsonStr = jsonStr.replace("numbersOfFail", "numberOfFail");
            jsonStr = jsonStr.replace("achivementsUnlocked", "achievementsUnlocked");
            return new Gson().fromJson(jsonStr, StatsEntity.class);
        }
        else if (ObjectType.Achievements.getType().equals(objectType)) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                
                int length = jsonArray.length();
                String description = null;
                String unlockedStr = null;
                boolean isUnlocked = false;
                List<Achievement> list = new ArrayList<Achievement>();
                
                for (int i = 0; i < length; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject fields = obj.getJSONObject("fields");
                        
                    description = fields.getString("description");
                    unlockedStr = fields.getString("unlocked");
                    
                    if (unlockedStr.equals("True"))
                        isUnlocked = true;
                    else
                        isUnlocked = false;
                    
                    list.add(new Achievement(description, isUnlocked));
                }
                
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }         
        }
        
        return null;
    }
    
    private void sendToServer(String jsonString)
    {
        pr = new PostRequest(Constants.SERVER_URL + Constants.POST_SIGNUP, jsonString);

        try {
            jsonStr = pr.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
