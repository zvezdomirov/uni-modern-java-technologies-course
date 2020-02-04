package bg.sofia.uni.fmi.mjt.weather;

import java.io.IOException;
import org.junit.Test;

public class WeatherForecastClientTest {
    /* There is some problem and I can't mock HttpClient:
     * java.lang.reflect.InaccessibleObjectException: Unable to make
     * protected java.net.http.HttpClient() accessible: module java.net.http
     * does not "opens java.net.http" to unnamed module @5a61f5df
     */
//    @Mock
//    private HttpClient mockClient;
//    @Mock
//    private Gson mockGson;
//    @InjectMocks
//    private WeatherForecastClient weatherForecast;
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//        this.weatherForecast = new WeatherForecastClient(
//                this.mockClient,
//                "",
//                "");
//    }

    @Test
    public void getForecastShouldWorkCorrectly() throws IOException, InterruptedException, NoSuchMethodException {
//        Geocode[] coordinates = new Geocode[1];
//        final String lat = "1";
//        final String lon = "2";
//        coordinates[0] = new Geocode(lat, lon);
//        when(this.mockClient.send(any(), any()))
//                .thenReturn(any());
//        when(this.weatherForecast.getGson())
//                .thenReturn(this.mockGson);
//        when(this.mockGson.fromJson(anyString(), Geocode[].class))
//                .thenReturn(coordinates);
//        when(this.mockGson.fromJson(anyString(), WeatherForecast.class))
//                .thenReturn(new WeatherForecast());
    }
}
