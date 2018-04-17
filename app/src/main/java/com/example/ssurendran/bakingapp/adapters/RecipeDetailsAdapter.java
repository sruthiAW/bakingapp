package com.example.ssurendran.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.activities.RecipeStepDetailActivity;
import com.example.ssurendran.bakingapp.provider.RecipeContract;
import com.example.ssurendran.bakingapp.utils.Constants;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ssurendran on 4/12/18.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.ViewHolder> {

    private static final int INGREDIENT_TYPE = 0;
    private static final int STEPS_TYPE = 1;
    private static final int SELECTED_STEP_TYPE = 2;

    private final Context context;
    private Cursor ingredientCursor;
    private Cursor stepsCursor;
    private String recipeName;
    private int selectedPosition = -1;

    public RecipeDetailsAdapter(Context context, String recipeName, Cursor ingredientCursor, Cursor stepsCursor) {
        this.context = context;
        this.ingredientCursor = ingredientCursor;
        this.stepsCursor = stepsCursor;
        this.recipeName = recipeName;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return INGREDIENT_TYPE;
        }
        else if (position == selectedPosition){
            return SELECTED_STEP_TYPE;
        }
        else {
            return STEPS_TYPE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == INGREDIENT_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_layout, parent, false);
        } else if (viewType == STEPS_TYPE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item_layout, parent, false);
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            if (ingredientCursor != null && ingredientCursor.getCount() > 0) {
                while (ingredientCursor.moveToNext()) {
                    stringBuilder.append(Integer.parseInt(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_INGREDIENT_ID))) + 1)
                            .append(". ")
                            .append(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_NAME)))
                            .append(" -- ")
                            .append(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_QUANTITY)))
                            .append(" ")
                            .append(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_MEASURE)))
                            .append("\n");
                }
                if (!ingredientCursor.moveToNext()){
                    ingredientCursor.moveToPosition(-1);
                }
                holder.ingredientsListTv.setText(stringBuilder.toString());
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            if (stepsCursor != null && stepsCursor.getCount() > 0) {
                stepsCursor.moveToPosition(position-1);

                stringBuilder.append(Integer.parseInt(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_STEP_ID)))+1)
                        .append(". ")
                        .append(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION)));

                holder.stepTextView.setText(stringBuilder.toString());

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    stepsCursor.moveToPosition(position-1);

                    String recipeId = stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_RECIPE_ID));

                    if(!context.getResources().getBoolean(R.bool.isTablet)) {
                        Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                        intent.putExtra(Constants.EXTRA_RECIPE_ID, recipeId);
                        intent.putExtra(Constants.EXTRA_STEP_ID, String.valueOf(position - 1));
                        intent.putExtra(Constants.EXTRA_RECIPE_NAME, recipeName);
                        intent.putExtra(Constants.EXTRA_STEP_COUNT, stepsCursor.getCount());
                        context.startActivity(intent);

                    } else {
                        selectedPosition = position;
                        FragmentChanger fragmentChanger = (FragmentChanger)context;
                        fragmentChanger.changeStepDetailFragment(recipeId, String.valueOf(position - 1));
                        fragmentChanger.saveSelectedPositionInList(selectedPosition);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 1 + stepsCursor.getCount();
    }

    public void refreshData(String recipeName, Cursor ingredientCursor, Cursor stepsCursor, int selectedPosition) {
        this.recipeName = recipeName;
        this.ingredientCursor = ingredientCursor;
        this.stepsCursor = stepsCursor;
        this.selectedPosition = selectedPosition;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.ingredients_list_tv)
        TextView ingredientsListTv;

        @Nullable
        @BindView(R.id.step_tv)
        TextView stepTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FragmentChanger{
        void changeStepDetailFragment(String recipeId, String stepId);
        void saveSelectedPositionInList(int selectedPosition);
    }

}
