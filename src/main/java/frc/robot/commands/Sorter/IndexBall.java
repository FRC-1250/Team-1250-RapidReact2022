// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Sorter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

public class IndexBall extends CommandBase {
  /** Creates a new IndexBall. */
  private final Sorter sorter;
  private final Shooter shooter;
  private final Intake intake;

  private double proximity;

  public IndexBall(Sorter m_sorter, Shooter m_shooter, Intake m_intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_sorter);
    sorter = m_sorter;
    shooter = m_shooter;
    intake = m_intake;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    proximity = sorter.getColorSensorProxmity();
    if (proximity > 225) {
      sorter.Setsortercollectspeed(0);
      if (shooter.isUptakeSensorTripped()) {
        sorter.Setsorterspeed(0);
      } else {
        sorter.Setsorterspeed(-1);
      }
    } else {
      sorter.Setsorterspeed(0);
      if (intake.isReverseLimitSwitchPressed()) {
        sorter.Setsortercollectspeed(0);
      } else {
        sorter.Setsortercollectspeed(1);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sorter.Setsorterspeed(0);
    sorter.Setsortercollectspeed(0);
  }
}
