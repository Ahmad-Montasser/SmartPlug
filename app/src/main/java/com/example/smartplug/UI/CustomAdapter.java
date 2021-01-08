package com.example.smartplug.UI;

import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.Model.MyLocation;
import com.example.smartplug.Model.MyPlug;
import com.example.smartplug.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    int type;
    private ArrayList<MyLocation> locationLocalDataSet;
    private ArrayList<MyPlug> plugLocalDataSet;

    public CustomAdapter(ArrayList dataSet, int i) {
        type = i;
        if (type == 0)
            locationLocalDataSet = dataSet;
        else
            plugLocalDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view;
        if (type == 0) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.location_row_item, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.plugs_row_item, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (type == 0) {
            viewHolder.getLocationTextView().setText(locationLocalDataSet.get(position).getName());
            DecimalFormat df = new DecimalFormat("#.##");
            viewHolder.getLatitudeTextView().setText("Lat: " + df.format(locationLocalDataSet.get(position).getLatitude()));
            viewHolder.getLongitudeTextView().setText("Long: " + df.format(locationLocalDataSet.get(position).getLongitude()));
        } else {
            viewHolder.getPlugNameTextView().setText(plugLocalDataSet.get(position).getPlugName());
            viewHolder.getPlugLocationTextView().setText(plugLocalDataSet.get(position).getLocationName());
            if (plugLocalDataSet.get(position).getStatus() == 1)
                viewHolder.getPlugStatusTextView().setChecked(true);
            else
                viewHolder.getPlugStatusTextView().setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (type == 0)
            return locationLocalDataSet.size();
        else
            return plugLocalDataSet.size();
    }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView latitudeTextView;
        private final TextView longitudeTextView;
        private final TextView plugNameTextView;
        private final TextView plugLocationTextView;

        private final Switch plugStatusSwitch;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            locationTextView = (TextView) view.findViewById(R.id.locationNameTextView);
            latitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView);
            longitudeTextView = (TextView) view.findViewById(R.id.longitudeTextView);
            plugNameTextView = (TextView) view.findViewById(R.id.plugNameTextView);
            plugLocationTextView = (TextView) view.findViewById(R.id.plugLocationTextView);
            plugStatusSwitch = view.findViewById(R.id.plugStatusSwitch);
        }

        public TextView getLatitudeTextView() {
            return latitudeTextView;
        }

        public TextView getLongitudeTextView() {
            return longitudeTextView;
        }

        public Switch getPlugStatusTextView() {
            return plugStatusSwitch;
        }

        public TextView getPlugNameTextView() {
            return plugNameTextView;
        }

        public TextView getPlugLocationTextView() {
            return plugLocationTextView;
        }

        public TextView getLocationTextView() {
            return locationTextView;
        }
    }


}
