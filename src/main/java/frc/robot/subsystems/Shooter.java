// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  /**
   * A method to run the uptake conveyor based on speed controller inputs
   * A method to run flywheel based on speed controler speed inputs
   * A method to run flywheel based on velocity (RPM) input
   * A method to get flywheel RPM
   */

  CANSparkMax UptakeConveyor = new CANSparkMax(Constants.uptakeConveyor_CAN_ID, MotorType.kBrushless);
  WPI_TalonFX shooterFront = new WPI_TalonFX(Constants.shooterFront_CAN_ID);
  WPI_TalonFX shooterRear = new WPI_TalonFX(Constants.shooterRear_CAN_ID);
  Servo shooterLeftAngleServo = new Servo(Constants.SHOOTER_ANGLE_LEFT_PWN_ID);
  Servo shooterRightAngleServo = new Servo(Constants.SHOOTER_ANGLE_RIGHT_PWN_ID);
  DigitalInput shooterUptakeConveyorSensor = new DigitalInput(Constants.SHOOTER_UPTAKE_CONVEYOR_SENSOR_DIO_ID);

  /** Creates a new Shooter. */
  public Shooter() {
    shooterFront.follow(shooterRear);
    shooterRear.configPeakOutputReverse(0);
    shooterRear.config_kP(0, Constants.shooter_P);
    shooterRear.config_kI(0, Constants.shooter_I);
    shooterRear.config_kD(0, Constants.shooter_D);
    shooterRear.config_kF(0, Constants.shooter_F);
  }

  public void setShooterServoPosition(double Position) {
    shooterLeftAngleServo.set(Position);
    shooterRightAngleServo.set(Position);
  }

  

  public double getShooterServoPosition() {
    return shooterRightAngleServo.getPosition();
  }

  public boolean isUptakeSensorTripped() {
    return shooterUptakeConveyorSensor.get();
  }

  public void setShooterSpeed(double speed){
    shooterFront.set(speed);
    shooterRear.set(speed);
  }
  public void setUptakeConveyorSpeed(double speed){
    UptakeConveyor.set(speed);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
