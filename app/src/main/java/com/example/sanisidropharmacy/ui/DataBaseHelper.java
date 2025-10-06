package com.example.sanisidropharmacy.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public abstract class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SanIsidroPharmacy.db";
    private static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_USER = "users";

    // user table columns
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    // Item Table Columns
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_ITEM_CONTRACEPTIVES = "Contraceptives";

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}

