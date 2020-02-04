package bg.sofia.uni.fmi.mjt.weather.dto;

public class DataPoint {
    private String time;
    private String summary;
    private String precipProbability;
    private String temperature;

    public String getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getPrecipProbability() {
        return precipProbability;
    }

    public String getTemperature() {
        return temperature;
    }
}
