package bg.sofia.uni.fmi.mjt.smartcity.device;

import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseSmartDevice implements SmartDevice {
    private String id;
    private String name;
    private double powerConsumption;
    private LocalDateTime installationDateTime;
    private DeviceType type;

    public BaseSmartDevice(String name,
                           double powerConsumption,
                           LocalDateTime installationDateTime) {
        this.name = name;
        this.powerConsumption = powerConsumption;
        this.installationDateTime = installationDateTime;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    void setId() {
        this.id = String.format(
                "%s-%s-%d",
                this.type.getShortName(),
                this.getName(),
                this.getCount());
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalPowerConsumption() {
        return this.powerConsumption *
                Duration.between(this.installationDateTime, LocalDateTime.now()).toHours();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public DeviceType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseSmartDevice that = (BaseSmartDevice) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
