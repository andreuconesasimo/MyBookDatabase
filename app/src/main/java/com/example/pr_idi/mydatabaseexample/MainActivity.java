package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BookData bookData;
    private DrawerLayout mDrawerLayout;
    private ListView titlesListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private View booksAuthorView, changeEvaluationView, booksExpandableView, aboutView, helpView;
    private NavigationView navigationView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private long bookId;
    private Toolbar toolbar;
    private FrameLayout frame;
    private ArrayAdapter<String> dataAdapter;
    private ArrayAdapter<CharSequence> adapterSpinnerEvaluation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mTitle = getTitle();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.navigationView.setNavigationItemSelectedListener(new NavigationViewClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {

            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Choose one option");
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        carregarVistaPrincipal();
    }

    private void carregarVistaPrincipal(){
        if (bookData == null) bookData = new BookData(this);
        bookData.open();
        List<Book> values = bookData.getAllBooks();
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.list_view_home,null);
        titlesListView = (ListView) v.findViewById(R.id.books_list);
        MenuAdapter adapter2 = new MenuAdapter(this, R.layout.list_view_row_item, values);
        titlesListView.setAdapter(adapter2);
        frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.removeAllViews();
        frame.addView(v);
        bookData.close();
    }

    private class NavigationViewClickListener implements  NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            boolean resultat = true;
            mDrawerLayout.closeDrawers();
            selectItem(menuItem);
            return resultat;
        }
    }

    private void selectItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.vista_principal:
                carregarVistaPrincipal();
                break;
            case R.id.add_book:
                break;
            case R.id.remove_book:
                break;
            case R.id.books_of_one_author:
                frame.removeAllViews();
                bookData.open();
                List<String> authors = bookData.getAllAuthors();
                LayoutInflater inflater2 = getLayoutInflater();
                booksAuthorView = inflater2.inflate(R.layout.books_author_view, null);
                Spinner spinner = (Spinner) booksAuthorView.findViewById(R.id.spinner);
                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, authors);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
                booksExpandableView = inflater2.inflate(R.layout.expandable_list_view, null);
                ExpandableListView expandableListView = (ExpandableListView) booksExpandableView.findViewById(R.id.books_author_expandable_list_view);
                ExpandableListAdapter listAdapter = new ExpandableListAdapter(getBaseContext(), listDataHeader, listDataChild);
                expandableListView.setAdapter(listAdapter);
                frame.addView(booksAuthorView);
                frame.addView(booksExpandableView);
                bookData.close();
                break;
            case R.id.change_my_evaluation:
                frame.removeAllViews();
                bookData.open();
                List<Book> books = bookData.getAllBooks();
                LayoutInflater inflater3 = getLayoutInflater();
                changeEvaluationView = inflater3.inflate(R.layout.change_evaluation_view, null);
                Spinner spinner2 = (Spinner) changeEvaluationView.findViewById(R.id.spinner_evaluation);
                SpinnerAdapter adp = new SpinnerAdapter(this,getApplicationContext(), R.layout.spinner_row_item, books);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adp);
                spinner2.setOnItemSelectedListener(new SpinnerChangeEvaluationItemSelectedListener());

                Spinner evaluationView = (Spinner) changeEvaluationView.findViewById(R.id.valoracio);
                adapterSpinnerEvaluation = ArrayAdapter.createFromResource(this, R.array.evaluation_array, android.R.layout.simple_spinner_item);
                adapterSpinnerEvaluation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                evaluationView.setAdapter(adapterSpinnerEvaluation);

                ImageButton imageButton = (ImageButton) changeEvaluationView.findViewById(R.id.save_book);
                imageButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(imageButton));
                frame.addView(changeEvaluationView);
                bookData.close();
                break;
            case R.id.help:
                frame.removeAllViews();
                LayoutInflater inflater4 = getLayoutInflater();
                helpView = inflater4.inflate(R.layout.help,null);
                frame.addView(helpView);
                break;
            case R.id.about:
                frame.removeAllViews();
                LayoutInflater inflater5 = getLayoutInflater();
                aboutView = inflater5.inflate(R.layout.about,null);
                frame.addView(aboutView);
                break;
            default:
                break;
        }
        mTitle = menuItem.getTitle().toString();
        getSupportActionBar().setTitle(mTitle);
    }

    private class SpinnerChangeEvaluationItemSelectedListener implements Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            bookData.open();
            //EditText evaluation = (EditText) findViewById(R.id.text_area_evaluation);
            Spinner evaluation = (Spinner) findViewById(R.id.valoracio);
            String eva = bookData.getEvaluation(view.getId());
            if (!eva.equals(null)){
                int spinnerPosition = adapterSpinnerEvaluation.getPosition(eva);
                evaluation.setSelection(spinnerPosition);
            }
            bookId = view.getId();
            bookData.close();
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
            bookData.open();
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
            bookData.close();
            frame.removeView(booksExpandableView);
            frame.addView(booksExpandableView);
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
                Spinner evaluation = (Spinner) findViewById(R.id.valoracio);
                String text = evaluation.getSelectedItem().toString();
                SampleDialog sd = SampleDialog.newInstance("Are you sure you want to modify your book's evaluation?",text,bookId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sd.show(transaction,"Alert");
                break;
        }

    }

    public void onClickHelp (View v)
    {
        int id = v.getId ();
        switch (id) {
            case R.id.help_button1 :
                Toast toast = Toast.makeText(this, "boto ajuda apretat", Toast.LENGTH_LONG);
                toast.show();
                break;
            default:
                break;
        }
    }

    public void getBookInfo(View view){
        BookData bookData2 = new BookData(this);
        bookData2.open();
        Book book = bookData2.getBook(view.getId());
        bookData2.close();
        Toast toast = Toast.makeText(this, book.getTitle() + "\n" + book.getAuthor() + "\n" + book.getCategory() + "\n" + book.getPersonal_evaluation() + "\n" + book.getPublisher() + "\n" + book.getYear(), Toast.LENGTH_LONG);
        toast.show();
    }

    public void editBook(View view){
        mTitle = "Change my evaluation";
        getSupportActionBar().setTitle(mTitle);
        frame.removeAllViews();
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        LayoutInflater inflater3 = getLayoutInflater();
        changeEvaluationView = inflater3.inflate(R.layout.change_evaluation_view, null);
        Spinner spinner2 = (Spinner) changeEvaluationView.findViewById(R.id.spinner_evaluation);
        SpinnerAdapter adp = new SpinnerAdapter(this, this,R.layout.spinner_row_item,books);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adp);
        int spinnerPosition = adp.getPosition(view.getId());
        spinner2.setSelection(spinnerPosition);
        spinner2.setOnItemSelectedListener(new SpinnerChangeEvaluationItemSelectedListener());

        Spinner evaluationView = (Spinner) changeEvaluationView.findViewById(R.id.valoracio);
        adapterSpinnerEvaluation = ArrayAdapter.createFromResource(this, R.array.evaluation_array, android.R.layout.simple_spinner_item);
        adapterSpinnerEvaluation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        evaluationView.setAdapter(adapterSpinnerEvaluation);

        ImageButton imageButton = (ImageButton) changeEvaluationView.findViewById(R.id.save_book);
        imageButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(imageButton));
        bookData.close();
        frame.addView(changeEvaluationView);
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
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        //return super.onPrepareOptionsMenu(menu);
        return false;
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
        /*if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

}