package bg.sofia.uni.fmi.mjt.smartcity;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartLamp;
import bg.sofia.uni.fmi.mjt.smartcity.hub.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.smartcity.hub.SmartCityHub;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        SmartCityHub hub = new SmartCityHub();
        try {
            SmartLamp myLamp1 = new SmartLamp("my lamp", 2.5, LocalDateTime.now());
            SmartLamp myLamp2 = new SmartLamp("my lamp", 2.5, myLamp1.getInstallationDateTime());
            myLamp2.setId(myLamp1.getId());
            System.out.println("Are lamps equal? -> " + myLamp1.equals(myLamp2));
            hub.register(myLamp1);
            hub.register(myLamp2);

        } catch (DeviceAlreadyRegisteredException e) {
            System.out.println(e.getMessage());
        }

    }
}
