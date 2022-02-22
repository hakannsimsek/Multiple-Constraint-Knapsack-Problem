package com.company;

// Item object class for working on items as objects
public class Item {

    private int id;
    private int value;
    private int[] weights;
    private boolean isOccupied;

    // item constructor that used for creating item objects
    public Item(int id, int value) {
        this.id = id;
        this.value = value;
        this.isOccupied = false;
    }

    // getter setter methods to reach necessary values

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
