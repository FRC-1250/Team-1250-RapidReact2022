// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utility.CANDeviceHealth;
import frc.robot.utility.CANDeviceHealth.DeviceHealth;

public class SystemMonitor extends SubsystemBase {

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private final PowerDistribution pdp = new PowerDistribution(Constants.POWER_DISTRIBUTION_BOARD_CAN_ID,
      ModuleType.kRev);
  private NetworkTableEntry channelCurrent;
  private List<WPI_TalonFX> talons = new ArrayList<WPI_TalonFX>();
  private List<CANSparkMax> sparkMaxs = new ArrayList<CANSparkMax>();
  private List<Pigeon2> pigeon2s = new ArrayList<Pigeon2>();
  private TreeMap<Integer, CANDeviceHealth> healthList = new TreeMap<Integer, CANDeviceHealth>();
  private static SystemMonitor instance = new SystemMonitor();

  private Long healthUpdateTimer = 0L;
  private Long LEDUpdateTimer = 0L;
  private int LEDCycle = 1;
  private int LEDCycleLenth = 0;

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
    channelCurrent = Constants.SYSTEM_MONITOR_TAB.add("Climber current", 0).getEntry();
  }

  private void registerDevice(PowerDistribution pdp) {
    healthList.put(pdp.getModule(), new CANDeviceHealth());
  }

  public void registerDevice(Pigeon2 device) {
    pigeon2s.add(device);
    healthList.put(device.getDeviceID(), new CANDeviceHealth());
  }

  public void registerDevice(WPI_TalonFX device) {
    talons.add(device);
    healthList.put(device.getDeviceID(), new CANDeviceHealth());
  }

  public void registerDevice(CANSparkMax device) {
    sparkMaxs.add(device);
    healthList.put(device.getDeviceId(), new CANDeviceHealth());
  }

  private void testPigeonHealth() {
    for (Pigeon2 device : pigeon2s) {
      if (ErrorCode.CAN_MSG_NOT_FOUND == device.getLastError()) {
        healthList.get(device.getDeviceID()).reportDisconnect();
      } else {
        healthList.get(device.getDeviceID()).reportOK();
      }
    }
  }

  private void testTalonHealth() {
    for (WPI_TalonFX device : talons) {
      if (ErrorCode.CAN_MSG_NOT_FOUND == device.getLastError()) {
        healthList.get(device.getDeviceID()).reportDisconnect();
      } else {
        healthList.get(device.getDeviceID()).reportOK();
      }
    }
  }

  private void testSparkHealth() {
    for (CANSparkMax device : sparkMaxs) {
      if (REVLibError.kCANDisconnected == device.getLastError()) {
        healthList.get(device.getDeviceId()).reportDisconnect();
      } else {
        healthList.get(device.getDeviceId()).reportOK();
      }
    }
  }

  private void testPdp() {
    if (pdp.getFaults().CanWarning) {
      healthList.get(pdp.getModule()).reportDisconnect();
    } else {
      healthList.get(pdp.getModule()).reportOK();
    }
  }

  public void updateShuffleBoard() {
    channelCurrent.setNumber(getCurrentByChannel(17));
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

  @Override
  public void periodic() {
    // This should be in its own thread, catch all exceptions so we don't fail due to monitoring
    Long currentTime = System.currentTimeMillis();
    DeviceHealth deviceHealth;

    if (System.currentTimeMillis() > healthUpdateTimer && !healthList.isEmpty()) {
      try {
        testPigeonHealth();
        testSparkHealth();
        testTalonHealth();
        testPdp();
      } catch (Exception e) {
        System.out.println(e.getStackTrace());
      }
      healthUpdateTimer = currentTime + 5000;
    }

    if (System.currentTimeMillis() > LEDUpdateTimer && !healthList.isEmpty()) {
      LEDCycleLenth = healthList.size() / Constants.CANDLE_LED_COUNT;
      try {

        if (LEDCycle == LEDCycleLenth) {
          for (int i = 0 + (LEDCycle * Constants.CANDLE_LED_COUNT); i < healthList.size(); i++) {
            deviceHealth = healthList.get(i).getDeviceHealth();
            switch (deviceHealth) {
              case GREEN:
                setLEDs(0, 255, 0, i, 1);
              case YELLOW:
                setLEDs(255, 255, 0, i, 1);
              case RED:
                setLEDs(255, 0, 0, i, 1);
            }
          }
          LEDCycle = 0;
        } else {
          for (int i = 0 + (LEDCycle * Constants.CANDLE_LED_COUNT); i < Constants.CANDLE_LED_COUNT
              + (LEDCycle * Constants.CANDLE_LED_COUNT); i++) {
            deviceHealth = healthList.get(i).getDeviceHealth();
            switch (deviceHealth) {
              case GREEN:
                setLEDs(0, 255, 0, i, 1);
              case YELLOW:
                setLEDs(255, 255, 0, i, 1);
              case RED:
                setLEDs(255, 0, 0, i, 1);
            }
          }
          LEDCycle++;
        }
      } catch (Exception e) {
        System.out.println(e.getStackTrace());
      }
      LEDUpdateTimer = currentTime + 10000;
    }
  }
}