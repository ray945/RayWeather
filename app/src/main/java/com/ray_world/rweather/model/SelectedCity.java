package com.ray_world.rweather.model;

/**
 * Created by Rui on 2015/6/8 0008.
 */
public class SelectedCity {

    private String cityName;
    private String districtName;
    private String weather;
    private String temp;

    public SelectedCity(){
        super();
    }

    public SelectedCity(String district, String city, String temp) {
        this.districtName = district;
        this.cityName = city;
        this.temp = temp;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
