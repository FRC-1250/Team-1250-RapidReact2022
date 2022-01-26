// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sorter extends SubsystemBase {
  CANSparkMax sorter = new CANSparkMax(Constants.sorter_CAN_ID, MotorType.kBrushless);
  CANSparkMax sorterCollect = new CANSparkMax(Constants.sorterCollect_CAN_ID, MotorType.kBrushless);

  /** Creates a new Sorter. */
  public Sorter() {
  }

  public void Setsortercollectspeed(double speed) {
    sorterCollect.set(speed);
  }

  public void Setsorterspeed(double speed) {
    sorter.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
