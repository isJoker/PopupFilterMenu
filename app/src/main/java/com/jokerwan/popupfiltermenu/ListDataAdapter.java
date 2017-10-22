package com.jokerwan.popupfiltermenu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * Created by ${万嘉诚} on 2016/12/7.
 * WeChat：wjc398556712
 * Function：
 */

public class ListDataAdapter extends BaseAdapter {
    private Context mContext;
    private String[] aears;

    public ListDataAdapter(Context mContext, String[] aears) {
        this.mContext = mContext;
        this.aears = aears;
    }

    @Override
    public int getCount() {
        return aears.length;
    }

    @Override
    public Object getItem(int position) {
        return aears[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        textView.setText(aears[position]);
        textView.setTextSize(15);
        textView.setTextColor(0x66000000);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20,20,20,20);
        return textView;
    }
}
