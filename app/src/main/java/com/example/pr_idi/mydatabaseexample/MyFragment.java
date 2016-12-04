package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by Andreu on 03/12/2016.
 */
public class MyFragment extends Fragment {
    public final static String KEY_TEXT = "key_text";

    private String mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mText = getArguments().getString(KEY_TEXT);

        View v = inflater.inflate(R.layout.fragment, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tv_fragment);
        tv.setText(mText);

        return v;
    }
}
