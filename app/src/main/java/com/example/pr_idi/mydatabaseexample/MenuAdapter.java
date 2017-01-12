package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.internal.view.SupportActionModeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Andreu on 01/12/2016.
 */
public class MenuAdapter extends ArrayAdapter<Book> {
    private Context mContext;
    private int layoutResourceId;
    private List<Book> data;

    public MenuAdapter(Context mContext, int layoutResourceId, List<Book> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Book book = data.get(position);
        Button button = (Button) convertView.findViewById(R.id.title_button);
        button.setText(book.getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Book currentBook = data.get(position);
                Toast toast = Toast.makeText(mContext, "Title: " + currentBook.getTitle() + "\n" + "Author: " + currentBook.getAuthor() + "\n" + "Category: " + currentBook.getCategory() + "\n" + "Evaluation: " + currentBook.getPersonal_evaluation() + "\n" + "Publisher: " + currentBook.getPublisher() + "\n" + "Year: " + currentBook.getYear(), Toast.LENGTH_LONG);
                toast.getView().setBackgroundResource(R.color.colorAccent);
                toast.getView().setPadding(10,10,10,10);
                toast.show();
            }
        });


        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.edit_book);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Book currentBook = data.get(position);
                ((MainActivity) mContext).editBook(currentBook.getId());
            }
        });

        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_book);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Book currentBook = data.get(position);
                SampleDialogDelete sdd = SampleDialogDelete.newInstance("Are you sure you want to delete the book?",currentBook.getId());
                FragmentTransaction transaction = ((FragmentActivity) mContext).getFragmentManager().beginTransaction();
                sdd.show(transaction,"Alert");
            }
        });

        return convertView;
    }

    @Override
    public int getViewTypeCount() {

        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
