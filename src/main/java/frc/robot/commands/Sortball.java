// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Sorter;

public class Sortball extends CommandBase {
  /** Creates a new Sortball. */
  private final Sorter sorter;
  private long time;
  private Color detectedColor;
  private ColorMatchResult matchedColor;

  public Sortball(Sorter m_sorter) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_sorter);
    sorter = m_sorter;
    time = 0;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    sorter.Setsortercollectspeed(sorter.conveySpeed.getDouble(0));
    if (time < System.currentTimeMillis()) {
      if (sorter.m_colorSensor.getProximity() > 250) {
        detectedColor = sorter.m_colorSensor.getColor();
        matchedColor = sorter.m_colorMatcher.matchClosestColor(detectedColor);
        if (matchedColor.color == sorter.kBlueTarget) {
          sorter.Setsorterspeed(sorter.sortSpeed.getDouble(0));
        } else if (matchedColor.color == sorter.kRedTarget) {
          sorter.Setsorterspeed(-sorter.sortSpeed.getDouble(0));
        }
        time = System.currentTimeMillis()
            + Double.valueOf(sorter.motorSwitchDelayInMs.getDouble(0)).longValue();
      } else {
        sorter.Setsorterspeed(0);
      }
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
