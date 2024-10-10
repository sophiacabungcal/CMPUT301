package com.example.listycity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a class that keeps a list of city objects
 */
public class CityList {
    private List<City> cities = new ArrayList<>();

    /**
     * This adds a city to the list if the city does not exist
     * @param city
     * This is a candidate city to add
     */
    public void add(City city) {
        if (cities.contains(city)) {
            throw new IllegalArgumentException();
        }
        cities.add(city);
    }

    /**
     * This returns a sorted list of cities
     * @return
     * Return the sorted list
     */
    public List<City> getCities() {
        List<City> list = cities;
        Collections.sort(list);
        return list;
    }

    /**
     * This checks if the city exists in the list
     * @param city
     * This is a candidate city to check
     * @return
     * Return true if the city exists in the list
     */
    public boolean hasCity(City city) {
        for (City c : cities) {
            if (c.equals(city)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This deletes a city from the list
     * @param city
     * This is a candidate city to delete
     */
    public void deleteCity(City city) {
        if (!cities.contains(city)) {
            throw new IllegalArgumentException();
        }

        cities.remove(city);
    }

    /**
     * This returns the number of cities in the list
     * @return
     * Return the number of cities in the list
     */
    public int countCities() {
        return cities.size();
    }
}
