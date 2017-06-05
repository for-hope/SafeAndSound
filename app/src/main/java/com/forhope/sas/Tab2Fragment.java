package com.forhope.sas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lamine on 5/31/2017.
 */

public class Tab2Fragment extends Fragment {
    public static final String TAG = "Tab1Fragment";
    private String[] dummyStrings;
    private int[] myImageList;
    private int[] colors;
    private RelativeLayout listLayout;
    String m_Text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2,container,false);

        ListView listView = (ListView) view.findViewById(R.id.list);
        dummyStrings = getResources().getStringArray(R.array.my_items);
      // myImageList = getResources().getIntArray(R.array.img_id_arr);
        myImageList = new int[]{R.drawable.ic_perm_contact_calendar_white_24dp, R.drawable.ic_action_tick,R.drawable.ic_schedule_white_24dp,R.drawable.ic_action1_tick,R.drawable.ic_settings_phone_white_24dp,R.drawable.ic_report_problem_white_24dp};
        colors= new int[]{Color.rgb(0,206,209),Color.rgb(60,179,113),R.color.Safe, Color.rgb(255,99,71),Color.rgb(255,140,0),Color.rgb(147,112,219)};
        listLayout = (RelativeLayout) getActivity().findViewById(R.id.list_layout);




        CostumeAdapter costumeAdapter = new CostumeAdapter();
        listView.setAdapter(costumeAdapter);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simple_item_text, dummyStrings);
        //listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getActivity(), "You clicked at position: " + (position + 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                intent.putExtra("string", "go to another Activity from ListView position: " + (position + 1));
                startActivity(intent);*/
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
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
        });

        return  view;
    }
    class CostumeAdapter extends BaseAdapter {

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
            description.setText(dummyStrings[position]);
            ImageView img = (ImageView) convertView.findViewById(R.id.icon_img);
            img.setImageResource(myImageList[position]);
            RelativeLayout imgIcon = (RelativeLayout) convertView.findViewById(R.id.CircleLayout);
            GradientDrawable backgroundGradient = (GradientDrawable)imgIcon.getBackground();
            backgroundGradient.setColor(colors[position]);
            return convertView;
        }
    }
}
