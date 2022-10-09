// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utility.DeviceHealth;
import frc.robot.utility.DeviceHealth.DeviceStatus;

public class SystemMonitor extends SubsystemBase {

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private final PowerDistribution pdp = new PowerDistribution(Constants.POWER_DISTRIBUTION_BOARD_CAN_ID,
      ModuleType.kRev);
  private List<WPI_TalonFX> registeredTalonFX = new ArrayList<WPI_TalonFX>();
  private List<CANSparkMax> registedSparkMax = new ArrayList<CANSparkMax>();
  private List<Pigeon2> registeredPigeon = new ArrayList<Pigeon2>();
  private TreeMap<Integer, DeviceHealth> deviceHealthMap = new TreeMap<Integer, DeviceHealth>();
  private static SystemMonitor instance = new SystemMonitor();
  private Long healthUpdateTimer = 0L;
  private Long currentTime = 0L;

  public static SystemMonitor getInstance() {
    return instance;
  }

  private SystemMonitor() {
    candle.configLOSBehavior(true);
    candle.configStatusLedState(true);
    configureShuffleBoard();
    registerDevice(pdp, "pdp");
  }

  private void configureShuffleBoard() {
  }

  private void registerDevice(PowerDistribution pdp, String friendlyName) {
    deviceHealthMap.put(pdp.getModule(), new DeviceHealth("pdp"));
  }

  public void registerDevice(Pigeon2 device, String friendlyName) {
    registeredPigeon.add(device);
    deviceHealthMap.put(device.getDeviceID(), new DeviceHealth(friendlyName));
  }

  public void registerDevice(WPI_TalonFX device, String friendlyName) {
    registeredTalonFX.add(device);
    deviceHealthMap.put(device.getDeviceID(), new DeviceHealth(friendlyName));

  }

  public void registerDevice(CANSparkMax device, String friendlyName) {
    registedSparkMax.add(device);
    deviceHealthMap.put(device.getDeviceId(), new DeviceHealth(friendlyName));
  }

  private void testPigeonHealth() {
    ErrorCode ec;
    for (Pigeon2 device : registeredPigeon) {
      ec = device.getLastError();
      if (ec.value < 0 && ec.value > -5) {
        deviceHealthMap.get(device.getDeviceID()).reportError(ec.toString());
      } else {
        deviceHealthMap.get(device.getDeviceID()).reportOK(ec.toString());
      }
    }
  }

  private void testTalonHealth() {
    ErrorCode ec;
    for (WPI_TalonFX device : registeredTalonFX) {
      ec = device.getLastError();
      if (ec.value < 0 && ec.value > -5) {
        deviceHealthMap.get(device.getDeviceID()).reportError(ec.toString());
      } else {
        deviceHealthMap.get(device.getDeviceID()).reportOK(ec.toString());
      }
    }
  }

  private void testSparkHealth() {
    REVLibError ec;
    for (CANSparkMax device : registedSparkMax) {
      ec = device.getLastError();
      if (REVLibError.kCANDisconnected == ec || REVLibError.kHALError == ec) {
        deviceHealthMap.get(device.getDeviceId()).reportError(ec.toString());
      } else {
        deviceHealthMap.get(device.getDeviceId()).reportOK(ec.toString());
      }
    }
  }

  private void testPdp() {
    if (pdp.getFaults().CanWarning) {
      deviceHealthMap.get(pdp.getModule()).reportError("CAN warning");
    } else {
      deviceHealthMap.get(pdp.getModule()).reportOK("OK");
    }
  }

  public void updateShuffleBoard() {
  }

  public double getCurrentByChannel(int channel) {
    return pdp.getCurrent(channel);
  }

  public double getTotalCurrent() {
    return pdp.getTotalCurrent();
  }

  public DeviceStatus checkDevices() {
    DeviceStatus deviceHealth;
    for (Integer devicdeId : deviceHealthMap.keySet()) {
      deviceHealth = deviceHealthMap.get(devicdeId).getDeviceHealth();
      if (deviceHealth == DeviceStatus.RED) {
        return DeviceStatus.RED;
      } else if (deviceHealth == DeviceStatus.YELLOW) {
        return DeviceStatus.YELLOW;
      }
    }
    return DeviceStatus.GREEN;
  }

  @Override
  public void periodic() {
    currentTime = System.currentTimeMillis();
    if (currentTime > healthUpdateTimer && !deviceHealthMap.isEmpty()) {
      try {
        testPigeonHealth();
        testSparkHealth();
        testTalonHealth();
        testPdp();
        switch (checkDevices()) {
          case RED:
            candle.setLEDs(255, 0, 0);
            break;
          case YELLOW:
            candle.setLEDs(255, 255, 0);
            break;
          case GREEN:
            candle.setLEDs(0, 255, 0);
            break;
        }
      } catch (Exception e) {
        System.out.println(e.getStackTrace());
      }
      healthUpdateTimer = currentTime + 5000;
    }
  }
}