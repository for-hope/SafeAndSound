package com.forhope.sas;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Tab2Fragment extends Fragment {
    public static final String TAG = "Tab1Fragment";
    private String[] dummyStrings;
    private String[] dummyDes;
    private int[] myImageList;
    private int[] colors;
    private static final int RESULT_PICK_CONTACT = 1;
    private Contacts contact;
    MySQLiteHelper db;
    List<Contacts> list;
    String m_Text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);
        db = new MySQLiteHelper(getContext());
        list = db.getAllContacts();
        ListView listView = (ListView) view.findViewById(R.id.list);
        dummyStrings = getResources().getStringArray(R.array.my_items);
        dummyDes = getResources().getStringArray(R.array.my_des_items);
      // myImageList = getResources().getIntArray(R.array.img_id_arr);
        myImageList = new int[]{R.drawable.ic_perm_contact_calendar_white_24dp, R.drawable.ic_action_tick,R.drawable.ic_schedule_white_24dp,R.drawable.ic_action1_tick,R.drawable.ic_settings_phone_white_24dp,R.drawable.ic_report_problem_white_24dp};
        colors= new int[]{Color.rgb(0,206,209),Color.rgb(60,179,113),R.color.Safe, Color.rgb(255,99,71),Color.rgb(255,140,0),Color.rgb(147,112,219)};





        CostumeAdapter costumeAdapter = new CostumeAdapter();
        listView.setAdapter(costumeAdapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_item_text, dummyStrings);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        pickContact();
                        break;
                    case 1:
                        costumeSMSDialog();
                        break;
                    case 2:
                        getNumberPicker();
                        break;
                    case 3: {
                        displayInfo();
                        Intent intent = new Intent(getContext(), ContactsListActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(getContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        db.removeAll();
                    }
                }



            }
        });

        return  view;
    }
   private class CostumeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dummyStrings.length ;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(), R.layout.costume_list, null);
            TextView title = (TextView) convertView.findViewById(R.id.textView_title);
            title.setText(dummyStrings[position]);
            TextView description= (TextView) convertView.findViewById(R.id.textView_description);
            description.setText(dummyDes[position]);
            ImageView img = (ImageView) convertView.findViewById(R.id.icon_img);
            img.setImageResource(myImageList[position]);
            RelativeLayout imgIcon = (RelativeLayout) convertView.findViewById(R.id.CircleLayout);
            GradientDrawable backgroundGradient = (GradientDrawable)imgIcon.getBackground();
            backgroundGradient.setColor(colors[position]);
            return convertView;
        }
    }
    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, RESULT_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode ==RESULT_PICK_CONTACT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int column2 =  cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                String number = cursor.getString(column);
                String nameYes = cursor.getString(column2);
                Toast.makeText(getContext(), "Number=" + number + " " + nameYes, Toast.LENGTH_SHORT).show();
                //saveInfo(nameYes,number);
                contact = new Contacts(nameYes,number);
                if (db.hasObject(number)){
                    Toast.makeText(getContext(),"Contact Already Exists",Toast.LENGTH_LONG).show();
                } else {
                    db.addContact(contact);
                }

            }
        }
    }



    public void displayInfo() {
        list = db.getAllContacts();
//        Toast.makeText(getContext(),"Name = " + list.get(0).getContactName() + "         Number = " + list.get(0).getContactNumber() ,Toast.LENGTH_LONG).show();
    }

    private void getNumberPicker() {
        final AlertDialog.Builder d = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("SMS Repeating Period (in MINUTES)");
        d.setMessage("Please choose the period which your SMS will be repeatedly sent in UNSAFE MODE");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(60);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: " + numberPicker.getValue());
                Prefrences.saveInfo(getContext(),"timePeriod",numberPicker.getValue());
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();





    }

    private void costumeSMSDialog() {
        //Adding Dialog/
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Costume SMS");
        builder.setMessage("Choose your costume SMS (Blank For Default SMS");
        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Prefrences.saveInfo(getContext(),"costumeSMS",m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
