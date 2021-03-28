package com.example.nasaproject;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WaypointListRecyclerAdapter extends RecyclerView.Adapter {
    public WaypointListRecyclerAdapter(Context context, ArrayList<Waypoint> waypointList) {
        this.context = context;
        this.waypointList = waypointList;
    }

    private Context context;
    private ArrayList<Waypoint> waypointList;

    public static class WaypointRowHolder extends RecyclerView.ViewHolder {
        public WaypointRowHolder(View rowView) {
            super(rowView);
            waypointName = rowView.findViewById(R.id.waypoint_row_name);
            waypointCoords = rowView.findViewById(R.id.waypoint_row_coords);
        }

        public TextView waypointName;
        public TextView waypointCoords;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.row_waypoint, parent, false);
        return new WaypointRowHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WaypointRowHolder waypointRowHolder = (WaypointRowHolder)holder;
        waypointRowHolder.waypointName.setText(waypointList.get(position).id);
        String coords = "[" + waypointList.get(position).xCoord + "," + waypointList.get(position).yCoord + "]";
        waypointRowHolder.waypointCoords.setText(coords);

        // TODO: 3/28/21 Add the listener here, so you can ask the map to highlight some waypoint when selected. 
    }

    @Override
    public int getItemCount() {
        return waypointList.size();
    }
}
