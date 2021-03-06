// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.utility.RobotHelper;

public class RetractIntake extends CommandBase {
  private final Intake intake;
  private double start;
  private double end;
  private double progress = 0;

  public RetractIntake(Intake m_intake) {
    intake = m_intake;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    start = intake.getIntakePosition();
    end = 0;
  }

  @Override
  public void execute() {
    intake.SetIntakeRollerspeed(0);
    progress = RobotHelper.calculateProgress(start, end, intake.getIntakePosition());

    if (progress > 0.70) {
      intake.setIntakeSpeed(-0.2);
    } else {
      intake.setIntakeSpeed(-0.8);
    }
  }

  @Override
  public void end(boolean interrupted) {
    intake.setIntakeSpeed(0);
  }

  @Override
  public boolean isFinished() {
    return RobotHelper.isDoneTraveling(start, end, intake.getIntakePosition());
  }
}
