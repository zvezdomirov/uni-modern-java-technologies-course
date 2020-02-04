package bg.sofia.uni.fmi.mjt.weather.dto;

public class DataBlock {
    private DataPoint[] data;
    private String summary;

    public DataPoint[] getData() {
        return data;
    }

    public String getSummary() {
        return summary;
    }
}
