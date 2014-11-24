package com.selfielock.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.selfielock.achievement.Achievement;
import com.selfielock.database.AchievementContract.AchievementScheme;
import com.selfielock.database.StatsContract.StatsScheme;
import com.selfielock.database.UserContract.UserScheme;
import com.selfielock.utils.SLUtils;

public class AchievementTransactions extends DatabaseInstance {
    
    private DatabaseHelper slDbHelper;
    private Context context;

    public AchievementTransactions(Context context) 
    {
        super(context);
        this.context = context;
        this.slDbHelper = super.getSLDbHelper();
    }
    
    /*****************/
    /*** Functions ***/
    /*****************/
    
    public boolean AddAchievementToUser(UserEntity user, AchievementCollection ac)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();

        String strAc = null;
        
        try {
            strAc = SLUtils.toString(ac);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AchievementScheme.COLUMN_NAME_LIST_ACHIEVEMENT, strAc);
        values.put(AchievementScheme.COLUMN_NAME_USER_EMAIL, user.getEmail());

        // Insert the new row, returning the primary key value of the new row
        createSuccessful = db.insert(AchievementScheme.TABLE_NAME, null, values) > 0;   
        db.close();
        
        return createSuccessful;
    }
    
    public AchievementCollection getAllAchievementsByUserEmail(String userEmail)
    {
        AchievementCollection listAchievements = new AchievementCollection();
        SQLiteDatabase db = slDbHelper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                AchievementScheme.TABLE_NAME,
                AchievementScheme.COLUMN_NAME_USER_EMAIL,
                userEmail
                );
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst())
        {         
            listAchievements.addFromString(
                cursor.getString(cursor.getColumnIndexOrThrow(AchievementScheme.COLUMN_NAME_LIST_ACHIEVEMENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(AchievementScheme.COLUMN_NAME_USER_EMAIL))
            );    
        }
        
        cursor.close();
        db.close();
        
        return listAchievements;
    }
    
    public boolean updateUserAchievement(AchievementCollection ac)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();
        
        String strAc = null;
 
        try {
            strAc = SLUtils.toString(ac);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ContentValues cv = new ContentValues();
        cv.put(AchievementScheme.COLUMN_NAME_LIST_ACHIEVEMENT, strAc);
        
        db.update(AchievementScheme.TABLE_NAME, cv, AchievementScheme.COLUMN_NAME_USER_EMAIL + "='" + ac.getUserEmail() + "'", null);
        
        db.close();
        
        return createSuccessful;
    }
    
    public void deleteAllRows()
    {
        SQLiteDatabase db = slDbHelper.getWritableDatabase();
        db.delete(AchievementScheme.TABLE_NAME, null, null);
    }
}
