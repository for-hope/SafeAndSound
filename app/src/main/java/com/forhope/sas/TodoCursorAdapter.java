package com.forhope.sas;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.costume_check_list, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView contactTitle = (TextView) view.findViewById(R.id.textView_title);
        TextView contactNumber = (TextView) view.findViewById(R.id.textView_description);
        TextView contactLetter = (TextView) view.findViewById(R.id.contactletter);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow("contactName"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("contactNumber"));
        contactLetter.setText(String.valueOf(body.charAt(0)));
        // Populate fields with extracted properties
        contactTitle.setText(body);
        contactNumber.setText(String.valueOf(priority));
    }
}