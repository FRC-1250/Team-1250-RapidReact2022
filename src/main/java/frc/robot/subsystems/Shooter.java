// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  CANSparkMax UptakeConveyor = new CANSparkMax(Constants.uptakeConveyor_CAN_ID, MotorType.kBrushless);
  WPI_TalonFX shooterFront = new WPI_TalonFX(Constants.shooterFront_CAN_ID);
  WPI_TalonFX shooterRear = new WPI_TalonFX(Constants.shooterRear_CAN_ID);
  Servo shooterLeftAngleServo = new Servo(Constants.SHOOTER_ANGLE_LEFT_PWN_ID);
  Servo shooterRightAngleServo = new Servo(Constants.SHOOTER_ANGLE_RIGHT_PWN_ID);
  DigitalInput shooterUptakeConveyorSensor = new DigitalInput(Constants.SHOOTER_UPTAKE_CONVEYOR_SENSOR_DIO_ID);
  // DIO ID changed to 9
  private NetworkTableEntry shooterCurrentRpmNT;
  private NetworkTableEntry shooterTargetRpmNT;
  private NetworkTableEntry uptakeSensorNT;

  public enum ShooterDirection {
    SHOOT_FRONT(0.25),
    SHOOT_FRONT_FENDER(0.35),
    SHOOT_BACK_FEDNER(0.65),
    SHOOT_BACK(0.75);

    public final double servoPosition;

    ShooterDirection(double servoPosition) {
      this.servoPosition = servoPosition;
    }
  }

  public enum ShooterHeight {
    SHOOT_HIGH(21000),
    SHOOT_HIGH_FENDER(18000),
    SHOOT_LOW(9000);

    public final double rpmInTicks;

    ShooterHeight(double rpmInTicks) {
      this.rpmInTicks = rpmInTicks;
    }
  }

  /** Creates a new Shooter. */
  public Shooter() {
    configureTalon(shooterFront, false);
    configureTalon(shooterRear, false);
    shooterFront.follow(shooterRear);
    configureShuffleBoard();
  }

  private void configureTalon(WPI_TalonFX talon, boolean inverted) {
    talon.configPeakOutputReverse(0);
    talon.configOpenloopRamp(0.5);
    talon.configClosedloopRamp(0.5);
    talon.config_kP(0, Constants.shooter_P);
    talon.config_kI(0, Constants.shooter_I);
    talon.config_kD(0, Constants.shooter_D);
    talon.config_kF(0, Constants.shooter_F);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.SYSTEM_MONITOR_TAB.getLayout("Shooter", BuiltInLayouts.kList);
    layout.add(this);
    uptakeSensorNT = layout.add("Uptake sensor", false).getEntry();
    shooterCurrentRpmNT = layout.add("Current shooter RPM", 0).getEntry();
    shooterTargetRpmNT = layout.add("Target shooter RPM", 0).getEntry();
  }

  public void updateShuffleBoard() {
    shooterCurrentRpmNT.setNumber(getFrontShooterRpm());
    uptakeSensorNT.setBoolean(isUptakeSensorTripped());
  }

  public void setShooterServoPosition(double Position) {
    shooterLeftAngleServo.set(Position);
    shooterRightAngleServo.set(Position);
  }

  public double getShooterServoPosition() {
    return shooterRightAngleServo.getPosition();
  }

  public boolean isUptakeSensorTripped() {
    return !shooterUptakeConveyorSensor.get();
  }

  public void setShooterSpeed(double speed) {
    shooterRear.set(speed);
  }

  public void setShooterRpm(double rpm) {
    shooterRear.set(ControlMode.Velocity, rpm);
    shooterTargetRpmNT.setNumber(rpm);
  }

  public void setUptakeConveyorSpeed(double speed) {
    UptakeConveyor.set(speed);
  }

  public double getFrontShooterRpm() {
    return shooterFront.getSelectedSensorVelocity();
  }

  public double getRearShooterRpm() {
    return shooterRear.getSelectedSensorVelocity();
  }

  @Override
  public void periodic() {
  }
}
