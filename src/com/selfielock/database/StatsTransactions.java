package com.selfielock.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.selfielock.database.StatsContract.StatsScheme;
import com.selfielock.database.UserContract.UserScheme;
import com.selfielock.utils.ConnectionStatus;

public class StatsTransactions extends DatabaseInstance {
    
    private DatabaseHelper slDbHelper;

    public StatsTransactions(Context context) 
    {
        super(context);
        this.slDbHelper = super.getSLDbHelper();
    }
    
    /*****************/
    /*** Functions ***/
    /*****************/
    
    public boolean AddStatsToUser(UserEntity user)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StatsScheme.COLUMN_NAME_NUMBER_WIN, 0);
        values.put(StatsScheme.COLUMN_NAME_NUMBER_FAIL, 0);
        values.put(StatsScheme.COLUMN_NAME_TIMES_PLAYED, 0);
        values.put(StatsScheme.COLUMN_NAME_NUMBER_ACHIEVEMENTS, 0);
        values.put(StatsScheme.COLUMN_NAME_USER_EMAIL, user.getEmail());

        // Insert the new row, returning the primary key value of the new row
        createSuccessful = db.insert(StatsScheme.TABLE_NAME, null, values) > 0;   
        db.close();
        
        return createSuccessful;
    }
    
    public boolean updateUserStats(StatsEntity stats)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put(StatsScheme.COLUMN_NAME_NUMBER_WIN, stats.getNumberOfWin());
        cv.put(StatsScheme.COLUMN_NAME_NUMBER_FAIL, stats.getNumberOfFail());
        cv.put(StatsScheme.COLUMN_NAME_TIMES_PLAYED, stats.getTimesPlayed());
        cv.put(StatsScheme.COLUMN_NAME_NUMBER_ACHIEVEMENTS, stats.getAchievementsUnlocked());
        
        db.update(StatsScheme.TABLE_NAME, cv, StatsScheme.COLUMN_NAME_USER_EMAIL + "='" + stats.getUserEmail() + "'", null);
        
        db.close();
        
        return createSuccessful;
    }
    
    public StatsEntity getStatsByUser(UserEntity user)
    {
        StatsEntity stats = null;
        SQLiteDatabase db = slDbHelper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
                StatsScheme.TABLE_NAME,
                StatsScheme.COLUMN_NAME_USER_EMAIL,
                user.getEmail());
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst())
        {
            stats = new StatsEntity(
                cursor.getInt(cursor.getColumnIndexOrThrow(StatsScheme.COLUMN_NAME_NUMBER_WIN)),
                cursor.getInt(cursor.getColumnIndexOrThrow(StatsScheme.COLUMN_NAME_NUMBER_FAIL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(StatsScheme.COLUMN_NAME_TIMES_PLAYED)),
                cursor.getInt(cursor.getColumnIndexOrThrow(StatsScheme.COLUMN_NAME_NUMBER_ACHIEVEMENTS)),
                cursor.getString(cursor.getColumnIndexOrThrow(StatsScheme.COLUMN_NAME_USER_EMAIL))
            );
        }
        
        cursor.close();
        db.close();
        
        return stats;
    }
}
