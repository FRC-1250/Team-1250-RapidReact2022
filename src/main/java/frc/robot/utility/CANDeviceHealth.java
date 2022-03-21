package frc.robot.utility;

public class CANDeviceHealth {
    private int disconnects;
    private DeviceHealth deviceHealth;

    public enum DeviceHealth {
        GREEN,
        YELLOW,
        RED;
    }

    public CANDeviceHealth() {
        disconnects = 0;
        deviceHealth = DeviceHealth.GREEN;
    }

    public void reportOK() {
        if (disconnects < 1) {
            deviceHealth = DeviceHealth.GREEN;
        } else {
            deviceHealth = DeviceHealth.YELLOW;
        }
    }

    public void reportDisconnect() {
        disconnects++;
        deviceHealth = DeviceHealth.RED;
    }

    public DeviceHealth getDeviceHealth() {
        return deviceHealth;
    }
}
