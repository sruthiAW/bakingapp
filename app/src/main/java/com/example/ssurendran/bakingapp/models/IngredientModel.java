package com.example.ssurendran.bakingapp.models;

/**
 * Created by ssurendran on 4/10/18.
 */

public class IngredientModel {

    private String name;
    private String measure;
    private String quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
