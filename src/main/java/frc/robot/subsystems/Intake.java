// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  WPI_TalonFX intakeRoller = new WPI_TalonFX(Constants.intakeRoller_CAN_ID);
  CANSparkMax intake = new CANSparkMax(Constants.intakeDeployRight_CAN_ID, MotorType.kBrushless);
  DigitalInput RightreverseLimitSwitch = new DigitalInput(Constants.INTAKE_RIGHT_REVERSE_LIMITSWITCH_DIO_ID);
  DigitalInput LeftreverseLimtiSwitch = new DigitalInput(Constants.INTAKE_LEFT_REVERSE_LIMITSWITCH_DIO_ID);
  private NetworkTableEntry ReverseLimitSwitch;
  private NetworkTableEntry IntakePosition;

  public Intake() {
    configureShuffleBoard();
    configureSparkMax(intake, true);
  }

  private void configureSparkMax(CANSparkMax canSparkMax, boolean inverted) {
    canSparkMax.setIdleMode(IdleMode.kCoast);
    canSparkMax.setInverted(inverted);
    canSparkMax.setSmartCurrentLimit(40);
  }

  private void configureShuffleBoard() {
    ReverseLimitSwitch = Constants.INTAKE_SORT_TAB.add("Limit switch", false).withSize(2, 1).withPosition(4, 0).getEntry();
    IntakePosition = Constants.INTAKE_SORT_TAB.add("Intake position", 0).withSize(2, 1).withPosition(4, 1).getEntry();
  }

  public void updateShuffleBoard() {
    ReverseLimitSwitch.setBoolean(isReverseLimitSwitchPressed());
    IntakePosition.setNumber(getIntakePosition());
  }

  public void SetIntakeRollerspeed(double speed) {
    intakeRoller.set(speed);
  }

  public void setIntakeSpeed(double speed) {
    intake.set(speed);
  }

  public double getIntakePosition() {
    return intake.getEncoder().getPosition();
  }

  public boolean isIntakeBeyondBumpers() {
    return getIntakePosition() > Constants.INTAKE_PASSED_BUMPER_REVOLUTION_DISTANCE;
  }

  public boolean isReverseLimitSwitchPressed() {
    return RightreverseLimitSwitch.get() || LeftreverseLimtiSwitch.get();
  }

  @Override
  public void periodic() {
    if (isReverseLimitSwitchPressed()) {
      intake.getEncoder().setPosition(0);
    }
  }
}
