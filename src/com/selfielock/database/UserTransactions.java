package com.selfielock.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.selfielock.database.UserContract.UserScheme;

public class UserTransactions extends DatabaseInstance {
    
    private DatabaseHelper slDbHelper;
    private Context context;

    public UserTransactions(Context context) 
    {
        super(context);
        this.context = context;
        this.slDbHelper = super.getSLDbHelper();
    }
    
    /*****************************/
    /*** How to use this class ***/
    /*****************************/
    
    /*getActivity().deleteDatabase("SelfieLock.db");
    
    UserTransactions ut = new UserTransactions(getActivity());
    
    ut.AddUser(new UserEntity("Timmy", "Turner", "timmy.turner@kekchose.com"));
    ut.AddUser(new UserEntity("Georges", "Turner", "Georges.turner@kekchose.com"));
    ut.AddUser(new UserEntity("Michael", "Turner", "Michael.turner@kekchose.com"));
    UserEntity un = ut.getUserByEmail("Georges.turner@kekchose.com");
    
    List<UserEntity> users = ut.getAllUsers();*/
    
    /*****************/
    /*** Functions ***/
    /*****************/
    
    public boolean AddUser(UserEntity user)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UserScheme.COLUMN_NAME_FIRSTNAME, user.getFirstName());
        values.put(UserScheme.COLUMN_NAME_LASTNAME, user.getLastName());
        values.put(UserScheme.COLUMN_NAME_GENDER, user.getGender());
        values.put(UserScheme.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(UserScheme.COLUMN_NAME_IMAGE, user.getImage());
        values.put(UserScheme.COLUMN_NAME_PASSWORD, user.getPassword());

        // Insert the new row, returning the primary key value of the new row
        createSuccessful = db.insert(UserScheme.TABLE_NAME, null, values) > 0;
        
        // Create the stats for that user
        if (createSuccessful)
        {
            StatsTransactions st = new StatsTransactions(context);
            createSuccessful = st.AddStatsToUser(user);
        }
        
        // Create the achievement for that user
        /*if (createSuccessful)
        {
            AchievementCollection ac = new AchievementCollection(user.getEmail());
            AchievementTransactions at = new AchievementTransactions(context);
            createSuccessful = at.AddAchievementToUser(user, ac);
        }*/
        
        db.close();
        
        return createSuccessful;
    }
    
    public boolean deleteUser(UserEntity user)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getReadableDatabase();
        
        createSuccessful = db.delete(UserScheme.TABLE_NAME, UserScheme.COLUMN_NAME_EMAIL + "='" + user.getEmail() + "'", null) > 0;
        
        db.close();
        
        return createSuccessful;
    }
    
    public boolean updateUser(UserEntity user)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put(UserScheme.COLUMN_NAME_FIRSTNAME, user.getFirstName());
        cv.put(UserScheme.COLUMN_NAME_LASTNAME, user.getLastName());
        cv.put(UserScheme.COLUMN_NAME_GENDER, user.getGender());
        cv.put(UserScheme.COLUMN_NAME_EMAIL, user.getEmail());
        cv.put(UserScheme.COLUMN_NAME_IMAGE, user.getImage());
        cv.put(UserScheme.COLUMN_NAME_PASSWORD, user.getPassword());
        
        db.update(UserScheme.TABLE_NAME, cv, UserScheme.COLUMN_NAME_EMAIL + "='" + user.getEmail() + "'", null);
        
        db.close();
        
        return createSuccessful;
    }

    public UserEntity getUserByEmail(String email)
    {
        UserEntity user = null;
        SQLiteDatabase db = slDbHelper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s = '%s' ORDER BY %s ASC",
                UserScheme.TABLE_NAME,
                UserScheme.COLUMN_NAME_EMAIL,
                email,
                UserScheme.COLUMN_NAME_FIRSTNAME);
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst())
        {
            user = new UserEntity(
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_LASTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_GENDER)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_EMAIL)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_PASSWORD))
            );
        }
        
        cursor.close();
        db.close();
        
        return user;
    }
    
    public List<UserEntity> getAllUsers()
    {
        List<UserEntity> listUsers = new ArrayList<UserEntity>();
        SQLiteDatabase db = slDbHelper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s ORDER BY %s ASC",
                UserScheme.TABLE_NAME, 
                UserScheme.COLUMN_NAME_FIRSTNAME);
        
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        
        while (cursor.isAfterLast() == false) 
        {

            listUsers.add(new UserEntity(
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_FIRSTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_LASTNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_GENDER)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_EMAIL)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(UserScheme.COLUMN_NAME_PASSWORD))
            ));
            
            cursor.moveToNext();
        }
  
        cursor.close();
        db.close();
        
        return listUsers;
    }
}
