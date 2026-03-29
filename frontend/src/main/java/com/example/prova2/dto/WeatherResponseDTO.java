package com.example.prova2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDTO {

    // =========================================================================
    // CAMPI (Proprietà)
    // =========================================================================
    private double latitude;
    private double longitude;
    private double elevation;
    private String timezone;
    private CurrentWeather currentWeather;
    private HourlyWeather hourlyWeather;
    private DailyWeather daily;

    // =========================================================================
    // COSTRUTTORI
    // =========================================================================
    public WeatherResponseDTO() {}

    // =========================================================================
    // GETTER
    // =========================================================================
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getElevation() { return elevation; }
    public String getTimezone() { return timezone; }
    public CurrentWeather getCurrentWeather() { return currentWeather; }
    public HourlyWeather getHourlyWeather() { return hourlyWeather; }
    public DailyWeather getDaily() { return daily; }

    // =========================================================================
    // SETTER
    // =========================================================================
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setElevation(double elevation) { this.elevation = elevation; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public void setCurrentWeather(CurrentWeather currentWeather) { this.currentWeather = currentWeather; }
    public void setHourlyWeather(HourlyWeather hourlyWeather) { this.hourlyWeather = hourlyWeather; }
    public void setDaily(DailyWeather daily) { this.daily = daily; }


    // =========================================================================
    // CLASSI ANNIDATE 
    // =========================================================================

    public static class CurrentWeather {
        private String time;
        private double temperature;
        private double windspeed;
        private int winddirection;
        private int weathercode;

        public CurrentWeather() {}

        public String getTime() { return time; }
        public double getTemperature() { return temperature; }
        public double getWindspeed() { return windspeed; }
        public int getWinddirection() { return winddirection; }
        public int getWeathercode() { return weathercode; }

        public void setTime(String time) { this.time = time; }
        public void setTemperature(double temperature) { this.temperature = temperature; }
        public void setWindspeed(double windspeed) { this.windspeed = windspeed; }
        public void setWinddirection(int winddirection) { this.winddirection = winddirection; }
        public void setWeathercode(int weathercode) { this.weathercode = weathercode; }
    }

    public static class HourlyWeather {
        private List<String> time;
        private List<Double> temperature_2m;
        private List<Double> precipitation;

        public HourlyWeather() {}

        public List<String> getTime() { return time; }
        public List<Double> getTemperature_2m() { return temperature_2m; }
        public List<Double> getPrecipitation() { return precipitation; }

        public void setTime(List<String> time) { this.time = time; }
        public void setTemperature_2m(List<Double> temperature_2m) { this.temperature_2m = temperature_2m; }
        public void setPrecipitation(List<Double> precipitation) { this.precipitation = precipitation; }
    }

    public static class DailyWeather {
        private List<String> time;
        private List<Integer> temperature_2m_max;
        private List<Double> temperature_2m_min;
        private List<Integer> precipitation_sum;

        public DailyWeather() {}

        public List<String> getTime() { return time; }
        public List<Integer> getTemperature_2m_max() { return temperature_2m_max; }
        public List<Double> getTemperature_2m_min() { return temperature_2m_min; }
        public List<Integer> getPrecipitation_sum() { return precipitation_sum; }

        public void setTime(List<String> time) { this.time = time; }
        public void setTemperature_2m_max(List<Integer> temperature_2m_max) { this.temperature_2m_max = temperature_2m_max; }
        public void setTemperature_2m_min(List<Double> temperature_2m_min) { this.temperature_2m_min = temperature_2m_min; }
        public void setPrecipitation_sum(List<Integer> precipitation_sum) { this.precipitation_sum = precipitation_sum; }
    }
}