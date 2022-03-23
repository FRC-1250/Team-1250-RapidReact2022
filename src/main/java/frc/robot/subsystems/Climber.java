// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  Servo ratchetServo = new Servo(Constants.ratchetServo_PWM_ID);
  WPI_TalonFX climberHook = new WPI_TalonFX(Constants.climberHook_CAN_ID);
  private NetworkTableEntry hookPos;

  public boolean extendClimberHasMoved = false;

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
    hookPos = Constants.CLIMBER_TAB.add("Climber pos", 0).withSize(2, 1).withPosition(2, 0).getEntry();
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
