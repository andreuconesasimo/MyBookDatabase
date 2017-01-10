package com.example.pr_idi.mydatabaseexample;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
    private Boolean vistaPrincipal;

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

    public void carregarVistaPrincipal(){
        mTitle = "Home";
        getSupportActionBar().setTitle(mTitle);
        vistaPrincipal = true;
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

    private void carregarVistaBooksAuthor(){
        vistaPrincipal = false;
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
    }

    private void carregarVistaChangeEvaluation(){
        vistaPrincipal = false;
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

        frame.addView(changeEvaluationView);
        bookData.close();
    }

    private void carregarVistaHelp(){
        vistaPrincipal = false;
        frame.removeAllViews();
        LayoutInflater inflater4 = getLayoutInflater();
        helpView = inflater4.inflate(R.layout.help,null);
        frame.addView(helpView);
    }

    private void carregarVistaAbout(){
        vistaPrincipal = false;
        frame.removeAllViews();
        LayoutInflater inflater5 = getLayoutInflater();
        aboutView = inflater5.inflate(R.layout.about,null);
        frame.addView(aboutView);
    }

    private class NavigationViewClickListener implements  NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();
            selectItem(menuItem);
            return true;
        }
    }

    private void carregarMyBooks() {
        vistaPrincipal = false;
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        bookData.close();

        frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.removeAllViews();
        frame.addView(new DetailsRecyclerView(this, books));
    }

    private void selectItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.vista_principal:
                carregarVistaPrincipal();
                break;
            case R.id.add_book:
                break;
            case R.id.my_books:
                carregarMyBooks();
                break;
            case R.id.books_of_one_author:
                carregarVistaBooksAuthor();
                break;
            case R.id.change_my_evaluation:
                carregarVistaChangeEvaluation();
                break;
            case R.id.help:
                carregarVistaHelp();
                break;
            case R.id.about:
                carregarVistaAbout();
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
            String author = parent.getItemAtPosition(position).toString();
            bookData.open();
            List<Book> authorBooks = bookData.getBooksPerAuthor(author);
            int i = 0;
            listDataHeader.clear();
            listDataChild.clear();
            for (Book b : authorBooks){
                listDataHeader.add(b.getTitle());
                List<String> bookInfo = new ArrayList<String>();
                bookInfo.add("Category: " + b.getCategory());
                bookInfo.add("Evaluation: " + b.getPersonal_evaluation());
                bookInfo.add("Publisher: " + b.getPublisher());
                bookInfo.add("Year: " + String.valueOf(b.getYear()));
                listDataChild.put(listDataHeader.get(i), bookInfo);
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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_book:
                Spinner evaluation = (Spinner) findViewById(R.id.valoracio);
                String text = evaluation.getSelectedItem().toString();
                SampleDialogEdit sde = SampleDialogEdit.newInstance("Are you sure you want to modify your book's evaluation?",text,bookId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                sde.show(transaction,"Alert");
                break;
        }
    }

    public void getBookInfo(View view){
        BookData bookData2 = new BookData(this);
        bookData2.open();
        Book book = bookData2.getBook(view.getId());
        bookData2.close();
        Toast toast = Toast.makeText(this, "Title: " + book.getTitle() + "\n" + "Author: " + book.getAuthor() + "\n" + "Category: " + book.getCategory() + "\n" + "Evaluation: " + book.getPersonal_evaluation() + "\n" + "Publisher: " + book.getPublisher() + "\n" + "Year: " + book.getYear(), Toast.LENGTH_LONG);
        toast.getView().setBackgroundResource(R.color.colorAccent);
        toast.getView().setPadding(10,10,10,10);
        toast.show();
    }

    public void editBook(View view){
        vistaPrincipal = false;
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

        bookData.close();
        frame.addView(changeEvaluationView);
    }

    public void deleteBook(View view){
        SampleDialogDelete sdd = SampleDialogDelete.newInstance("Are you sure you want to delete the book?",view.getId());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        sdd.show(transaction,"Alert");
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!vistaPrincipal){
            carregarVistaPrincipal();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Exit app?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

}