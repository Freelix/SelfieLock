package com.selfielock.database;

import android.provider.BaseColumns;

public final class UserContract extends DatabaseContract {
    
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public UserContract() {}

    /* Inner class that defines the table contents */
    public static abstract class UserScheme implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_ENTRY_ID = "userId";
        public static final String COLUMN_NAME_FIRSTNAME = "firstName";
        public static final String COLUMN_NAME_LASTNAME = "lastName";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_PASSWORD = "password";
        
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserScheme.TABLE_NAME + " (" +
                        UserScheme._ID + " INTEGER PRIMARY KEY," +
                        UserScheme.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_LASTNAME + TEXT_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_IMAGE + BLOB_TYPE + COMMA_SEP +
                        UserScheme.COLUMN_NAME_PASSWORD + TEXT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserScheme.TABLE_NAME;
    } 
}
