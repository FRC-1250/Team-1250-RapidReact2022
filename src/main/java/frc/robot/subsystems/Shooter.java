// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
  CANSparkMax UptakeConveyor= new CANSparkMax(Constants.uptakeConveyor_CAN_ID,MotorType.kBrushless);
  WPI_TalonFX shooterTop = new WPI_TalonFX(Constants.shooterTop_CAN_ID);
  WPI_TalonFX shooterBottom = new WPI_TalonFX(Constants.shooterBottom_CAN_ID);

  /** Creates a new Shooter. */
  public Shooter() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
