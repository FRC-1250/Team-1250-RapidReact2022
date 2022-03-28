package frc.robot.utility;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.Constants;

public class CANDeviceHealth implements Comparable<CANDeviceHealth> {
    private int canID;
    private String friendlyName;
    private int disconnects;
    private DeviceHealth deviceHealth;
    private NetworkTableEntry deviceHealthNT;
    private ShuffleboardLayout layout;

    public enum DeviceHealth {
        GREEN,
        YELLOW,
        RED;
    }

    public CANDeviceHealth(int canID, String friendlyName) {
        this.canID = canID;
        this.friendlyName = friendlyName;
        disconnects = 0;
        deviceHealth = DeviceHealth.GREEN;
        layout = Constants.SYSTEM_MONITOR_TAB.getLayout("CAN Diagnostics", BuiltInLayouts.kList);
        deviceHealthNT = layout.add(friendlyName, "").getEntry();
    }

    public void reportOK() {
        if (disconnects < 1) {
            deviceHealth = DeviceHealth.GREEN;
            deviceHealthNT.setString(DeviceHealth.GREEN.toString());
        } else {
            deviceHealth = DeviceHealth.YELLOW;
            deviceHealthNT.setString(DeviceHealth.YELLOW.toString());
        }
    }

    public void reportDisconnect() {
        disconnects++;
        deviceHealth = DeviceHealth.RED;
        deviceHealthNT.setString(DeviceHealth.RED.toString());
    }

    public DeviceHealth getDeviceHealth() {
        return deviceHealth;
    }

    public int getCanID() {
        return canID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public int compareTo(CANDeviceHealth o) {
        if (canID == o.canID)
            return 0;
        else if (canID > o.canID)
            return 1;
        else if (canID < o.canID)
            return -1;
        else
            return 0;
    }

}
