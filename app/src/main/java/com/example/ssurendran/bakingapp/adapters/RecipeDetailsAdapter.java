package com.example.ssurendran.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

    private final Context context;
    private Cursor ingredientCursor;
    private Cursor stepsCursor;

    public RecipeDetailsAdapter(Context context, Cursor ingredientCursor, Cursor stepsCursor) {
        this.context = context;
        this.ingredientCursor = ingredientCursor;
        this.stepsCursor = stepsCursor;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return INGREDIENT_TYPE;
        } else {
            return STEPS_TYPE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == INGREDIENT_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item_layout, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            if (ingredientCursor != null && ingredientCursor.getCount() > 0) {
                while (ingredientCursor.moveToNext()) {
                    stringBuilder.append(Integer.parseInt(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_INGREDIENT_ID))) + 1)
                            .append(". ")
                            .append(ingredientCursor.getString(ingredientCursor.getColumnIndex(RecipeContract.IngredientTableColumns.COLUMN_NAME)))
                            .append("\t")
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

                stringBuilder.append(Integer.parseInt(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_STEP_ID))) + 1)
                        .append(". ")
                        .append(stepsCursor.getString(stepsCursor.getColumnIndex(RecipeContract.StepsTableColumns.COLUMN_SHORT_DESCRIPTION)));

                holder.stepTextView.setText(stringBuilder.toString());

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //in phones
                    //TODO: modify for tablets
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_STEP_ID, position-1);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stepsCursor.getCount() + 1;
    }

    public void refreshData(Cursor ingredientCursor, Cursor stepsCursor) {
        this.ingredientCursor = ingredientCursor;
        this.stepsCursor = stepsCursor;
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

}
