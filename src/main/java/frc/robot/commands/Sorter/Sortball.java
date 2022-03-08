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

  private Color matchedColor;
  private double proximity;

  private double hit = 0;
  private double miss = 0;
  private final double sampleRate = 3;

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
    matchedColor = sorter.matchColor();
    sorter.sendDetectedColorToShuffleBoard(matchedColor);
    if (proximity > 225){
      sorter.setLateralConveyorSpeed(0);
    }
    if (proximity > 400) {
      
      if (sorter.isMyAllianceColor(matchedColor)) {
        hit++;
      } else {
        miss++;
      }

      if (hit >= sampleRate) {
        sorter.hits.setNumber(hit);
        sorter.miss.setNumber(miss);
        if (shooter.isUptakeSensorTripped()) {
          sorter.setSortWheelSpeed(0);
        } else {
          sorter.setSortWheelSpeed(-1);
        }
        hit = 0;
        miss = 0;
      } else if (miss >= sampleRate) {
        sorter.hits.setNumber(hit);
        sorter.miss.setNumber(miss);
        sorter.setSortWheelSpeed(1);
        hit = 0;
        miss = 0;
      }
    } else {
      hit = 0;
      miss = 0;
      sorter.setSortWheelSpeed(0);
      if (!intake.isReverseLimitSwitchPressed()) {
        sorter.setLateralConveyorSpeed(.7);
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
