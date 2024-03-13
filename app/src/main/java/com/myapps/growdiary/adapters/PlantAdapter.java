package com.myapps.growdiary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.myapps.growdiary.R;
import com.myapps.growdiary.interfaces.RecyclerViewInterface;
import com.myapps.growdiary.model.Imager;
import com.myapps.growdiary.model.Plant;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private final String urlNoImage = "https://www.pinclipart.com/picdir/big/564-5642296_plant-clipart-png-download.png";
    private ArrayList<Plant> plants;


    public PlantAdapter(ArrayList<Plant> plants, RecyclerViewInterface recyclerViewInterface) {
        this.plants = plants;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant,parent,false);
        return new PlantViewHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = getItem(position);
        holder.plant_LBL_title.setText(plant.getTitle());
        holder.plant_LBL_status.setText("Status: "+ plant.getStatus());
        holder.plant_LBL_lastUpdate.setText("Last update: " + plant.getLastUpdate());
        if(plant.getType() == Plant.DocumentationType.WILD_DISCOVERY)
            holder.plant_IMG_subscribers.setVisibility(View.VISIBLE);
        else
            holder.plant_IMG_subscribers.setVisibility(View.GONE);

        String imageUrl = plant.getImage();
        if (imageUrl != null && !imageUrl.isEmpty())
            Imager.me().imageCrop(holder.plant_IMG_main, imageUrl);
        else
            Imager.me().imageCrop(holder.plant_IMG_main, urlNoImage);

    }

    private Plant getItem(int position) {
        return plants.get(position);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout plant_IMG_subscribers;
        AppCompatImageView plant_IMG_main;
        MaterialTextView plant_LBL_title;
        MaterialTextView plant_LBL_status;
        MaterialTextView plant_LBL_lastUpdate;

        public PlantViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            plant_IMG_subscribers = itemView.findViewById(R.id.plant_IMG_subscribers);
            plant_IMG_main = itemView.findViewById(R.id.plant_IMG_main);
            plant_LBL_title = itemView.findViewById(R.id.plant_LBL_title);
            plant_LBL_status = itemView.findViewById(R.id.plant_LBL_status);
            plant_LBL_lastUpdate = itemView.findViewById(R.id.plant_LBL_lastUpdate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
