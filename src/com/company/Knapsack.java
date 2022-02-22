package com.company;

// Knapsack object class for working on knapsacks as objects
public class Knapsack {

    private int capacity;
    private int id;
    private int weights[];


    // Knapsack constructor that used for creating knapsack objects
    public Knapsack(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    // getter setter methods to reach necessary values

    public int[] getWeights() {
        return weights;
    }

    public void setWeights(int[] weightsArr) {
        int counter=0;
        this.weights = new int[(weightsArr.length)];
        for(int weight : weightsArr){
            this.weights[counter] = weight;
            counter++;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

