// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  Servo ratchetServo = new Servo(Constants.ratchetServo_PWM_ID);
  WPI_TalonFX climberHook = new WPI_TalonFX(Constants.climberHook_CAN_ID);

  public Climber() {
  }

  public void ratchetSpeed(double speed) {
    ratchetServo.set(speed);
  }

  public void ratchetAngle(double angle) {
    ratchetServo.setAngle(angle);
  }

  public double getRatchetServoAngle() {
    return ratchetServo.getAngle();
  }

  public void setClimberHookSpeed(double speed) {
    climberHook.set(speed);
  }

  public void resetClimberHookPositon() {
    climberHook.setSelectedSensorPosition(0);
  }

  public double getClimberHookPosition() {
    return climberHook.getSelectedSensorPosition();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
