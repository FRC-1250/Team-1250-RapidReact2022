// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

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
  private NetworkTableEntry hookCurrentDraw;
  private NetworkTableEntry hookPos;

  public enum ClimbHeight {
    CLIMB_MID_RUNG(-189439),
    CLIMB_LOW_RUNG(-72141);

    public final double heightInTicks;

    ClimbHeight(double heightInTicks) {
      this.heightInTicks = heightInTicks;
    }
  }

  public Climber() {
    configureShuffleBoard();
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.PRIMARY_TAB.getLayout("Climber", BuiltInLayouts.kList).withSize(2, 3);
    layout.add("Climber command", this);
    hookCurrentDraw = layout.add("Climber hook current", 0).getEntry();
    hookPos = layout.add("Climber hook pos", 0).getEntry();
  }

  public void updateShuffleBoard() {
    hookCurrentDraw.setNumber(getClimberHookCurrent());
    hookPos.setNumber(getClimberHookPosition());
  }

  public void setRatchetPosition(double position) {
    ratchetServo.set(position);
  }

  public void setClimberHookSpeed(double speed) {
    climberHook.set(speed);
  }

  public double getClimberHookCurrent() {
    return climberHook.getOutputCurrent();
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
