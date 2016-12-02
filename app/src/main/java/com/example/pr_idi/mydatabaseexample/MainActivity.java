package com.example.pr_idi.mydatabaseexample;


import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private BookData bookData;
    private String[] options;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView titlesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // inicialitzar menu lateral
        options = getResources().getStringArray(R.array.string_array_name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.menu_list);
        // Set the adapter for the list view
        DrawerAdapter adp = new DrawerAdapter(this,R.layout.drawer_list_item,options);
        mDrawerList.setAdapter(adp);

        bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();
        titlesListView = (ListView) findViewById(R.id.books_list);
        MenuAdapter adapter = new MenuAdapter(this, R.layout.list_view_row_item, values);
        //set the adapter
        titlesListView.setAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        /*@SuppressWarnings("unchecked")
        ArrayAdapter<Book> adapter = (ArrayAdapter<Book>) getListAdapter();
        Book book;
        adapter.notifyDataSetChanged();*/
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }
    public void getBookInfo(View view){
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Toast toast = Toast.makeText(this, buttonText, Toast.LENGTH_SHORT);
        toast.show();
    }
}