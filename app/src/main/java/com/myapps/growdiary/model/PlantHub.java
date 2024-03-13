package com.myapps.growdiary.model;

import java.util.ArrayList;

public class PlantHub {
    private ArrayList<Plant> plants;

    public PlantHub(){
        this.plants = new ArrayList<>();
    }

    public PlantHub setPlants(ArrayList<Plant> plants){
        this.plants = plants;
        return this;
    }

    public void addPlants(Plant plant) {
        plants.add(plant);
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public static ArrayList<Plant> getAllPlants() {
        PlantHub plants = MSPV.getMe().readThirtyPlants();
        return plants.getPlants();
    }

}
