package com.example.listycity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CityListTest {
    private CityList mockCityList() {
        CityList cityList = new CityList();
        cityList.add(mockCity());
        return cityList;
    }

    private City mockCity() {
        return new City("Edmonton", "Alberta");
    }

    @Test
    void testAdd() {
        CityList cityList = mockCityList();
        assertEquals(1, cityList.getCities().size());
        City city = new City("Regina", "Saskatchewan");
        cityList.add(city);
        assertEquals(2, cityList.getCities().size());
        assertTrue(cityList.getCities().contains(city));
    }

    @Test
    void testAddException() {
        CityList cityList = mockCityList();
        City city = new City("Yellowknife", "Northwest Territories");
        cityList.add(city);
        assertThrows(IllegalArgumentException.class, () -> {
            cityList.add(city);
        });
    }

    @Test
    void testGetCities() {
        CityList cityList = mockCityList();
        // This line checks if the first city in the cityList (retrieved by cityList.getCities().get(0))
        // is the same as the city returned by mockCity()
        assertEquals(0, mockCity().compareTo(cityList.getCities().get(0)));

        // This pushes down the original city
        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);

        // Now the original city should be at position 1
        assertEquals(0, city.compareTo(cityList.getCities().get(0)));
        assertEquals(0, mockCity().compareTo(cityList.getCities().get(1)));
    }

    @Test
    void testHasCity() {
        CityList cityList = mockCityList();
        assertTrue(cityList.hasCity(mockCity()));

        City city = new City("Fredericton", "New Brunswick");
        cityList.add(city);
        assertTrue(cityList.hasCity(city));
        cityList.deleteCity(city);
        assertFalse(cityList.hasCity(city));

        City city3 = new City("Fredericton", "New Brunswick");
        cityList.add(city3);
        assertTrue(cityList.hasCity(city));

        City city2 = new City("Halifax", "Nova Scotia");
        assertFalse(cityList.hasCity(city2));
    }

    @Test
    void testDeleteCity() {
        CityList cityList = mockCityList();
        City city = new City("St. John's", "Newfoundland and Labrador");
        assertThrows(IllegalArgumentException.class, () -> {
            cityList.deleteCity(city);
        });

        cityList.add(city);
        assertTrue(cityList.hasCity(city));
        assertEquals(2, cityList.getCities().size());

        cityList.deleteCity(city);
        assertFalse(cityList.hasCity(city));
        assertEquals(1, cityList.getCities().size());
    }

    @Test
    void testCountCities() {
        CityList cityList = mockCityList();
        assertEquals(1, cityList.countCities());

        City city = new City("Whitehorse", "Yukon");
        cityList.add(city);
        assertEquals(2, cityList.countCities());
    }
}