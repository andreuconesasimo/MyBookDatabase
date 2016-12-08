package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andreu on 08/12/2016.
 */

public class SpinnerAdapter extends ArrayAdapter {

    private Context context;
    private List<Book> bookList;
    private int resourceId;

    public SpinnerAdapter(Context context, int resourceId, List<Book> bookList) {
        super(context, resourceId, bookList);
        this.context = context;
        this.bookList = bookList;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);
        }

        Book b = bookList.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.spinner_item_text);
        tv.setText(b.toString());
        convertView.setId((int) b.getId());
        return convertView;
    }
}
