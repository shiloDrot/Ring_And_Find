package com.example.shilo90.ringandfind;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by shilo90 on 03/09/15.
 */
public class SpinnerAdapter extends ArrayAdapter<CharSequence> {


    public SpinnerAdapter(Context context, int resource, CharSequence[] objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount(){
        return super.getCount() - 1;
    }
}
