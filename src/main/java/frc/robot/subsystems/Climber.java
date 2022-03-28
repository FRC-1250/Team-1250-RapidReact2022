// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  Servo ratchetServo = new Servo(Constants.ratchetServo_PWM_ID);
  WPI_TalonFX climberHook = new WPI_TalonFX(Constants.climberHook_CAN_ID);
  private NetworkTableEntry hookPos;

  public boolean extendClimberHasMoved = false;

  public enum ClimbHeight {
    CLIMB_MID_RUNG(-285000),
    CLIMB_LOW_RUNG(-114532);

    public final double heightInTicks;

    ClimbHeight(double heightInTicks) {
      this.heightInTicks = heightInTicks;
    }
  }

  public Climber() {
    configureShuffleBoard();
    SystemMonitor.getInstance().registerDevice(climberHook, "Climber");
    climberHook.setNeutralMode(NeutralMode.Brake);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.SYSTEM_MONITOR_TAB.getLayout("Climber", BuiltInLayouts.kList);
    layout.add(this);
    hookPos = layout.add("Climber pos", 0).getEntry();
  }

  public void updateShuffleBoard() {
    hookPos.setNumber(getClimberHookPosition());
  }

  public void setRatchetPosition(double position) {
    ratchetServo.set(position);
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
