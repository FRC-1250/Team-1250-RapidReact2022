package frc.robot.utility;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.Constants;

public class DeviceHealth {
    private String deviceFriendlyName;
    private int errorCount;
    private DeviceStatus deviceStatus;
    private NetworkTableEntry deviceHealthNT;
    private ShuffleboardLayout layout;
    private Boolean activeError;
    private Timer activeErrorTime;

    public enum DeviceStatus {
        GREEN,
        YELLOW,
        RED;
    }

    public DeviceHealth(String deviceFriendlyName) {
        this.deviceFriendlyName = deviceFriendlyName;
        errorCount = 0;
        deviceStatus = DeviceStatus.GREEN;
        layout = Constants.SYSTEM_MONITOR_TAB.getLayout("CAN Diagnostics", BuiltInLayouts.kList);
        deviceHealthNT = layout.add(deviceFriendlyName, "").getEntry();
        activeError = false;
        activeErrorTime = new Timer();
    }

    public void reportOK(String error) {
        if (errorCount < 3) {
            deviceStatus = DeviceStatus.GREEN;
            deviceHealthNT.setString(String.format("%s - %s", DeviceStatus.GREEN.toString(), error));
        } else {
            deviceStatus = DeviceStatus.YELLOW;
            deviceHealthNT.setString(String.format("%s - %s", DeviceStatus.YELLOW.toString(), error));
        }
        activeError = false;
        activeErrorTime.stop();
    }

    public void reportError(String error) {
        if (!activeError) {
            activeError = true;
            activeErrorTime.start();
            errorCount++;
            deviceStatus = DeviceStatus.RED;
            deviceHealthNT.setString(String.format("%s - %s", DeviceStatus.RED.toString(), error));
        }
    }

    public DeviceStatus getDeviceHealth() {
        return deviceStatus;
    }

    public String getFriendlyName() {
        return deviceFriendlyName;
    }
}