package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.LocalDateTime;

public class SmartCamera extends BaseSmartDevice {
    private static int cameraCount = 0;
    public SmartCamera(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(name, powerConsumption, installationDateTime);
        this.setType(DeviceType.CAMERA);
        this.setId();
        cameraCount++;
    }

    @Override
    public int getCount() {
        return cameraCount;
    }
}
