package com.selfielock.database;

import java.io.IOException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.selfielock.database.AchievementContract.AchievementScheme;
import com.selfielock.database.GeoLocationContract.GeoLocationScheme;
import com.selfielock.utils.SLUtils;

public class GeoLocationTransactions extends DatabaseInstance {

    private DatabaseHelper slDbHelper;
    private Context context;

    public GeoLocationTransactions(Context context) 
    {
        super(context);
        this.context = context;
        this.slDbHelper = super.getSLDbHelper();
    }
    
    /*****************/
    /*** Functions ***/
    /*****************/
    
    public boolean AddGeoLocation(LocationCollection listLocations)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();

        String strListLocations = null;
        
        try {
            strListLocations = SLUtils.toString(listLocations);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(GeoLocationScheme.COLUMN_NAME_LIST_GEO_LOCATION, strListLocations);
        values.put(GeoLocationScheme.COLUMN_NAME_USER_EMAIL, listLocations.getUserEmail());

        // Insert the new row, returning the primary key value of the new row
        createSuccessful = db.insert(GeoLocationScheme.TABLE_NAME, null, values) > 0;   
        db.close();
        
        return createSuccessful;
    }
    
    public LocationCollection getAllLocationsByUserEmail(String userEmail)
    {
        LocationCollection listLocations = new LocationCollection();
        SQLiteDatabase db = slDbHelper.getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s = '%s'",
            GeoLocationScheme.TABLE_NAME,
            GeoLocationScheme.COLUMN_NAME_USER_EMAIL,
            userEmail
        );
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst())
        {         
            listLocations.addFromString(
                cursor.getString(cursor.getColumnIndexOrThrow(GeoLocationScheme.COLUMN_NAME_LIST_GEO_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(GeoLocationScheme.COLUMN_NAME_USER_EMAIL))
            );    
        }
        
        cursor.close();
        db.close();
        
        return listLocations;
    }
    
    public boolean updateUserLocations(LocationCollection lc)
    {
        boolean createSuccessful = false;
        
        SQLiteDatabase db = slDbHelper.getWritableDatabase();
        
        String strLocations = null;
 
        try {
            strLocations = SLUtils.toString(lc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ContentValues cv = new ContentValues();
        cv.put(GeoLocationScheme.COLUMN_NAME_LIST_GEO_LOCATION, strLocations);
        
        db.update(GeoLocationScheme.TABLE_NAME, cv, GeoLocationScheme.COLUMN_NAME_USER_EMAIL + "='" + lc.getUserEmail() + "'", null);
        
        db.close();
        
        return createSuccessful;
    }

}
