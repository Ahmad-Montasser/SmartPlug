package com.example.smartplug.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartplug.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<AddLocation.MyLocation> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView latitudeTextView;
        private final TextView longitudeTextView;

        public TextView getLatitudeTextView() {
            return latitudeTextView;
        }

        public TextView getLongitudeTextView() {
            return longitudeTextView;
        }

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            locationTextView = (TextView) view.findViewById(R.id.locationNameTextView);
            latitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView) ;
            longitudeTextView=(TextView) view.findViewById(R.id.longitudeTextView);
        }

        public TextView getLocationTextView() {
            return locationTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(ArrayList<AddLocation.MyLocation> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_row_item, viewGroup, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getLocationTextView().setText(localDataSet.get(position).getName());
        viewHolder.getLatitudeTextView().setText("Lat: "+localDataSet.get(position).getLatitude());
        viewHolder.getLongitudeTextView().setText("Long: "+localDataSet.get(position).getLongitude());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
