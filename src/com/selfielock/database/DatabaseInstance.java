package com.selfielock.database;

import android.content.Context;

public class DatabaseInstance {

    private DatabaseHelper slDbHelper;
    
    public DatabaseInstance(Context context)
    {
        slDbHelper = new DatabaseHelper(context);
    }
    
    public DatabaseHelper getSLDbHelper()
    {
        return slDbHelper;
    }
    
}
