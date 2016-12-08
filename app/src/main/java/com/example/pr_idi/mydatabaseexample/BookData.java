package com.example.pr_idi.mydatabaseexample;

/**
 * BookData
 * Created by pr_idi on 10/11/16.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public Book createBook(String title, String author) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + author);

        // Add data: Note that this method only provides title and author
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);

        // Invented data
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, "Do not know");
        values.put(MySQLiteHelper.COLUMN_YEAR, 2030);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, "Fantasia");
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, "regular");

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);

        // Main activity calls this procedure to create a new book
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        // Do not forget to close the cursor
        cursor.close();

        // Return the book
        return newBook;
    }

    public void deleteBook(Book book) {
        long id = book.getId();
        System.out.println("Book deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        Collections.sort(books, new Comparator<Book>() {

            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareTo(b2.getTitle());
            }
        });

        return books;
    }

    public List<String> getAllAuthors(){
        List<String> authors = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                authorColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // AQUEST IF EVITA REPETICIONS --------- PPODRIA FER DISTINCT A LA QUERY________________
            if (!authors.contains(cursor.getString(0))) {
                authors.add(cursor.getString(0));
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Collections.sort(authors);
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
                allColumns, MySQLiteHelper.COLUMN_AUTHOR + " = '" + author+"'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        Collections.sort(books, new Comparator<Book>() {

            public int compare(Book b1, Book b2) {
                return b1.getTitle().compareTo(b2.getTitle());
            }
        });

        return books;
    }

    public List<String> getAllTitles() {

        List<String> titles = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                titleColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // AQUEST IF EVITA REPETICIONS --------- PPODRIA FER DISTINCT A LA QUERY________________
            if (!titles.contains(cursor.getString(0))) {
                titles.add(cursor.getString(0));
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        Collections.sort(titles);
        return titles;
    }

    public String getEvaluation(long id) {
        String evaluation;
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                personalEvaluation, MySQLiteHelper.COLUMN_ID + " = '" + id +"'", null, null, null, null);

        cursor.moveToFirst();
        evaluation = cursor.getString(0);
        // make sure to close the cursor
        cursor.close();

        return evaluation;
    }

    public void updateBookEvaluation(long bookId, String evaluationText) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION,evaluationText);
        database.update(MySQLiteHelper.TABLE_BOOKS,values,MySQLiteHelper.COLUMN_ID + " = " + bookId,null);
    }
}