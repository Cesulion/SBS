package com.example.studentbikestation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder>{
    private Context context;
    private List<Station> listStation;
    private DatabaseReference db;


    StationAdapter(Context context, List<Station> listStation)
    {
        this.context = context;
        this.listStation = listStation;
        db = FirebaseDatabase.getInstance().getReference();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.station_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Station station = listStation.get(position);
        viewHolder.locationView.setText(station.getName());

        Drawable d = ContextCompat.getDrawable(context, android.R.drawable.presence_invisible);
        Drawable a = ContextCompat.getDrawable(context, android.R.drawable.presence_online);
        Drawable u = ContextCompat.getDrawable(context, android.R.drawable.presence_busy);
        //////////////////////////////////////////////////////LOCK 1
        if(station.getLock1().equals(""))
        {
            viewHolder.lock1View.setImageDrawable(d);
        }
        else if(station.getLock1().equals("available"))
        {
            viewHolder.lock1View.setImageDrawable(a);
        }
        else
        {
            viewHolder.lock1View.setImageDrawable(u);
        }
        ///////////////////////////////////////////////////////LOCK 2
        if(station.getLock2().equals(""))
        {
            viewHolder.lock2View.setImageDrawable(d);
        }
        else if(station.getLock2().equals("available"))
        {
            viewHolder.lock2View.setImageDrawable(a);
        }
        else
        {
            viewHolder.lock2View.setImageDrawable(u);
        }
        ///////////////////////////////////////////////////////LOCK 3
        if(station.getLock3().equals(""))
        {
            viewHolder.lock3View.setImageDrawable(d);
        }
        else if(station.getLock3().equals("available"))
        {
            viewHolder.lock3View.setImageDrawable(a);
        }
        else
        {
            viewHolder.lock3View.setImageDrawable(u);
        }



    }
    public int getItemCount() {
        return listStation.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView locationView;
        ImageView lock1View;
        ImageView lock2View;
        ImageView lock3View;



        ViewHolder(View itemView) {
            super(itemView);
            locationView = itemView.findViewById(R.id.locationView);
            lock1View = itemView.findViewById(R.id.lock1View);
            lock2View = itemView.findViewById(R.id.lock2View);
            lock3View = itemView.findViewById(R.id.lock3View);


        }


    }

}
