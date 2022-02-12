// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

public class Sortball extends CommandBase {
  /** Creates a new Sortball. */
  private final Sorter sorter;
  private final Shooter shooter;
  private Color matchedColor;
  private double proximity;

  public Sortball(Sorter m_sorter, Shooter m_shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_sorter);
    sorter = m_sorter;
    shooter = m_shooter;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    proximity = sorter.getColorSensorProxmity();
    sorter.sendProximityToShuffleBoard(proximity);
    matchedColor = sorter.matchColor();
    sorter.sendDetectedColorToShuffleBoard(matchedColor);
    if (proximity > 250) {
      sorter.Setsortercollectspeed(0);
      if (sorter.isMyAllianceColor(matchedColor) && !shooter.isUptakeSensorTripped()) {
        sorter.Setsorterspeed(-0.6);
      } else if (sorter.isOpposingAllianceColor(matchedColor)) {
        sorter.Setsorterspeed(0.6);
      }
    } else {
      sorter.Setsorterspeed(0);
      sorter.Setsortercollectspeed(1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sorter.Setsorterspeed(0);
    sorter.Setsortercollectspeed(0);
  }
}
