package com.cmonzon.bakingapp.data.local;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cmonzon.bakingapp.data.Ingredient;
import com.cmonzon.bakingapp.data.Recipe;
import com.cmonzon.bakingapp.data.RecipesDataSource;
import com.cmonzon.bakingapp.data.Step;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.cmonzon.bakingapp.data.local.RecipesModelContract.RecipeEntry.COLUMN_RECIPE_ID;
import static com.cmonzon.bakingapp.data.local.RecipesModelContract.RecipeEntry.COLUMN_RECIPE_NAME;

/**
 * @author cmonzon
 */

public class RecipesLocalDataSource implements RecipesDataSource {

    public static volatile RecipesLocalDataSource INSTANCE;

    private final BriteDatabase databaseHelper;

    @NonNull
    private static final Function<Cursor, Recipe> recipeMapperFunction = new Function<Cursor, Recipe>() {
        @Override
        public Recipe apply(@NonNull Cursor cursor) throws Exception {
            return DbMapper.getRecipe(cursor);
        }
    };

    @NonNull
    private static final Function<Cursor, Ingredient> ingredientMapperFunction = new Function<Cursor, Ingredient>() {
        @Override
        public Ingredient apply(@NonNull Cursor cursor) throws Exception {
            return DbMapper.getIngredient(cursor);
        }
    };

    @NonNull
    private static final Function<Cursor, Step> stepMapperFunction = new Function<Cursor, Step>() {
        @Override
        public Step apply(@NonNull Cursor cursor) throws Exception {
            return DbMapper.getStep(cursor);
        }
    };


    private RecipesLocalDataSource(Context context) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        databaseHelper = sqlBrite.wrapDatabaseHelper(new RecipesDbHelper(context), Schedulers.io());
    }

    public static RecipesLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        final String tableName = RecipesModelContract.RecipeEntry.TABLE_NAME;
        return databaseHelper.createQuery(tableName, DbMapper.getSelectAllQuery(tableName)).mapToList(recipeMapperFunction);
    }

    @Override
    public Observable<Recipe> getRecipe(int recipeId) {
        final String tableName = RecipesModelContract.RecipeEntry.TABLE_NAME;
        return databaseHelper.createQuery(tableName, DbMapper.getSelectByIdQuery(tableName, COLUMN_RECIPE_ID), String.valueOf(recipeId)).mapToOne(recipeMapperFunction);
    }

    @Override
    public String getRecipeName(int recipeId) {
        String name = "";
        final String[] columns = new String[]{COLUMN_RECIPE_NAME};
        final String[] args = new String[]{String.valueOf(recipeId)};
        final String tableName = RecipesModelContract.RecipeEntry.TABLE_NAME;
        Cursor cursor = databaseHelper.getReadableDatabase().query(tableName, columns, COLUMN_RECIPE_ID + "=?", args, null, null, null);
        if (cursor.moveToFirst()) {
            name = DbMapper.getRecipeName(cursor);
        }
        return name;
    }

    @Override
    public Observable<List<Ingredient>> getRecipeIngredients(int recipeId) {
        final String tableName = RecipesModelContract.IngredientEntry.TABLE_NAME;
        return databaseHelper.createQuery(tableName,
                DbMapper.getSelectByIdQuery(tableName, RecipesModelContract.IngredientEntry.COLUMN_RECIPE_ID), String.valueOf(recipeId))
                .mapToList(ingredientMapperFunction);
    }

    @Override
    public Cursor getRecipeIngredientsCursor(int recipeId) {
        final String[] args = new String[]{String.valueOf(recipeId)};
        final String tableName = RecipesModelContract.IngredientEntry.TABLE_NAME;
        return databaseHelper.getReadableDatabase().rawQuery(DbMapper.
                getSelectByIdQuery(tableName, RecipesModelContract.IngredientEntry.COLUMN_RECIPE_ID), args);
    }

    @Override
    public Observable<List<Step>> getRecipeSteps(int recipeId) {
        final String tableName = RecipesModelContract.StepEntry.TABLE_NAME;
        return databaseHelper.createQuery(tableName,
                DbMapper.getSelectByIdQuery(tableName, RecipesModelContract.StepEntry.COLUMN_RECIPE_ID), String.valueOf(recipeId))
                .mapToList(stepMapperFunction);
    }

    @Override
    public Observable<Step> getRecipeStep(int recipeId, final int index) {
        return getRecipeSteps(recipeId).map(new Function<List<Step>, Step>() {
            @Override
            public Step apply(@NonNull List<Step> steps) throws Exception {
                if (index < steps.size()) {
                    return steps.get(index);
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public void saveRecipes(List<Recipe> recipes) {
        BriteDatabase.Transaction transaction = databaseHelper.newTransaction();
        try {
            for (Recipe recipe : recipes) {
                databaseHelper.insert(RecipesModelContract.RecipeEntry.TABLE_NAME, DbMapper.recipeToContentValues(recipe));
                for (Ingredient ingredient : recipe.ingredients) {
                    databaseHelper.insert(RecipesModelContract.IngredientEntry.TABLE_NAME,
                            DbMapper.ingredientToContentValues(ingredient, recipe.id));
                }
                for (Step step : recipe.steps) {
                    databaseHelper.insert(RecipesModelContract.StepEntry.TABLE_NAME,
                            DbMapper.stepToContentValues(step, recipe.id));
                }
            }
            transaction.markSuccessful();
        } catch (Exception e) {
            Log.e("LocalDS", e.getLocalizedMessage());
        } finally {
            transaction.end();
        }
    }
}
