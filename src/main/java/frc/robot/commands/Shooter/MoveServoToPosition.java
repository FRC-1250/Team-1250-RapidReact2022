// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class MoveServoToPosition extends CommandBase {
  private final Shooter shooter;
  private final double position;

  /** Creates a new ServoPosition. */
  public MoveServoToPosition(Shooter m_shooter, double m_position) {
    shooter = m_shooter;
    position = m_position;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setShooterServoPosition(position);
  }

}
