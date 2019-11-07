package bg.sofia.uni.fmi.mjt.smartcity.hub;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmartCityHub {
    public static final String NULL_DEVICE_MESSAGE = "Device should not be null.";
    private Map<String, SmartDevice> hub;

    public SmartCityHub() {
        this.hub = new LinkedHashMap<>();
    }

    /**
     * Adds a @device to the SmartCityHub.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already registered.
     */
    public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException(NULL_DEVICE_MESSAGE);
        }
        String deviceId = device.getId();
        if (this.hub.containsKey(deviceId)) {
            throw new DeviceAlreadyRegisteredException(
                    String.format(
                            "You have already registered device with id %s",
                            deviceId));
        }
        this.hub.put(deviceId, device);
    }

    /**
     * Removes the @device from the SmartCityHub.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(SmartDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException(NULL_DEVICE_MESSAGE);
        }
        String deviceId = device.getId();
        if (!this.hub.containsKey(deviceId)) {
            throw new DeviceNotFoundException(deviceId);
        }
        this.hub.remove(deviceId);
    }

    /**
     * Returns a SmartDevice with an ID @id.
     *
     * @throws IllegalArgumentException in case @id is null.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        if (!this.hub.containsKey(id)) {
            throw new DeviceNotFoundException(id);
        }
        return this.hub.get(id);
    }

    /**
     * Returns the total number of devices with type @type registered in SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if (type == null) {
            throw new IllegalArgumentException("DeviceType should not be null.");
        }
        return (int) this.hub.values().stream()
                .map(SmartDevice::getType)
                .filter(deviceType -> deviceType.equals(type))
                .count();
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     * <p>
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s multiplied by the stated power consumption of the device.
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        ensureNotNegativeNumber(n);
        return this.hub.values().stream()
                .sorted((a, b) -> (int) (a.getTotalPowerConsumption() - b.getPowerConsumption()))
                .map(SmartDevice::getId)
                .limit(n)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the SmartCityHub (registration != installation).
     * <p>
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
        ensureNotNegativeNumber(n);
        int lastElementIndex = n;
        int hubSize = this.hub.size();
        if (n >= hubSize) {
            lastElementIndex = hubSize;
        }
        return new ArrayList<>(
                this.hub.values())
                .subList(0, lastElementIndex);
    }

    private void ensureNotNegativeNumber(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n should not be negative.");
        }
    }
}
