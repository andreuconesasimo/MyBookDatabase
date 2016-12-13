package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by Andreu on 01/12/2016.
 */
public class MenuAdapter extends ArrayAdapter<Book> {
    Context mContext;
    int layoutResourceId;
    List<Book> data;

    public MenuAdapter(Context mContext, int layoutResourceId, List<Book> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Book book = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        Button button = (Button) convertView.findViewById(R.id.title_button);
        button.setText(book.getTitle());
        button.setId((int) book.getId());
        button.setOnTouchListener(new ButtonHighlighterOnTouchListener(button));

        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.edit_book);
        editButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(editButton));
        editButton.setId((int) book.getId());
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_book);
        deleteButton.setOnTouchListener(new ImageButtonHighlighterOnTouchListener(deleteButton));
        deleteButton.setId((int) book.getId());
        return convertView;
    }
}
