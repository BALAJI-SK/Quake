package com.example.quake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakesAdapter extends ArrayAdapter<earthquake> {

    public EarthquakesAdapter(Context context, List<earthquake> objects) {
        super(context, 0, objects);
    }

    static class  ViewHolderItem{
        TextView magnitude, location , date,time,distanceOf;
        String webLink;
    }
    public View getView(int position, View listView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            viewHolderItem  =  new ViewHolderItem();
            viewHolderItem.magnitude= listView.findViewById(R.id.magnitude);
            viewHolderItem.location= listView.findViewById(R.id.location);
            viewHolderItem.date=listView.findViewById(R.id.date);
            viewHolderItem.time=listView.findViewById(R.id.time);
            viewHolderItem.distanceOf= listView.findViewById(R.id.distanceOf);
            viewHolderItem.webLink="";
            listView.setTag(viewHolderItem);
        }
        else{
            viewHolderItem =(ViewHolderItem) listView.getTag();
        }
        earthquake pos = getItem(position);
        //mgnitude
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) viewHolderItem.magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor((int)pos.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        viewHolderItem.magnitude.setText(String.valueOf( pos.getMagnitude()));
        //Location
        String location = pos.getPlace();
        String[] pices = new String[2];
        if(location.contains("of")) {
            pices= location.split("(?<=of)");
        }
        else{
            pices[0]="Near the" ;
            pices[1]=location;
        }
        viewHolderItem.distanceOf.setText(pices[0].trim());
        viewHolderItem.location.setText(pices[1].trim());



        //Date
        Date date = pos.getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String dateToDisplay = dateFormatter.format(date);

        @SuppressLint("SimpleDateFormat")  SimpleDateFormat timeFormatted = new SimpleDateFormat("HH:mm a");
        String timeToDisplay = timeFormatted.format(date);
        viewHolderItem.date.setText((dateToDisplay));
        viewHolderItem.time.setText(timeToDisplay);
        return listView;
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

