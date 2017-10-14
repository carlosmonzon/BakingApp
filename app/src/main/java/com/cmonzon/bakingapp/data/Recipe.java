package com.cmonzon.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;

/**
 * @author cmonzon
 */

public class Recipe implements Parcelable {

    public int id;

    public String name;

    public int servings;

    public String image;

    public Collection<Ingredient> ingredients;

    public Collection<Step> steps;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
