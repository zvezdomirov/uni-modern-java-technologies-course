package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public class SmartTrafficLight extends BaseSmartDevice{
    private static int lightCount = 0;
    public SmartTrafficLight(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        this.setType(DeviceType.TRAFFIC_LIGHT);
        this.setId();
        lightCount++;
    }

    @Override
    public int getCount() {
        return lightCount;
    }
}
