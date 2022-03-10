// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SystemMonitor extends SubsystemBase {

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private final PowerDistribution pdp = new PowerDistribution(Constants.POWER_DISTRIBUTION_BOARD_CAN_ID,
      ModuleType.kRev);

  public SystemMonitor() {
    CANdleConfiguration configAll = new CANdleConfiguration();
    configAll.disableWhenLOS = false;
    configAll.brightnessScalar = 0.1;
    configAll.vBatOutputMode = VBatOutputMode.Modulated;
    candle.configAllSettings(configAll, 100);
    configureShuffleBoard();
  }

  private void configureShuffleBoard() {
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

  @Override
  public void periodic() {
  }
}
