// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SystemMonitor extends SubsystemBase {

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private final PowerDistribution pdp = new PowerDistribution(Constants.POWER_DISTRIBUTION_BOARD_CAN_ID,
      ModuleType.kRev);
  private NetworkTableEntry channelCurrent;
  private List<WPI_TalonFX> talons = new ArrayList<WPI_TalonFX>();
  private List<CANSparkMax> sparkMaxs = new ArrayList<CANSparkMax>();
  private static SystemMonitor instance = new SystemMonitor();
  private Long timer = 0L;

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
  }

  public void registerDevice(WPI_TalonFX device) {
    talons.add(device);
  }

  public void registerDevice(CANSparkMax device) {
    sparkMaxs.add(device);
  }

  private void configureShuffleBoard() {
    channelCurrent = Constants.SYSTEM_MONITOR_TAB.add("Climber current", 0).getEntry();
  }

  private void checkHealth() {
    Faults fault = new Faults();
    StickyFaults stickyFaults = new StickyFaults();

    for (WPI_TalonFX wpi_TalonFX : talons) {
      System.out.println(wpi_TalonFX.getDeviceID());
      System.out.println(wpi_TalonFX.getFirmwareVersion());
      System.out.println(wpi_TalonFX.getFaults(fault));
      System.out.println(wpi_TalonFX.getStickyFaults(stickyFaults));
    }

    for (CANSparkMax canSparkMax : sparkMaxs) {
      System.out.println(canSparkMax.getDeviceId());
      System.out.println(canSparkMax.getFirmwareVersion());
      System.out.println(canSparkMax.getFaults());
      System.out.println(canSparkMax.getStickyFaults());
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
    if (System.currentTimeMillis() > timer) {
      checkHealth();
      timer = System.currentTimeMillis() + 10000;
    }
  }
}
