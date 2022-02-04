// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

  private final WPI_Pigeon2 pigeonGyro = new WPI_Pigeon2(Constants.DRIVETRAIN_PIGEON_CAN_ID);
  private final WPI_TalonFX leftFrontDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_FRONT_LEFT_CAN_ID);
  private final WPI_TalonFX leftBackDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_BACK_LEFT_CAN_ID);
  private final WPI_TalonFX rightFrontDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
  private final WPI_TalonFX rightBackDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_BACK_RIGHT_CAN_ID);
  private final DifferentialDrive drive = new DifferentialDrive(leftFrontDriveMotor, rightFrontDriveMotor);

  public Drivetrain() {
    leftFrontDriveMotor.setInverted(false);
    leftFrontDriveMotor.setNeutralMode(NeutralMode.Brake);
    leftFrontDriveMotor.configOpenloopRamp(0.8);
    leftFrontDriveMotor.configClosedloopRamp(0.8);
    leftBackDriveMotor.follow(leftFrontDriveMotor);

    rightFrontDriveMotor.setInverted(true);
    rightFrontDriveMotor.setNeutralMode(NeutralMode.Brake);
    rightFrontDriveMotor.configOpenloopRamp(0.8);
    rightFrontDriveMotor.configClosedloopRamp(0.8);
    rightBackDriveMotor.follow(rightFrontDriveMotor);
  }

  public void driveTank(double leftSpeed, double rightSpeed) {
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  public void driveArcade(double xSpeed, double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
  }

  public double getEncoderPosition() {
    return (leftFrontDriveMotor.getSelectedSensorPosition() + rightFrontDriveMotor.getSelectedSensorPosition()) / 2.0;
  }

  public void resetEncoders() {
    leftFrontDriveMotor.setSelectedSensorPosition(0);
    rightFrontDriveMotor.setSelectedSensorPosition(0);
  }

  public double getHeading() {
    return pigeonGyro.getAngle();
  }

  public void resetHeading() {
    pigeonGyro.reset();
  }

  @Override
  public void periodic() {
  }
}
