package com.selfielock.database;

import android.provider.BaseColumns;

public final class StatsContract extends DatabaseContract {
    
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public StatsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class StatsScheme implements BaseColumns {
        public static final String TABLE_NAME = "stats";
        public static final String COLUMN_NAME_ENTRY_ID = "statsId";
        public static final String COLUMN_NAME_NUMBER_WIN = "numberOfWin";
        public static final String COLUMN_NAME_NUMBER_FAIL = "numberOfFail";
        public static final String COLUMN_NAME_TIMES_PLAYED = "timesPlayed";
        public static final String COLUMN_NAME_NUMBER_ACHIEVEMENTS = "achievementsUnlocked";
        public static final String COLUMN_NAME_USER_EMAIL = "userEmail";
        
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + StatsScheme.TABLE_NAME + " (" +
                        StatsScheme._ID + " INTEGER PRIMARY KEY," +
                        StatsScheme.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        StatsScheme.COLUMN_NAME_NUMBER_WIN + INTEGER_TYPE + COMMA_SEP +
                        StatsScheme.COLUMN_NAME_NUMBER_FAIL + INTEGER_TYPE + COMMA_SEP +
                        StatsScheme.COLUMN_NAME_TIMES_PLAYED + INTEGER_TYPE + COMMA_SEP +
                        StatsScheme.COLUMN_NAME_NUMBER_ACHIEVEMENTS + INTEGER_TYPE + COMMA_SEP +
                        StatsScheme.COLUMN_NAME_USER_EMAIL + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + StatsScheme.TABLE_NAME;
    } 
}
