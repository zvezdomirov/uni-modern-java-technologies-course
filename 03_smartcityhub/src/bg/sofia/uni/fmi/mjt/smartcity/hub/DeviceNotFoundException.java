package bg.sofia.uni.fmi.mjt.smartcity.hub;

import java.io.IOException;

public class DeviceNotFoundException extends Exception {
    public DeviceNotFoundException(String id) {
        super(String.format("There is no device with id %s registered.", id));
    }
}
