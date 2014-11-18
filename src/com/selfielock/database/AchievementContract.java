package com.selfielock.database;

import android.provider.BaseColumns;

public final class AchievementContract extends DatabaseContract {
    
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public AchievementContract() {}

    /* Inner class that defines the table contents */
    public static abstract class AchievementScheme implements BaseColumns {
        public static final String TABLE_NAME = "achievement";
        public static final String COLUMN_NAME_ENTRY_ID = "achievementId";
        public static final String COLUMN_NAME_LIST_ACHIEVEMENT = "listAchievement";
        public static final String COLUMN_NAME_USER_EMAIL = "user";
        
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + AchievementScheme.TABLE_NAME + " (" +
                        AchievementScheme._ID + " INTEGER PRIMARY KEY," +
                        AchievementScheme.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        AchievementScheme.COLUMN_NAME_LIST_ACHIEVEMENT + TEXT_TYPE + COMMA_SEP +
                        AchievementScheme.COLUMN_NAME_USER_EMAIL + TEXT_TYPE + 
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AchievementScheme.TABLE_NAME;
    } 
}
