package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andreu on 02/12/2016.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

    Context mContext;
    int layoutResourceId;
    String[] data;

    public DrawerAdapter(Context mContext, int layoutResourceId, String[] data) {
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

        String text = data[position];
        ImageView imatge = (ImageView) convertView.findViewById(R.id.menu_image);
        int id;
        switch (text){
            case "Home":
                id = R.drawable.ic_home_black_24dp;
                imatge.setImageResource(id);
                break;
            case "Add book":
                id = R.drawable.ic_add_circle_black_24dp;
                imatge.setImageResource(id);
                break;
            case "Remove book":
                id = R.drawable.ic_delete_black_24dp;
                imatge.setImageResource(id);
            break;
            case "Books of one author":
                id = R.drawable.ic_list_black_24dp;
                imatge.setImageResource(id);
            break;
            case "Change my evaluation":
                id = R.drawable.ic_edit_black_24dp;
                imatge.setImageResource(id);
            break;
            case "Help":
                id = R.drawable.ic_help_black_24dp;
                imatge.setImageResource(id);
            break;
            case "About":
                id = R.drawable.ic_info_black_24dp;
                imatge.setImageResource(id);
            break;
        }
        TextView tv = (TextView) convertView.findViewById(R.id.textViewItem2);
        tv.setText(text);

        return convertView;
    }
}
