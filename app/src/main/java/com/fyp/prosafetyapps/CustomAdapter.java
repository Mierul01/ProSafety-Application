package com.fyp.prosafetyapps;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import java.util.List;

public class CustomAdapter extends ArrayAdapter<ContactModel> {

    Context context;
    List<ContactModel> contacts;
    public CustomAdapter(@NonNull Context context, List<ContactModel> contacts) {
        super(context, 0, contacts);
        this.context=context;
        this.contacts=contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //create a database helper object to handle the database manipulations
        DBHelper db=new DBHelper(context);

        // Get the data item for this position
        ContactModel c = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_user, parent, false);
        }

        LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.linear);

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        // Populate the data into the template view using the data object
        tvName.setText(c.getName());
        tvPhone.setText(c.getPhoneNo());

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //generate an MaterialSafetyDialog Box
                new SafetyDialog.Builder(context)
                        .setTitle("Remove trusted contact?")
                        .setEyebrow("YOUR SAFETY CIRCLE").setIcon(R.drawable.ic_remove_contact).setDestructive(true)
                        .setMessage("This person will no longer receive your shake-triggered help messages. You can add them again later.")
                        .setPositiveButton("Remove contact", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete the specified contact from the database
                                db.deleteContact(c);
                                //remove the item from the list
                                contacts.remove(c);
                                //notify the listview that dataset has been changed
                                notifyDataSetChanged();
                                SafetyFeedback.show(context, "Contact removed!");
                            }
                        })
                        .setNegativeButton("Keep contact", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                return true;
            }
        });
        convertView.findViewById(R.id.removeContact).setOnClickListener(v -> linearLayout.performLongClick());
        // Return the completed view to render on screen
        return convertView;
    }

    //this method will update the ListView
    public void refresh(List<ContactModel> list){
        contacts.clear();
        contacts.addAll(list);
        notifyDataSetChanged();
    }
}
