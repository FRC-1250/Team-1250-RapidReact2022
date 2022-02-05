// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {

  /**
   * TODO: 
   * 1 servo for climber ratchet - https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/servos.html?highlight=servo#
   * A method to control the servos angle
   * A method to control the servos speed
   * A nethod to get the current servo angle
   * 
   * 1 TalonFX for the climber hook
   * A method to set the speed of the climber talonFX
   * A method to get the current position of the climber talonFX 
   * A method to reset the current position of the climber talonFX
   * A method to detect if the home limit switch is triggered - https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/servos.html?highlight=servo#
   * 
   * Add the gear ratio of the climber to CONSTANTS
   */
  
  /** Creates a new Climber. */
  public Climber() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
