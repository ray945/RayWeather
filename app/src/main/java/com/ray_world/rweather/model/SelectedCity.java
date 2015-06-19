package com.ray_world.rweather.model;

/**
 * Created by Rui on 2015/6/8 0008.
 */
public class SelectedCity {

    private String cityName;
    private String cityCode;
    private String weather;
    private String temp;

    public SelectedCity(){
        super();
    }

    public SelectedCity(String city, String countyCode) {
        this.cityName = city;
        this.cityCode = countyCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
