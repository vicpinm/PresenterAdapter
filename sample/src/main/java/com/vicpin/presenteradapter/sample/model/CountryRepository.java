package com.vicpin.presenteradapter.sample.model;

import android.content.res.Resources;

import com.vicpin.presenteradapter.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 25/06/2016.
 */
public class CountryRepository {

    private static final int PAGE_SIZE = 30;

    public static List<Country> getItems(Resources resources){

        List<Country> countries = new ArrayList<>();

        for(String country : resources.getStringArray(R.array.countries)){
            countries.add(new Country(country, R.mipmap.ic_launcher));
        }

        return countries;
    }

    public static List<Country> getItemsPage(Resources resources, int page){

        int startIndex = page * PAGE_SIZE;
        int endIndex = (page * PAGE_SIZE) + PAGE_SIZE - 1;
        List<Country> countries = getItems(resources);

        if(startIndex >= countries.size()){
            return new ArrayList<>();
        }

        if(endIndex > countries.size()){
            endIndex = countries.size();
        }

        return countries.subList(startIndex,endIndex);
    }
}
