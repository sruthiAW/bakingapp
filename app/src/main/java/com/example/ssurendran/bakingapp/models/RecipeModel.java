package com.example.ssurendran.bakingapp.models;

import java.util.List;

/**
 * Created by ssurendran on 4/10/18.
 */

public class RecipeModel {

    private String id;
    private String name;
    private String servings;
    private String imageUrlString;
    private List<IngredientModel> ingredientsList;
    private List<StepModel> stepsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImageUrlString() {
        return imageUrlString;
    }

    public void setImageUrlString(String imageUrlString) {
        this.imageUrlString = imageUrlString;
    }

    public List<IngredientModel> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<IngredientModel> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<StepModel> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<StepModel> stepsList) {
        this.stepsList = stepsList;
    }
}
