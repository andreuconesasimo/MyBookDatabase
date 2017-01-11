package com.example.pr_idi.mydatabaseexample;

/**
 * BookData
 * Created by pr_idi on 10/11/16.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookData {

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    // Here we only select Title and Author, must select the appropriate columns
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_AUTHOR, MySQLiteHelper.COLUMN_PUBLISHER, MySQLiteHelper.COLUMN_YEAR, MySQLiteHelper.COLUMN_CATEGORY, MySQLiteHelper.COLUMN_PERSONAL_EVALUATION};

    private String[] authorColumn = {MySQLiteHelper.COLUMN_AUTHOR};
    private String[] titleColumn = {MySQLiteHelper.COLUMN_TITLE};
    private String[] personalEvaluation = {MySQLiteHelper.COLUMN_PERSONAL_EVALUATION};
    private List<String> allTitles;

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int deleteBook(long id) {
        int rowsAffected = database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
        return rowsAffected;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, "title COLLATE NOCASE");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();

        return books;
    }

    public List<String> getAllAuthors(){
        List<String> authors = new ArrayList<>();
        Cursor cursor = database.query(true,MySQLiteHelper.TABLE_BOOKS,
                authorColumn, null, null, null, null, "author COLLATE NOCASE",null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            authors.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();

        return authors;
    }

    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setPublisher(cursor.getString(3));
        book.setYear(Integer.parseInt(cursor.getString(4)));
        book.setCategory(cursor.getString(5));
        book.setPersonal_evaluation(cursor.getString(6));
        return book;
    }

    public List<Book> getBooksPerAuthor(String author) {
        List<Book> books = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_AUTHOR + " = '" + author+"'", null, null, null, "title COLLATE NOCASE");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();

        return books;
    }

    public String getEvaluation(long id) {
        String evaluation;
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                personalEvaluation, MySQLiteHelper.COLUMN_ID + " = '" + id +"'", null, null, null, null);

        cursor.moveToFirst();
        evaluation = cursor.getString(0);
        cursor.close();

        return evaluation;
    }

    public int updateBookEvaluation(long bookId, String evaluationText) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION,evaluationText);
        int rowsAffected = database.update(MySQLiteHelper.TABLE_BOOKS,values,MySQLiteHelper.COLUMN_ID + " = " + bookId,null);
        return rowsAffected;
    }

    public Book getBook(long bookId) {
        Book book;
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + bookId , null, null, null, null);

        cursor.moveToFirst();
        book = cursorToBook(cursor);
        cursor.close();
        return book;
    }

    public void addBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_AUTHOR, book.getAuthor());
        values.put(MySQLiteHelper.COLUMN_CATEGORY, book.getCategory());
        values.put(MySQLiteHelper.COLUMN_ID, book.getId());
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, book.getPersonal_evaluation());
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, book.getPublisher());
        values.put(MySQLiteHelper.COLUMN_TITLE, book.getTitle());
        values.put(MySQLiteHelper.COLUMN_YEAR, book.getYear());
        database.insert(MySQLiteHelper.TABLE_BOOKS, null, values);
    }
}