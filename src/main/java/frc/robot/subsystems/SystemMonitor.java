// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utility.CANDeviceHealth;
import frc.robot.utility.CANDeviceHealth.DeviceHealth;

public class SystemMonitor extends SubsystemBase {

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private final PowerDistribution pdp = new PowerDistribution(Constants.POWER_DISTRIBUTION_BOARD_CAN_ID,
      ModuleType.kRev);
  private List<WPI_TalonFX> talons = new ArrayList<WPI_TalonFX>();
  private List<CANSparkMax> sparkMaxs = new ArrayList<CANSparkMax>();
  private List<Pigeon2> pigeon2s = new ArrayList<Pigeon2>();
  private List<CANDeviceHealth> healthList = new ArrayList<CANDeviceHealth>();
  private static SystemMonitor instance = new SystemMonitor();
  private Long healthUpdateTimer = 0L;

  public static SystemMonitor getInstance() {
    return instance;
  }

  private SystemMonitor() {
    CANdleConfiguration configAll = new CANdleConfiguration();
    configAll.disableWhenLOS = false;
    configAll.brightnessScalar = 0.1;
    configAll.vBatOutputMode = VBatOutputMode.Modulated;
    candle.configAllSettings(configAll, 100);
    configureShuffleBoard();
    registerDevice(pdp);
  }

  private void configureShuffleBoard() {
  }

  private void registerDevice(PowerDistribution pdp) {
    healthList.add(new CANDeviceHealth(pdp.getModule(), "pdp"));
    Collections.sort(healthList);
  }

  public void registerDevice(Pigeon2 device, String friendlyName) {
    pigeon2s.add(device);
    healthList.add(new CANDeviceHealth(device.getDeviceID(), friendlyName));
    Collections.sort(healthList);
  }

  public void registerDevice(WPI_TalonFX device, String friendlyName) {
    talons.add(device);
    healthList.add(new CANDeviceHealth(device.getDeviceID(), friendlyName));
    Collections.sort(healthList);
  }

  public void registerDevice(CANSparkMax device, String friendlyName) {
    sparkMaxs.add(device);
    healthList.add(new CANDeviceHealth(device.getDeviceId(), friendlyName));
    Collections.sort(healthList);
  }

  private void testPigeonHealth() {
    for (Pigeon2 device : pigeon2s) {
      for (int i = 0; i < healthList.size(); i++) {
        if (device.getDeviceID() == healthList.get(i).getCanID()) {
          ErrorCode ec = device.getLastError();
          if (ec.value < 0 && ec.value > -5) {
            healthList.get(i).reportDisconnect(ec.toString());
          } else {
            healthList.get(i).reportOK(ec.toString());
          }
        }
      }
    }
  }

  private void testTalonHealth() {
    for (WPI_TalonFX device : talons) {
      for (int i = 0; i < healthList.size(); i++) {
        if (device.getDeviceID() == healthList.get(i).getCanID()) {
          ErrorCode ec = device.getLastError();
          if (ec.value < 0 && ec.value > -5) {
            healthList.get(i).reportDisconnect(ec.toString());
          } else {
            healthList.get(i).reportOK(ec.toString());
          }
        }
      }
    }
  }

  private void testSparkHealth() {
    for (CANSparkMax device : sparkMaxs) {
      for (int i = 0; i < healthList.size(); i++) {
        if (device.getDeviceId() == healthList.get(i).getCanID()) {
          REVLibError ec = device.getLastError();
          if (REVLibError.kCANDisconnected == ec || REVLibError.kHALError == ec) {
            healthList.get(i).reportDisconnect(ec.toString());
          } else {
            healthList.get(i).reportOK(ec.toString());
          }
        }
      }
    }
  }

  private void testPdp() {
    for (int i = 0; i < healthList.size(); i++) {
      if (pdp.getModule() == healthList.get(i).getCanID()) {
        if (pdp.getFaults().CanWarning) {
          healthList.get(i).reportDisconnect("CAN warning");
        } else {
          healthList.get(i).reportOK("OK");
        }
      }
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

  public void setLEDs(int r, int g, int b) {
    candle.setLEDs(r, g, b);
  }

  public void setLEDs(int r, int g, int b, int startIdx, int count) {
    candle.setLEDs(r, g, b, 0, startIdx, count);
  }

  public DeviceHealth checkDevices() {
    for (CANDeviceHealth canDeviceHealth : healthList) {
      if (canDeviceHealth.getDeviceHealth() == DeviceHealth.RED) {
        return DeviceHealth.RED;
      } else if (canDeviceHealth.getDeviceHealth() == DeviceHealth.YELLOW) {
        return DeviceHealth.YELLOW;
      }
    }
    return DeviceHealth.GREEN;
  }

  @Override
  public void periodic() {
    // This should be in its own thread, catch all exceptions so we don't fail due
    // to monitoring
    Long currentTime = System.currentTimeMillis();
    if (System.currentTimeMillis() > healthUpdateTimer && !healthList.isEmpty()) {
      try {
        testPigeonHealth();
        testSparkHealth();
        testTalonHealth();
        testPdp();
        switch (checkDevices()) {
          case RED:
            setLEDs(255, 0, 0);
            break;
          case YELLOW:
            setLEDs(255, 255, 0);
            break;
          case GREEN:
            setLEDs(0, 255, 0);
            break;
        }
      } catch (Exception e) {
        System.out.println(e.getStackTrace());
      }
      healthUpdateTimer = currentTime + 5000;
    }
  }
}