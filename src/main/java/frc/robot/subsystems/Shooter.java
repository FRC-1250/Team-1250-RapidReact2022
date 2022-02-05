// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  /**
   * TODO: 
   * 2 servos (left and right) for shooter position - https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/servos.html?highlight=servo#
   * A method to control the servos angle
   * A method to control the servos speed
   * A nethod to get the current servo angle
   * 
   * A method to run the uptake conveyor based on speed controller inputs
   * A method to run flywheel based on speed controler speed inputs
   * A method to run flywheel based on velocity (RPM) input
   * A method to get flywheel RPM
   * 
   * Add light sensor 
   */

  CANSparkMax UptakeConveyor= new CANSparkMax(Constants.uptakeConveyor_CAN_ID,MotorType.kBrushless);
  WPI_TalonFX shooterTop = new WPI_TalonFX(Constants.shooterTop_CAN_ID);
  WPI_TalonFX shooterBottom = new WPI_TalonFX(Constants.shooterBottom_CAN_ID);

  /** Creates a new Shooter. */
  public Shooter() {
    shooterTop.follow(shooterBottom);
    shooterTop.setInverted(InvertType.OpposeMaster);

    shooterTop.configPeakOutputReverse(0);
    shooterBottom.configPeakOutputReverse(0); 

    shooterBottom.config_kP(0,Constants.shooter_P);
    shooterBottom.config_kI(0,Constants.shooter_I);
    shooterBottom.config_kD(0,Constants.shooter_D);
    shooterBottom.config_kF(0,Constants.shooter_F);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
