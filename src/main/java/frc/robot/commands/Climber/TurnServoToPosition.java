// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class TurnServoToPosition extends CommandBase {
  /** Creates a new TurnToServoToDegree. */

  private final Climber m_climber;
  private final double m_position;

  public TurnServoToPosition(Climber climber, double position) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
    withTimeout(0.2);
    m_climber = climber;
    m_position = position;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.setRatchetPosition(m_position);
  }
}
