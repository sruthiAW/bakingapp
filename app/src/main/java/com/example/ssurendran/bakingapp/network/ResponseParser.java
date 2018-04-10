package com.example.ssurendran.bakingapp.network;

import com.example.ssurendran.bakingapp.models.IngredientModel;
import com.example.ssurendran.bakingapp.models.RecipeModel;
import com.example.ssurendran.bakingapp.models.StepModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ssurendran on 4/10/18.
 */

public class ResponseParser {

    public List<RecipeModel> parseRecipeList(String responseString) throws JSONException {
        List<RecipeModel> recipeList = new ArrayList<>();

        JSONArray mainJsonArray = new JSONArray(responseString);

        for(int i = 0; i < mainJsonArray.length(); i++){
            RecipeModel recipeModel = new RecipeModel();
            List<IngredientModel> ingredientList = new ArrayList<>();
            List<StepModel> stepList = new ArrayList<>();

            JSONObject arrayObject = mainJsonArray.getJSONObject(i);
            recipeModel.setId(arrayObject.getString("id"));
            recipeModel.setName(arrayObject.getString("name"));
            recipeModel.setServings(arrayObject.getString("servings"));
            recipeModel.setImageUrlString(arrayObject.getString("image"));

            JSONArray ingredientArray = arrayObject.getJSONArray("ingredients");
            for (int j = 0; j < ingredientArray.length(); j++){
                IngredientModel ingredient = new IngredientModel();
                JSONObject ingredientArrayObject = ingredientArray.getJSONObject(j);
                ingredient.setName(ingredientArrayObject.getString("ingredient"));
                ingredient.setQuantity(ingredientArrayObject.getString("quantity"));
                ingredient.setMeasure(ingredientArrayObject.getString("measure"));
                ingredientList.add(ingredient);
            }
            recipeModel.setIngredientsList(ingredientList);

            JSONArray stepsArray = arrayObject.getJSONArray("steps");
            for (int j = 0; j < stepsArray.length(); j++){
                StepModel step = new StepModel();
                JSONObject stepArrayObject = stepsArray.getJSONObject(j);
                step.setId(stepArrayObject.getString("id"));
                step.setShortDescription(stepArrayObject.getString("shortDescription"));
                step.setDescription(stepArrayObject.getString("description"));
                step.setVideoUrlString(stepArrayObject.getString("videoURL"));
                step.setThumbnailUrlString(stepArrayObject.getString("thumbnailURL"));
                stepList.add(step);
            }
            recipeModel.setStepsList(stepList);

            recipeList.add(recipeModel);
        }

        return recipeList;
    }
}
