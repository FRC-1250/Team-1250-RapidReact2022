// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.SystemMonitor;

public class RetractClimber extends CommandBase {
  private final Climber m_climber;
  private final SystemMonitor m_systemMonitor;
  private double amps;

  public RetractClimber(Climber climber, SystemMonitor systemMonitor) {
    m_climber = climber;
    m_systemMonitor = systemMonitor;
    addRequirements(climber);
  }

  @Override
  public void execute() {
    if(m_climber.extendClimberHasMoved){
    m_climber.setRatchetPosition(0.2);
    amps = m_systemMonitor.getCurrentByChannel(17);
    if (amps > 8) {
      m_climber.setClimberHookSpeed(1);
    } else if (amps <= 8) {
      m_climber.setClimberHookSpeed(0.3);
    }
  }
}

  @Override
  public void end(boolean interrupted) {
    m_climber.setClimberHookSpeed(0);
  }
}
