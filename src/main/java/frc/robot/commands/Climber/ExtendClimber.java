// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

public class ExtendClimber extends CommandBase {
  private final Climber m_climber;
  private long unjamServoTimer = 0;

  public ExtendClimber(Climber climber) {
    m_climber = climber;
    addRequirements(climber);
  }

  @Override
  public void initialize() {
    unjamServoTimer = System.currentTimeMillis() + 100;
  }

  @Override
  public void execute() {
    m_climber.setRatchetPosition(0.75);
    if (unjamServoTimer > System.currentTimeMillis()) {
      m_climber.setClimberHookSpeed(0.1);
    } else {
      m_climber.setClimberHookSpeed(-0.5);
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.setClimberHookSpeed(0);
  }
}
