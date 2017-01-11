package com.example.pr_idi.mydatabaseexample;

/**
 * MySQLiteHelper
 * Created by pr_idi on 10/11/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PUBLISHER= "publisher";
    public static final String COLUMN_CATEGORY= "category";
    public static final String COLUMN_PERSONAL_EVALUATION = "personal_evaluation";


    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_BOOKS + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_AUTHOR + " text not null, "
            + COLUMN_YEAR + " integer, "
            + COLUMN_PUBLISHER + " text not null, "
            + COLUMN_CATEGORY + " text not null, "
            + COLUMN_PERSONAL_EVALUATION + " text"
            + ");";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, "Metamorphosis");
        values.put(COLUMN_AUTHOR, "Kafka");
        values.put(COLUMN_YEAR, 1915);
        values.put(COLUMN_PUBLISHER, "Kurt Wolff");
        values.put(COLUMN_CATEGORY, "Novela");
        values.put(COLUMN_PERSONAL_EVALUATION,"molt bo");
        long newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "Don Quijote");
        values.put(COLUMN_AUTHOR, "Miguel de Cervantes");
        values.put(COLUMN_YEAR, 1605);
        values.put(COLUMN_PUBLISHER, "Francisco de Robles");
        values.put(COLUMN_CATEGORY, "Aventura");
        values.put(COLUMN_PERSONAL_EVALUATION,"regular");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "Ulysses");
        values.put(COLUMN_AUTHOR, "James Joyce");
        values.put(COLUMN_YEAR, 1922);
        values.put(COLUMN_PUBLISHER, "Sylvia Beach");
        values.put(COLUMN_CATEGORY, "Novela");
        values.put(COLUMN_PERSONAL_EVALUATION,"bo");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "Miguel Strogoff");
        values.put(COLUMN_AUTHOR, "Jules Verne");
        values.put(COLUMN_YEAR, 1876);
        values.put(COLUMN_PUBLISHER, "Pierre-Jules Hetzel");
        values.put(COLUMN_CATEGORY, "Guerra");
        values.put(COLUMN_PERSONAL_EVALUATION,"bo");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "El proceso");
        values.put(COLUMN_AUTHOR, "Kafka");
        values.put(COLUMN_YEAR, 1925);
        values.put(COLUMN_PUBLISHER, "Verlag Die Schmiede");
        values.put(COLUMN_CATEGORY, "Filosofia");
        values.put(COLUMN_PERSONAL_EVALUATION,"regular");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "El castillo");
        values.put(COLUMN_AUTHOR, "Kafka");
        values.put(COLUMN_YEAR, 1926);
        values.put(COLUMN_PUBLISHER, "Kurt Wolff");
        values.put(COLUMN_CATEGORY, "Filosofia");
        values.put(COLUMN_PERSONAL_EVALUATION,"molt dolent");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "Retrato del artista adolescente");
        values.put(COLUMN_AUTHOR, "James Joyce");
        values.put(COLUMN_YEAR, 1916);
        values.put(COLUMN_PUBLISHER, "The Egoist");
        values.put(COLUMN_CATEGORY, "Autobiografia");
        values.put(COLUMN_PERSONAL_EVALUATION,"bo");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "La vuelta al mundo en ochenta dias");
        values.put(COLUMN_AUTHOR, "Jules Verne");
        values.put(COLUMN_YEAR, 1872);
        values.put(COLUMN_PUBLISHER, "Pierre-Jules Hetzel");
        values.put(COLUMN_CATEGORY, "Aventura");
        values.put(COLUMN_PERSONAL_EVALUATION,"molt bo");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "Por quien doblan las campanas");
        values.put(COLUMN_AUTHOR, "Ernest Hemingway");
        values.put(COLUMN_YEAR, 1940);
        values.put(COLUMN_PUBLISHER, "Charles Scribner's Sons");
        values.put(COLUMN_CATEGORY, "Guerra");
        values.put(COLUMN_PERSONAL_EVALUATION,"dolent");
        newRowId = database.insert(TABLE_BOOKS, null, values);

        values = new ContentValues();
        values.put(COLUMN_TITLE, "El gran Gatsby");
        values.put(COLUMN_AUTHOR, "Francis Scott Fitzgerald");
        values.put(COLUMN_YEAR, 1925);
        values.put(COLUMN_PUBLISHER, "Charles Scribner's Sons");
        values.put(COLUMN_CATEGORY, "Novela");
        values.put(COLUMN_PERSONAL_EVALUATION,"bo");
        newRowId = database.insert(TABLE_BOOKS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

}