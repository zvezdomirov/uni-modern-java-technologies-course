package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public class SmartLamp extends BaseSmartDevice {
    private static int lampCount = 0;
    public SmartLamp(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        this.setType(DeviceType.LAMP);
        this.setId();
        lampCount++;
    }

    @Override
    public int getCount() {
        return lampCount;
    }
}
