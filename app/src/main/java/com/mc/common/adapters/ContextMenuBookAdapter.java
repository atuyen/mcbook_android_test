package com.mc.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.mc.books.R;
import com.mc.models.home.DialogBookMenuItem;
import com.skydoves.powermenu.MenuBaseAdapter;

public class ContextMenuBookAdapter  extends MenuBaseAdapter<DialogBookMenuItem>{

    public ContextMenuBookAdapter() {
        super();
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_dialog_book, viewGroup, false);
        }

        DialogBookMenuItem item = (DialogBookMenuItem) getItem(index);
        final ExtTextView title = view.findViewById(R.id.item_title);
        title.setText(item.getTitle());
        return super.getView(index, view, viewGroup);
    }
}
