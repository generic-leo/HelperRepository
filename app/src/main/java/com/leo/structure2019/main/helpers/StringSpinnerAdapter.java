package com.leo.structure2019.main.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leo.homeloan.R;

import java.util.List;

public class StringSpinnerAdapter<T> extends ArrayAdapter<T> {

    private int mResource;
    private LayoutInflater mInflater;
    private List<T> items;

    public StringSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        items = objects;
        mResource = resource;
        mInflater = LayoutInflater.from(context);
    }

    public void addItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public int addItems(List<T> items, String selection) {
        this.items.addAll(items);
        notifyDataSetChanged();

        for (int i=0; i < items.size(); i++){
            if (items.get(i).toString().toLowerCase().contains(selection.toLowerCase()))
                return i;
        }

        return 0;
    }

    public List<T> getItems() {
        return items;
    }

    public T getItem(int position){
        return items.get(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = view.findViewById(R.id.lblSpinnerTextView);
        String string = items.get(position).toString();
        offTypeTv.setText(string);
        return view;
    }


}
