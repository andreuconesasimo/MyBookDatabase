package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
    private View changeEvaluationView;
    private FrameLayout booksAuthorFrame;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private EditText et;
    private long bookId;

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

        // especificar ladaptador del listview lateral
        DrawerAdapter adp = new DrawerAdapter(this,R.layout.drawer_list_item, options);
        mDrawerList.setAdapter(adp);
        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0288D1")));
        getSupportActionBar().setTitle("Home");

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_menu_white_24dp, R.string.open_drawer,
                R.string.close_drawer) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Choose one option");
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        // Llistat de llibres per titol
        bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.list_view_home,null);
        titlesListView = (ListView) v.findViewById(R.id.books_list);
        MenuAdapter adapter2 = new MenuAdapter(this, R.layout.list_view_row_item, values);
        //Set the adapter
        titlesListView.setAdapter(adapter2);
        booksAuthorFrame.addView(v);
        bookData.close();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

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
                bookData.open();
                List<String> authors = bookData.getAllAuthors();
                LayoutInflater inflater2 = getLayoutInflater();
                booksAuthorView = inflater2.inflate(R.layout.books_author_view, null);
                Spinner spinner = (Spinner) booksAuthorView.findViewById(R.id.spinner);
                dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, authors);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
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

                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();
                    }
                });

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
                booksAuthorFrame.removeAllViews();
                bookData.open();
                List<Book> books = bookData.getAllBooks();
                LayoutInflater inflater3 = getLayoutInflater();
                changeEvaluationView = inflater3.inflate(R.layout.change_evaluation_view, null);
                Spinner spinner2 = (Spinner) changeEvaluationView.findViewById(R.id.spinner_evaluation);

                SpinnerAdapter adp = new SpinnerAdapter(this,R.layout.spinner_row_item,books);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adp);

                spinner2.setOnItemSelectedListener(new SpinnerChangeEvaluationItemSelectedListener());
                ImageButton imageButton = (ImageButton) changeEvaluationView.findViewById(R.id.save_book);
                imageButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(imageButton));
                booksAuthorFrame.addView(changeEvaluationView);
                break;
            case "Help":
                break;
            case "About":
                break;
        }

        mDrawerList.setItemChecked(position, true);
        mTitle = opcioMenu;
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class SpinnerChangeEvaluationItemSelectedListener implements Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            EditText evaluation = (EditText) findViewById(R.id.text_area_evaluation);
            String eva = bookData.getEvaluation(view.getId());
            evaluation.setText(eva);
            et = evaluation;
            bookId = view.getId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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
    // Nota : han de ser botons als que no sels hi hagi canviat el ID
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_book:
                SampleDialog sd = SampleDialog.newInstance("Are you sure you want to modify your book's evaluation?",et.getText().toString(),bookId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sd.show(transaction,"Alert");
                break;
        }

    }

    public void getBookInfo(View view){
        BookData bookData2 = new BookData(this);
        bookData2.open();
        Book book = bookData2.getBook(view.getId());
        bookData2.close();
        Toast toast = Toast.makeText(this, book.toString() + "\n" + book.getCategory() + "\n" + book.getPersonal_evaluation() + "\n" + book.getPublisher() + "\n" + book.getYear(), Toast.LENGTH_LONG);
        toast.show();
    }

    public void editBook(View view){
        mTitle = "Change my evaluation";
        getSupportActionBar().setTitle(mTitle);
        booksAuthorFrame.removeAllViews();
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        LayoutInflater inflater3 = getLayoutInflater();
        changeEvaluationView = inflater3.inflate(R.layout.change_evaluation_view, null);
        Spinner spinner2 = (Spinner) changeEvaluationView.findViewById(R.id.spinner_evaluation);

        SpinnerAdapter adp = new SpinnerAdapter(this,R.layout.spinner_row_item,books);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adp);

        int spinnerPosition = adp.getPosition(view.getId());
        spinner2.setSelection(spinnerPosition);

        spinner2.setOnItemSelectedListener(new SpinnerChangeEvaluationItemSelectedListener());
        ImageButton imageButton = (ImageButton) changeEvaluationView.findViewById(R.id.save_book);
        imageButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(imageButton));
        booksAuthorFrame.addView(changeEvaluationView);
    }

    public void deleteBook(View view){

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

}