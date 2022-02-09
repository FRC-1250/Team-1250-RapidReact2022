// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sorter;

public class Sortball extends CommandBase {
  /** Creates a new Sortball. */
  private final Sorter sorter;
  private Color detectedColor;
  private ColorMatchResult matchedColor;

  public Sortball(Sorter m_sorter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_sorter);
    sorter = m_sorter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (sorter.m_colorSensor.getProximity() > 250) {
      detectedColor = sorter.m_colorSensor.getColor();
      matchedColor = sorter.m_colorMatcher.matchColor(detectedColor);
      if (matchedColor != null) {
        if ((matchedColor.color == sorter.kBlueTarget && Alliance.Blue == sorter.alliance)
            || (matchedColor.color == sorter.kRedTarget && Alliance.Red == sorter.alliance)) {
          sorter.Setsorterspeed(-sorter.sortSpeedNT.getDouble(0));
        } else if ((matchedColor.color == sorter.kRedTarget && Alliance.Blue == sorter.alliance)
            || (matchedColor.color == sorter.kBlueTarget && Alliance.Red == sorter.alliance)) {
          sorter.Setsorterspeed(sorter.sortSpeedNT.getDouble(0));
        }
      }
      sorter.Setsortercollectspeed(0);

    } else {
      sorter.Setsorterspeed(0);
      sorter.Setsortercollectspeed(sorter.conveySpeedNT.getDouble(0));
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sorter.Setsorterspeed(0);
    sorter.Setsortercollectspeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
