// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sorter;

public class Sortball extends CommandBase {
  /** Creates a new Sortball. */
  private final Sorter sorter;

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
    sorter.Setsortercollectspeed(SmartDashboard.getNumber("ConveySpeed", 0));
    if (sorter.m_colorSensor.getProximity() > 250) {
      Color detectedColor = sorter.m_colorSensor.getColor();
      ColorMatchResult match = sorter.m_colorMatcher.matchClosestColor(detectedColor);
      if (match.color == sorter.kBlueTarget) {
        sorter.Setsorterspeed(SmartDashboard.getNumber("SortSpeed", 0));
      } else if (match.color == sorter.kRedTarget) {
        sorter.Setsorterspeed(-SmartDashboard.getNumber("SortSpeed", 0));
      }
    } else {
      sorter.Setsorterspeed(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
