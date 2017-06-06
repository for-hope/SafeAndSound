package com.forhope.sas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MySQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ContactsDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create contacts table
        String CREATE_CONTACTS_TABLE = "CREATE TABLE contacts ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "contactName TEXT, "+
                "contactNumber TEXT )";

        // create contactss table
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older contactss table if existed
        db.execSQL("DROP TABLE IF EXISTS contacts");

        // create fresh contactss table
        this.onCreate(db);
    }
//---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) contacts + get all contactss + delete all contactss
     */

    // Contactss table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contactss Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "contactName";
    private static final String KEY_NUMBER = "contactNumber";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME, KEY_NUMBER};

    public void addContact(Contacts contacts){
        Log.d("addContacts", contacts.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contacts.getContactName()); // get title
        values.put(KEY_NUMBER, contacts.getContactNumber()); // get author

        // 3. insert
        db.insert(TABLE_CONTACTS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Contacts getContact(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_CONTACTS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build contacts object
        Contacts contacts = new Contacts();
        contacts.setId(Integer.parseInt(cursor.getString(0)));
        contacts.setContactName(cursor.getString(1));
        contacts.setContactNumber(cursor.getString(2));

        Log.d("getContacts("+id+")", contacts.toString());

        // 5. return contacts
        return contacts;
    }

    // Get All Contactss
    public List<Contacts> getAllContacts() {
        List<Contacts> contacts = new LinkedList<Contacts>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_CONTACTS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build contacts and add it to list
        Contacts contact = null;
        if (cursor.moveToFirst()) {
            do {
                contact = new Contacts();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setContactName(cursor.getString(1));
                contact.setContactNumber(cursor.getString(2));

                // Add contacts to contactss
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        Log.d("getAllContactss()", contacts.toString());

        // return contacts
        return contacts;
    }

    // Updating single contacts
    public int updateContact(Contacts contact) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", contact.getContactName()); // get title
        values.put("author", contact.getContactNumber()); // get author

        // 3. updating row
        int i = db.update(TABLE_CONTACTS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(contact.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single contacts
    public void deleteContact(Contacts contact) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_CONTACTS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(contact.getId()) });

        // 3. close
        db.close();

        Log.d("deleteContacts", contact.toString());

    }
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_CONTACTS, null, null);

    }
    public Cursor getRecords() {
        getReadableDatabase();

        // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite

// Get access to the underlying writeable database
        SQLiteDatabase db = this.getReadableDatabase();
// Query for items from the database and get a cursor back
        Cursor todoCursor = db.rawQuery("SELECT rowid _id, * FROM " + TABLE_CONTACTS, null);
        return  todoCursor;
    }
    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_NUMBER + " =?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[] {id});

        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d(TAG, String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
}

