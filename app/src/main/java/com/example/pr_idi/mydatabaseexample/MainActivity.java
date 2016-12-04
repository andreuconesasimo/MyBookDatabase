package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BookData bookData;
    private String[] options;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView titlesListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private View booksAuthorView;
    private FrameLayout booksAuthorFrame;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    View booksExpandableView;
    private ArrayAdapter<String> dataAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mTitle = getTitle();
        booksAuthorFrame = (FrameLayout) findViewById(R.id.content_frame);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // inicialitzar menu lateral
        options = getResources().getStringArray(R.array.string_array_name);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.menu_list);
        // Set the adapter for the list view
        DrawerAdapter adp = new DrawerAdapter(this,R.layout.drawer_list_item, options);
        mDrawerList.setAdapter(adp);
        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_menu_white_24dp, R.string.open_drawer,
                R.string.close_drawer) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Choose one option");
                // creates call to onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        // LLISTAT LLIBRES PER TITOL
        bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.list_view_home,null);
        titlesListView = (ListView) v.findViewById(R.id.books_list);
        MenuAdapter adapter2 = new MenuAdapter(this, R.layout.list_view_row_item, values);
        //set the adapter
        titlesListView.setAdapter(adapter2);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(v);
        bookData.close();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        String opcioMenu = options[position];
        switch (opcioMenu){
            case "Home":
                bookData.open();
                List<Book> values = bookData.getAllBooks();
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.list_view_home,null);
                titlesListView = (ListView) v.findViewById(R.id.books_list);
                MenuAdapter adapter2 = new MenuAdapter(this, R.layout.list_view_row_item, values);
                //set the adapter
                titlesListView.setAdapter(adapter2);
                FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
                frame.removeAllViews();
                frame.addView(v);
                bookData.close();
                break;
            case "Add book":
                break;
            case "Remove book":
                break;
            case "Books of one author":
                booksAuthorFrame.removeAllViews();
                // OMPLIR SPINNER
                bookData.open();
                List<String> authors = bookData.getAllAuthors();
                LayoutInflater inflater2 = getLayoutInflater();
                booksAuthorView = inflater2.inflate(R.layout.books_author_view, null);
                Spinner spinner = (Spinner) booksAuthorView.findViewById(R.id.spinner);
                dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, authors);
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                spinner.setAdapter(dataAdapter);
                // SET SPINNER SELECTED LISTENER
                spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());


                booksExpandableView = inflater2.inflate(R.layout.expandable_list_view, null);
                ExpandableListView expandableListView = (ExpandableListView) booksExpandableView.findViewById(R.id.books_author_expandable_list_view);
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        Toast.makeText(
                                getApplicationContext(),
                                listDataHeader.get(groupPosition)
                                        + " : "
                                        + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                });

                // Listview Group expanded listener
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // Listview Group collasped listener
                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Collapsed",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                ExpandableListAdapter listAdapter = new ExpandableListAdapter(getBaseContext(),listDataHeader,listDataChild);
                expandableListView.setAdapter(listAdapter);
                booksAuthorFrame.addView(booksAuthorView);
                booksAuthorFrame.addView(booksExpandableView);

                break;
            case "Change my evaluation":
                break;
            case "Help":
                break;
            case "About":
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = opcioMenu;
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class SpinnerItemSelectedListener implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            String author = parent.getItemAtPosition(position).toString();
            List<Book> authorBooks = bookData.getBooksPerAuthor(author);
            int i = 0;
            listDataHeader.clear();
            listDataChild.clear();
            for (Book b : authorBooks){
                listDataHeader.add(b.getTitle());
                List<String> bookInfo = new ArrayList<String>();
                bookInfo.add(b.getCategory());
                bookInfo.add(b.getPersonal_evaluation());
                bookInfo.add(b.getPublisher());
                bookInfo.add(String.valueOf(b.getYear()));
                listDataChild.put(listDataHeader.get(i), bookInfo); // Header, Child data
                ++i;
            }

            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.books_author_expandable_list_view);
            ExpandableListAdapter listAdapter = new ExpandableListAdapter(getBaseContext(),listDataHeader,listDataChild);
            expandableListView.setAdapter(listAdapter);
            booksAuthorFrame.removeView(booksExpandableView);
            booksAuthorFrame.addView(booksExpandableView);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Called by the system when the device configuration changes while your
        // activity is running
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    public void getBookInfo(View view){
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Toast toast = Toast.makeText(this, buttonText, Toast.LENGTH_SHORT);
        toast.show();
    }

}