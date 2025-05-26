package com.example.healthup.Locations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthup.R;
import com.example.healthup.domain.Location;

import java.util.ArrayList;

public class LocationsListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Location> locations;
    private Context context;

    public LocationsListViewAdapter(LayoutInflater inflater, ArrayList<Location> locations, Context context) {
        this.inflater = inflater;
        this.locations = locations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int i) {
        return locations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.location_list_item, container, false);
        }

        Location currentLocation = locations.get(position);
        ((TextView) convertView.findViewById(R.id.locationListName)).setText(currentLocation.getName());
        ImageView viewLocation = convertView.findViewById(R.id.locationListView);

        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewLocationActivity.class);
                intent.putExtra("Location", currentLocation);
                context.startActivity(intent);
//                ((Activity) context).finish();
            }
        });

        ImageView directions = convertView.findViewById(R.id.locationListGoto);

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/dir/?api=1"
                        + "&destination=" + ((Location)getItem(position)).getLat() + "," + ((Location)getItem(position)).getLon()
                        + "&travelmode=driving";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Ensure it opens in Google Maps

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }
}