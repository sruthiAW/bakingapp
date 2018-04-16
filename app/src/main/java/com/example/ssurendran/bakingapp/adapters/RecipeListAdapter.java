package com.example.ssurendran.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.activities.RecipeDetailActivity;
import com.example.ssurendran.bakingapp.models.RecipeModel;
import com.example.ssurendran.bakingapp.preferences.BakingAppPreferences;
import com.example.ssurendran.bakingapp.widgets.BakingAppWidget;
import com.example.ssurendran.bakingapp.widgets.BakingWidgetService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_ID;
import static com.example.ssurendran.bakingapp.utils.Constants.EXTRA_RECIPE_NAME;

/**
 * Created by ssurendran on 4/10/18.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private static final String TAG = "RecipeListAdapter";
    private static final String FAVORITE = "favorite";
    private static final String NORMAL = "normal";

    private Context context;
    private List<RecipeModel> recipeList = new ArrayList<>();
    private BakingAppPreferences preferences;

    public RecipeListAdapter(Context context, ArrayList<RecipeModel> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        this.preferences = new BakingAppPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RecipeModel recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());

        Picasso.with(context)
                .load(Uri.parse(recipe.getImageUrlString()))
                .placeholder(R.drawable.default_recipe_image)
                .error(R.drawable.default_recipe_image)
                .into(holder.recipeThumbnail);


        if (preferences.getFavoriteRecipeId().equalsIgnoreCase(recipe.getId())) {
            holder.favoriteIcon.setTag(FAVORITE);
            holder.favoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
            holder.favoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
        } else {
            holder.favoriteIcon.setTag(NORMAL);
            holder.favoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.favoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
        }


        holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.favoriteIcon.getTag().equals(FAVORITE)) {
                    holder.favoriteIcon.setTag(NORMAL);
                    holder.favoriteIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
                    holder.favoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
                    preferences.setFavoriteRecipeId("");
                    preferences.setFavoriteRecipeName("");
                } else {
                    holder.favoriteIcon.setTag(FAVORITE);
                    holder.favoriteIcon.setImageResource(R.drawable.ic_star_black_24dp);
                    holder.favoriteIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
                    preferences.setFavoriteRecipeId(recipe.getId());
                    preferences.setFavoriteRecipeName(recipe.getName());
                }

                updateWidgetData();
                notifyDataSetChanged();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
                intent.putExtra(EXTRA_RECIPE_NAME, recipe.getName());
                context.startActivity(intent);
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

    private void updateWidgetData(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));

        BakingAppWidget.updateWidgetRecipe(context, appWidgetManager, appWidgetIds);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_thumbnail)
        ImageView recipeThumbnail;
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.favorite_icon)
        ImageView favoriteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
