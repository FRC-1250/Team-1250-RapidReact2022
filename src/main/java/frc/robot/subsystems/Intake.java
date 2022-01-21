// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  WPI_TalonFX intakeRoller= new WPI_TalonFX(Constants.intakeRoller_CAN_ID); 
  CANSparkMax intakeDeployRight= new CANSparkMax(Constants.intakeDeployRight_CAN_ID,MotorType.kBrushless);
  CANSparkMax intakeDeployRights = new CANSparkMax(Constants.intakeDeployLeft_CAN_ID,MotorType.kBrushless);

  
  /** Creates a new Intake. */
  public Intake() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
