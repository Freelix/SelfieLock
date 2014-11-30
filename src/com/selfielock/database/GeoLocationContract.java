package com.selfielock.database;

import android.provider.BaseColumns;

public class GeoLocationContract extends DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public GeoLocationContract() {}

    /* Inner class that defines the table contents */
    public static abstract class GeoLocationScheme implements BaseColumns {
        public static final String TABLE_NAME = "geoLocation";
        public static final String COLUMN_NAME_ENTRY_ID = "geoLocationId";
        public static final String COLUMN_NAME_LIST_GEO_LOCATION = "listGeoLocation";
        public static final String COLUMN_NAME_USER_EMAIL = "user";
        
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GeoLocationScheme.TABLE_NAME + " (" +
                        GeoLocationScheme._ID + " INTEGER PRIMARY KEY," +
                        GeoLocationScheme.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        GeoLocationScheme.COLUMN_NAME_LIST_GEO_LOCATION + TEXT_TYPE + COMMA_SEP +
                        GeoLocationScheme.COLUMN_NAME_USER_EMAIL + TEXT_TYPE + 
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GeoLocationScheme.TABLE_NAME;
    } 
}
