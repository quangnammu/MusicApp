package com.huynhquangnam.musicapp.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huynhquangnam.musicapp.Data.BaseListElement;
import com.huynhquangnam.musicapp.R;

import java.util.List;

public class ElementListAdapter extends BaseAdapter {

    public List<BaseListElement> listElements;
    public Context context;

    public ElementListAdapter(Context context, List<BaseListElement> listElements){
        this.listElements = listElements;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listElements.size();
    }

    @Override
    public Object getItem(int i) {
        return listElements.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView icon;
        TextView elementName,elementNumber;
        view = View.inflate(context, R.layout.list_item, null);
        icon = view.findViewById(R.id.logo);
        elementName = view.findViewById(R.id.element_name);
        elementNumber = view.findViewById(R.id.element_number);
        icon.setImageResource(listElements.get(i).getIconResource());
        elementName.setText(listElements.get(i).getElementName());
        elementNumber.setText(String.valueOf(listElements.get(i).getNumber()));
        return view;
    }
}
