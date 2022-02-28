// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class RetractClimber extends CommandBase {
  private final Climber m_climber;

  public RetractClimber(Climber climber) {
    m_climber = climber;
    addRequirements(climber);
  }

  @Override
  public void execute() {
    m_climber.setRatchetPosition(0.2);
    m_climber.setClimberHookSpeed(1);
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.setClimberHookSpeed(0);
  }
}
