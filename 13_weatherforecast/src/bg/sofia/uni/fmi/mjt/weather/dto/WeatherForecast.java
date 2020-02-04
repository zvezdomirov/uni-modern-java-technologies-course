package bg.sofia.uni.fmi.mjt.weather.dto;

public class WeatherForecast {
    private String latitude;
    private String longitude;
    private String timezone;
    private DataPoint currently;
    private DataBlock hourly;
    private DataBlock daily;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public DataPoint getCurrently() {
        return currently;
    }

    public DataBlock getHourly() {
        return hourly;
    }

    public DataBlock getDaily() {
        return daily;
    }
}
