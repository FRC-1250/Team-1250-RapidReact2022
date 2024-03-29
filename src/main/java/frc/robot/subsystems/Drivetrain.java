// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

  private final WPI_Pigeon2 pigeonGyro = new WPI_Pigeon2(Constants.PIGEON_CAN_ID);
  private final WPI_TalonFX leftFrontDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_FRONT_LEFT_CAN_ID);
  private final WPI_TalonFX leftBackDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_BACK_LEFT_CAN_ID);
  private final WPI_TalonFX rightFrontDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_FRONT_RIGHT_CAN_ID);
  private final WPI_TalonFX rightBackDriveMotor = new WPI_TalonFX(Constants.DRIVETRAIN_BACK_RIGHT_CAN_ID);
  private final DifferentialDrive drive = new DifferentialDrive(leftFrontDriveMotor, rightFrontDriveMotor);

  private NetworkTableEntry rightFrontMotorTemp;
  private NetworkTableEntry rightBackMotorTemp;
  private NetworkTableEntry leftFrontMotorTemp;
  private NetworkTableEntry leftBackMotorTemp;
  private NetworkTableEntry pigeonHeadingNT;
  private NetworkTableEntry leftFrontTicks;
  private NetworkTableEntry rightFrontTicks;

  public Drivetrain() {
    configureDriveMotor(leftFrontDriveMotor, true);
    configureDriveMotor(leftBackDriveMotor, true);
    configureDriveMotor(rightFrontDriveMotor, false);
    configureDriveMotor(rightBackDriveMotor, false);

    rightBackDriveMotor.follow(rightFrontDriveMotor);
    leftBackDriveMotor.follow(leftFrontDriveMotor);

    SystemMonitor.getInstance().registerDevice(leftFrontDriveMotor, "leftFrontDriveMotor");
    SystemMonitor.getInstance().registerDevice(leftBackDriveMotor, "leftBackDriveMotor");
    SystemMonitor.getInstance().registerDevice(rightBackDriveMotor, "rightBackDriveMotor");
    SystemMonitor.getInstance().registerDevice(rightFrontDriveMotor, "rightFrontDriveMotor");
    SystemMonitor.getInstance().registerDevice(pigeonGyro, "pigeonGyro");

    configureShuffleBoard();
  }

  private void configureDriveMotor(WPI_TalonFX talon, boolean inverted) {
    talon.setInverted(inverted);
    talon.setNeutralMode(NeutralMode.Brake);
    talon.configOpenloopRamp(0.8);
    talon.configClosedloopRamp(0.8);
  }

  public void configureBrake() {
    leftFrontDriveMotor.setNeutralMode(NeutralMode.Brake);
    leftBackDriveMotor.setNeutralMode(NeutralMode.Brake);
    rightFrontDriveMotor.setNeutralMode(NeutralMode.Brake);
    rightBackDriveMotor.setNeutralMode(NeutralMode.Brake);
  }

  public void configureCoast() {
    leftFrontDriveMotor.setNeutralMode(NeutralMode.Coast);
    leftBackDriveMotor.setNeutralMode(NeutralMode.Coast);
    rightFrontDriveMotor.setNeutralMode(NeutralMode.Coast);
    rightBackDriveMotor.setNeutralMode(NeutralMode.Coast);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.SYSTEM_MONITOR_TAB.getLayout("Drivetrain", BuiltInLayouts.kList);
    layout.add(this);
    leftFrontMotorTemp = layout.add("FL Motor temp", 0).getEntry();
    rightFrontMotorTemp = layout.add("FR Motor temp", 0).getEntry();
    leftBackMotorTemp = layout.add("BL Motor temp", 0).getEntry();
    rightBackMotorTemp = layout.add("BR Motor temp", 0).getEntry();
    rightFrontTicks = layout.add("FR motor ticks", 0).getEntry();
    leftFrontTicks = layout.add("FL motor ticks", 0).getEntry();
    pigeonHeadingNT = layout.add("Heading", 0).withWidget(BuiltInWidgets.kGyro).getEntry();
  }

  public void updateShuffleBoard() {
    rightFrontMotorTemp.setDouble(getTemperatue(rightFrontDriveMotor));
    rightBackMotorTemp.setDouble(getTemperatue(rightBackDriveMotor));
    leftFrontMotorTemp.setDouble(getTemperatue(leftFrontDriveMotor));
    leftBackMotorTemp.setDouble(getTemperatue(leftBackDriveMotor));
    rightFrontTicks.setDouble(rightFrontDriveMotor.getSelectedSensorPosition());
    leftFrontTicks.setDouble(leftFrontDriveMotor.getSelectedSensorPosition());
    pigeonHeadingNT.setDouble(getHeading());
  }

  public void driveTank(double leftSpeed, double rightSpeed) {
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  public void driveArcade(double xSpeed, double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
  }
  public void driveswerve(double x, double y, double rotation){
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

  public double getTemperatue(WPI_TalonFX talonFX) {
    return talonFX.getTemperature();
  }

  @Override
  public void periodic() {
  }
}
