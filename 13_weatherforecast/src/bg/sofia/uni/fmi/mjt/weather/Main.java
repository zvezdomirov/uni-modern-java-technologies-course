package bg.sofia.uni.fmi.mjt.weather;

import java.io.IOException;
import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String locationIqToken = "8a3e253620aacd";
        final String darkskyKey = "e0ecfa52d6fd1a6f8809fca5b50fe466";
        HttpClient client = HttpClient.newHttpClient();
        WeatherForecastClient weatherForecastClient = new WeatherForecastClient(
                client, darkskyKey, locationIqToken);
        String location = "Stara Zagora";
        System.out.println(weatherForecastClient.getForecast(location)
                .getCurrently().getTemperature());
        System.out.println(weatherForecastClient.getCurrent(location).getSummary());
        System.out.println(weatherForecastClient.getHourlyForecast(location));
    }
}
