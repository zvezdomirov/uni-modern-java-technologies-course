package bg.sofia.uni.fmi.mjt.weather.dto;

public class Geocode {
    private String lat;
    private String lon;

    public Geocode() {
    }

    public Geocode(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
