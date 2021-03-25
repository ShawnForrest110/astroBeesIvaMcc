package com.example.nasaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

//TODO: implement recyclerview for list from telemetry stream
public class TelemetryRowRecyclerAdapter extends RecyclerView.Adapter {

    //TODO: implement constructor when finalizing this adapter class.
    /*public TelemetryRowRecyclerAdapter(Context context, ArrayList<Name>){
        this.context = context;
        this.name = name; //change to array list name when we have it.
    }*/

    private Context context;
    //TODO: add arrayList for telemetry: private ArrayList<Name> name;

    public static class TelemetryRowHolder extends RecyclerView.ViewHolder {
        public TelemetryRowHolder(View rowView) {
            super(rowView);
            telemetryView = rowView.findViewById(R.id.telemetry_row_view);
        }

        public TextView telemetryView;
    }

    @NonNull
    @Override //should call the telemetry row from telemetry_row.xml & return it to fragment/activity
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.telemetry_row, parent, false);
        return new TelemetryRowHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //TODO: add name of array list to get index of data point, change getter name too if needed
        TelemetryRowHolder telemetryRowHolder = (TelemetryRowHolder)holder;
        //telemetryRowHolder.telemetryView.setText(name.get(position).getData);
    }

    @Override
    public int getItemCount() {
        return 0; //return arrayName.size(); when arrayList is added
    }
}
