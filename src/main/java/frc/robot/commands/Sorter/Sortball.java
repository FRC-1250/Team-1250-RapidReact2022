// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Sorter;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

public class Sortball extends CommandBase {
  /** Creates a new Sortball. */
  private final Sorter sorter;
  private final Shooter shooter;
  private final Intake intake;
  private boolean sensorTrippedPreviously = false;

  private Color matchedColor;
  private double proximity;

  private double hit = 0;
  private double miss = 0;
  private final double sampleRate = 5;

  public Sortball(Sorter m_sorter, Shooter m_shooter, Intake m_intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    sorter = m_sorter;
    shooter = m_shooter;
    intake = m_intake;
    addRequirements(m_sorter);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    proximity = sorter.getColorSensorProxmity();
    sorter.sendProximityToShuffleBoard(proximity);

    if (proximity > 250) {
      sorter.setLateralConveyorSpeed(0);
      if (sensorTrippedPreviously == false) {
        sensorTrippedPreviously = true;
        sorter.setSortWheelSpeed(0);
      }
    } else {
      sensorTrippedPreviously = false;
      hit = 0;
      miss = 0;
      if (intake.isIntakeBeyondBumpers()) {
        sorter.setLateralConveyorSpeed(1);
        sorter.setSortWheelSpeed(0.4);
      } else {
        sorter.setLateralConveyorSpeed(0);
        sorter.setSortWheelSpeed(0);
      }
    }

    if (proximity > 450) {
      matchedColor = sorter.matchColor();
      sorter.sendDetectedColorToShuffleBoard(matchedColor);
      if (sorter.isMyAllianceColor(matchedColor)) {
        hit++;
        System.out.print(String.format("Hit - R: %s, G: %s, B: %s", matchedColor.red, matchedColor.green, matchedColor.blue));
      } else {
        miss++;
        System.out.print(String.format("Miss - R: %s, G: %s, B: %s", matchedColor.red, matchedColor.green, matchedColor.blue));
      }
      if (hit >= sampleRate) {
        if (shooter.isUptakeSensorTripped()) {
          sorter.setSortWheelSpeed(0);
        } else {
          sorter.setSortWheelSpeed(-1);
        }
        hit = 0;
        miss = 0;
      } else if (miss >= sampleRate) {
        sorter.setSortWheelSpeed(1);
        hit = 0;
        miss = 0;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sorter.setSortWheelSpeed(0);
    sorter.setLateralConveyorSpeed(0);
  }
}
