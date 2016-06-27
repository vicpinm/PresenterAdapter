package com.vicpin.presenteradapter.sample.model;

import android.content.res.Resources;

import com.vicpin.presenteradapter.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 25/06/2016.
 */
public class CountryRepository {

    public static List<Country> getItems(Resources resources){

        List<Country> countries = new ArrayList<>();

        for(String country : resources.getStringArray(R.array.countries)){
            countries.add(new Country(country, R.mipmap.ic_launcher));
        }

        return countries;
    }
}
