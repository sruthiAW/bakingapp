package com.example.ssurendran.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.models.RecipeModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssurendran on 4/10/18.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context context;
    private List<RecipeModel> recipeList = new ArrayList<>();

    public RecipeListAdapter(Context context, ArrayList<RecipeModel> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeModel recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());

        Picasso.with(context)
                .load(Uri.parse(recipe.getImageUrlString()))
                .placeholder(R.drawable.default_recipe_image)
                .error(R.drawable.default_recipe_image)
                .into(holder.recipeThumbnail);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Recipe clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void refreshData(List<RecipeModel> recipeList) {
        this.recipeList = recipeList;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_thumbnail)
        ImageView recipeThumbnail;
        @BindView(R.id.recipe_name)
        TextView recipeName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
