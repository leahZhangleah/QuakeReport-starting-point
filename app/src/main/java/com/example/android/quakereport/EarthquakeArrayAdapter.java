package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeArrayAdapter extends ArrayAdapter<OneEarthquake> {
    public EarthquakeArrayAdapter(@NonNull Context context, @NonNull ArrayList<OneEarthquake> objects) {
        super(context, R.layout.list_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View itemView = convertView;
        if (itemView == null){
            itemView = inflater.inflate(R.layout.list_item,parent,false);
        }
        TextView magView = (TextView) itemView.findViewById(R.id.quake_mag);
        TextView placeView = (TextView) itemView.findViewById(R.id.quake_place);
        TextView placeOffsetView = (TextView) itemView.findViewById(R.id.quake_place_offset);
        TextView dateView = (TextView) itemView.findViewById(R.id.quake_date);
        TextView timeView = (TextView)itemView.findViewById(R.id.quake_time);


        OneEarthquake item = getItem(position);
        //format magnitude to keep only one digit after decimal
        double mag = item.getMagnitude();
        DecimalFormat formatter = new DecimalFormat("0.0");
        String magToDisplay = formatter.format(mag);
        magView.setText(magToDisplay);

        //format the background of magView according to the value of mag
        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();
        int backgroundColor = getMagBackgroundColor(mag);
        magnitudeCircle.setColor(backgroundColor);

        //format place(location) to be shown in two textviews
        String place = item.getPlace();
        if (place.contains("of")){
            int indexOfof = place.indexOf("of");
            String offset = place.substring(0,indexOfof+2);
            String mainLocation = place.substring(indexOfof + 3, place.length());
            placeOffsetView.setText(offset);
            placeView.setText(mainLocation);

        }else{
            placeOffsetView.setText("Near the ");
            placeView.setText(place);
        }

        //format time to human readable date
        long timeInMillseconds = item.getDate();
        Date date = new Date(timeInMillseconds);
        String dateToDisplay = formatDate(date);
        String timeToDisplay = formatTime(date);

        dateView.setText(dateToDisplay);
        timeView.setText(timeToDisplay);

        return itemView;
    }

    private String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM DD, yyyy");
        return formatter.format(date);
    }

    private String formatTime(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(date);
    }

    private int getMagBackgroundColor(double mag){
        int backgroundColor;
        DecimalFormat formatter = new DecimalFormat("0");
        int magToSwitch = Integer.parseInt(formatter.format(mag));
        switch (magToSwitch){
            case 0: case 1:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude1);
                break;
            case 2:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude2);
                break;
            case 3:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude3);
                break;
            case 4:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude4);
                break;
            case 5:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude5);
                break;
            case 6:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude6);
                break;
            case 7:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude7);
                break;
            case 8:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude8);
                break;
            case 9:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude9);
                break;
            default:
                backgroundColor = ContextCompat.getColor(getContext(),R.color.magnitude10plus);
        }
        return backgroundColor;
    }

}
