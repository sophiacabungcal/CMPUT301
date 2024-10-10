package com.example.listycity;
/**
 * This is a class that defines a City.
 */
public class City implements Comparable<City> {
    private String city;
    private String province;

    /**
     * This is a constructor that creates a City object.
     * @param city The name of the city.
     * @param province The name of the province.
     */
    City(String city, String province){
        this.city = city;
        this.province = province;
    }

    /**
     * This is a method that returns the name of the city.
     * @return The name of the city.
     */
    String getCityName(){
        return this.city;
    }

    /**
     * This is a method that returns the name of the province.
     * @return The name of the province.
     */
    String getProvinceName(){
        return this.province;
    }

    /**
     * This is a method that compares two cities.
     * @param city The city to compare.
     * @return The result of the comparison.
     */
    @Override
    public int compareTo(City city) {
        return this.city.compareTo(city.getCityName()); // this.city refers to the city name
    }

    /**
     * This is a method that returns a string representation of the city.
     * @return The string representation of the city.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        City city = (City) o;
        return this.city.equals(city.getCityName()) && this.province.equals(city.getProvinceName());
    }

    /**
     * This is a method that returns a hash code for the city.
     * @return The hash code for the city.
     */
    @Override
    public int hashCode() {
        return this.city.hashCode() + this.province.hashCode();
    }
}