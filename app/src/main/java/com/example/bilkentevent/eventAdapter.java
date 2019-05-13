package com.example.bilkentevent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class eventAdapter extends ArrayAdapter<Event> {

    Context context;
    public eventAdapter(Context context , int resourceId , List<Event>items){
        super(context,resourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        // Get the data item for this position
        Event e = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listevent, parent, false);
        }

        // Lookup view for data population
        TextView topic = (TextView) convertView.findViewById(R.id.topic);
        TextView timeS = (TextView) convertView.findViewById(R.id.timeStart);
        TextView timeE = (TextView) convertView.findViewById(R.id.timeEnd);
        // Populate the data into the template view using the data object
        topic.setText(e.getTopic()+" | ");
        timeS.setText(e.getStartTime() );
        timeE.setText(e.getFinishTime());




        return convertView;
    }



}
