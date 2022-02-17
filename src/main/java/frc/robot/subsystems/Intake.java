// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  WPI_TalonFX intakeRoller = new WPI_TalonFX(Constants.intakeRoller_CAN_ID);
  CANSparkMax intakeDeployRight = new CANSparkMax(Constants.intakeDeployRight_CAN_ID, MotorType.kBrushless);
  CANSparkMax intakeDeployLeft = new CANSparkMax(Constants.intakeDeployLeft_CAN_ID, MotorType.kBrushless);

  private ShuffleboardTab intaketab;
  private NetworkTableEntry FrontRightLimitSwitch;
  private NetworkTableEntry BackRightLimitSwitch;
  private NetworkTableEntry FrontLeftLimitSwitch; 
  private NetworkTableEntry BackLeftLimitSwitch;


  /** Creates a new Intake. */
  public Intake() {
    intakeDeployLeft.setInverted(true);
  }
  public void configureShuffleBoard(){
    intaketab = Shuffleboard.getTab("Intake");
    FrontRightLimitSwitch = intaketab.add("FrontRightLimitSwitch",false).getEntry();
    BackRightLimitSwitch = intaketab.add("BackRIghtLimitSwitch",false).getEntry();
    FrontLeftLimitSwitch = intaketab.add("FrontLeftLimitSwitch",false).getEntry();
    BackLeftLimitSwitch = intaketab.add("BackLeftLimitSwitch", false).getEntry();

  }
  public void SetIntakeRollerspeed(double speed) {
    intakeRoller.set(speed);
  }

  public void setIntakeDeploySpeed(double speed) {
    intakeDeployLeft.set(speed);
    intakeDeployRight.set(speed);
  }

  public double getIntakeDeployLeftPosition() {
    return intakeDeployLeft.getEncoder().getPosition();
  }

  public double getIntakeDeployRightPosition() {
    return intakeDeployRight.getEncoder().getPosition();
  }

  public boolean isLeftForwardLimitSwitchPressed() {
    return intakeDeployLeft.getForwardLimitSwitch(Type.kNormallyOpen).isPressed();
  }

  public boolean isRightForwardLimitSwitchPressed() {
    return intakeDeployRight.getForwardLimitSwitch(Type.kNormallyOpen).isPressed();
  }

  public boolean isLeftReverseLimitSwitchPressed() {
    return intakeDeployLeft.getReverseLimitSwitch(Type.kNormallyOpen).isPressed();
  }

  public boolean isRightReverseLimitSwitchPressed() {
    return intakeDeployRight.getReverseLimitSwitch(Type.kNormallyOpen).isPressed();
  }

  @Override
  public void periodic() {
    FrontRightLimitSwitch.setBoolean(isRightForwardLimitSwitchPressed());
    BackRightLimitSwitch.setBoolean(isRightReverseLimitSwitchPressed());
    FrontLeftLimitSwitch.setBoolean(isLeftForwardLimitSwitchPressed());
    BackLeftLimitSwitch.setBoolean(isLeftReverseLimitSwitchPressed());
    // This method will be called once per scheduler run
  }
}
