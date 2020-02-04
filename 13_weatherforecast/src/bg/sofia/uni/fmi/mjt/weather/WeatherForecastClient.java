package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.DataPoint;
import bg.sofia.uni.fmi.mjt.weather.dto.Geocode;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collection;

public class WeatherForecastClient {

    private HttpClient client;
    private Gson gson;
    private String secretKey;
    private String token;

    public WeatherForecastClient(HttpClient client, String secretKey, String token) {
        this.client = client;
        this.gson = new Gson();
        this.secretKey = secretKey;
        this.token = token;
    }

    /**
     * Fetches the weather forecast for the specified location.
     *
     * @return the forecast
     */
    public WeatherForecast getForecast(String location) {
        try {
            // Fetch the GeoCoordinates for current location from LocationIQ's REST API
            HttpRequest locationIqRequest = this.getLocationIqRequest(location);
            HttpResponse<String> locationIqResponse = this.client.send(
                    locationIqRequest, HttpResponse.BodyHandlers.ofString());
            Geocode[] coordinates = this.getGson().fromJson(
                    locationIqResponse.body(), Geocode[].class);

            // Fetch weather forecast from darksky.net's REST API
            HttpRequest darkskyRequest = this.getDarkskyRequest(coordinates[0]);
            HttpResponse<String> darkskyResponse = this.client.send(
                    darkskyRequest, HttpResponse.BodyHandlers.ofString());
            return this.getGson().fromJson(
                    darkskyResponse.body(), WeatherForecast.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong when trying to send the API request");
            e.printStackTrace();
        }
        return null;
    }

    private HttpRequest getLocationIqRequest(String location) {
        final String urlEncodedLocation = location.replaceAll(" ", "%20");
        final String locationIqUri =
                String.format("https://eu1.locationiq.com/v1/search.php?key=%s&q=%s&format=json",
                        this.token,
                        urlEncodedLocation);
        return HttpRequest
                .newBuilder(URI.create(locationIqUri))
                .build();
    }

    private HttpRequest getDarkskyRequest(Geocode coordinates) {
        final String darkskyUri =
                String.format("https://api.darksky.net/forecast/%s/%s,%s",
                        this.secretKey,
                        coordinates.getLat(),
                        coordinates.getLon());
        return HttpRequest.newBuilder(
                URI.create(darkskyUri)).build();
    }

    /**
     * Fetches the current weather for the specified location.
     *
     * @return the current weather
     */
    public DataPoint getCurrent(String location) {
        return this.getForecast(location)
                .getCurrently();
    }

    /**
     * Fetches the hourly weather forecast
     *
     * @return the hourly weather forecast
     */
    public Collection<DataPoint> getHourlyForecast(String location) {
        return Arrays.asList(this.getForecast(location)
                .getHourly()
                .getData());
    }

    /**
     * Fetches the weekly weather forecast
     *
     * @return the weekly weather forecast
     */
    public Collection<DataPoint> getWeeklyForecast(String location) {
        return Arrays.asList(this.getForecast(location)
                .getDaily()
                .getData());
    }

    public Gson getGson() {
        return gson;
    }
}