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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  WPI_TalonFX intakeRoller = new WPI_TalonFX(Constants.intakeRoller_CAN_ID);
  CANSparkMax intakeDeployRight = new CANSparkMax(Constants.intakeDeployRight_CAN_ID, MotorType.kBrushless);
  DigitalInput RightreverseLimitSwitch = new DigitalInput(Constants.INTAKE_RIGHT_REVERSE_LIMITSWITCH_DIO_ID);
  DigitalInput LeftreverseLimtiSwitch = new DigitalInput(Constants.INTAKE_LEFT_REVERSE_LIMITSWITCH_DIO_ID);

  
  private NetworkTableEntry ReverseLimitSwitch;
  private NetworkTableEntry IntakePosition;
  /** Creates a new Intake. */
  public Intake() {
    configureShuffleBoard();
    configureSparkMax(intakeDeployRight, true);

    
  }

  private void configureSparkMax(CANSparkMax canSparkMax, boolean inverted) {
    canSparkMax.setIdleMode(IdleMode.kCoast);
    canSparkMax.setInverted(inverted);
    canSparkMax.setSmartCurrentLimit(40);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.PRIMARY_TAB.getLayout("Intake", BuiltInLayouts.kList).withSize(2, 3);
    layout.add("Intake command", this);
    ReverseLimitSwitch = layout.add("RE Limit switch", false).getEntry();
    IntakePosition = layout.add("IntakePosition",0).getEntry();
  }

  public void updateShuffleBoard() {
   ReverseLimitSwitch.setBoolean(isReverseLimitSwitchPressed());
   IntakePosition.setNumber(getIntakeDeployRightPosition());
  }

  public void SetIntakeRollerspeed(double speed) {
    intakeRoller.set(speed);
  }

  public void setIntakeDeploySpeed(double speed) {
    intakeDeployRight.set(speed);
  }

  public double getIntakeDeployRightPosition() {
    return intakeDeployRight.getEncoder().getPosition();
  }
  public boolean isReverseLimitSwitchPressed() {
    return RightreverseLimitSwitch.get() || LeftreverseLimtiSwitch.get();
    //return intakeDeployLeft.getReverseLimitSwitch(Type.kNormallyClosed).isPressed();
  }
  
  
 

  @Override
  public void periodic() {
    if (isReverseLimitSwitchPressed()){
      intakeDeployRight.getEncoder().setPosition(0);
    }
  }
}
