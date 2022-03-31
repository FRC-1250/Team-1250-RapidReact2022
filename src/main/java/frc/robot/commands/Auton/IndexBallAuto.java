// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auton;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

public class IndexBallAuto extends CommandBase {
  private final Sorter sorter;
  private final Shooter shooter;
  private final Intake intake;

  public IndexBallAuto(Sorter m_sorter, Shooter m_shooter, Intake m_intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_sorter, m_shooter);
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
    sorter.setLateralConveyorSpeed(1);

    if (shooter.isUptakeSensorTripped()) {
      shooter.setUptakeConveyorSpeed(0);
    } else {
      shooter.setUptakeConveyorSpeed(0.5);
    }

    if (sorter.getColorSensorProxmity() > 250) {
      sorter.setLateralConveyorSpeed(0);
      if (shooter.isUptakeSensorTripped()) {
        sorter.setSortWheelSpeed(0);
      } else {
        sorter.setSortWheelSpeed(-0.35);
      }
    } else {
      sorter.setSortWheelSpeed(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sorter.setSortWheelSpeed(0);
    sorter.setLateralConveyorSpeed(0);
    shooter.setUptakeConveyorSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (shooter.isUptakeSensorTripped() && sorter.getColorSensorProxmity() > 250)
        || !intake.isIntakeBeyondBumpers();
  }
}
