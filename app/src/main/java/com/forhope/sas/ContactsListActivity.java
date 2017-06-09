package com.forhope.sas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ContactsListActivity extends Activity {
    ListView listView;
    MySQLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        db = new MySQLiteHelper(this);
        listView = (ListView) findViewById(R.id.contactlist);
        Cursor todoCursor = db.getRecords();
        TodoCursorAdapter todoAdapter = new TodoCursorAdapter(this, todoCursor);
        if(todoAdapter.getCount()>0) {
            listView.setAdapter(todoAdapter);
       } else {
            listView.setEmptyView(findViewById(R.id.emptyview));
        }


    }
}
