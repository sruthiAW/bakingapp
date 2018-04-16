package com.example.ssurendran.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.example.ssurendran.bakingapp.R;
import com.example.ssurendran.bakingapp.activities.RecipeDetailActivity;
import com.example.ssurendran.bakingapp.activities.RecipeGridActivity;
import com.example.ssurendran.bakingapp.preferences.BakingAppPreferences;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    public static final String RANDOM_NUMBER = "some_random_num";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        BakingAppPreferences preferences = new BakingAppPreferences(context);

        CharSequence noFavRecipeText = "No recipe favorited";
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_layout);

        if (TextUtils.isEmpty(preferences.getFavoriteRecipeId())) {
            views.setTextViewText(R.id.widget_list_header_text, noFavRecipeText);
        } else {
            views.setTextViewText(R.id.widget_list_header_text, preferences.getFavoriteRecipeName());
        }

        views.setRemoteAdapter(R.id.widget_ingredient_list, new Intent(context, BakingWidgetService.class));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateWidgetRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context.getApplicationContext(), appWidgetManager, appWidgetId);
            //to update ingredient list
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

