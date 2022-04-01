// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.utility.RobotHelper;

public class ExtendIntake extends CommandBase {
  private final Intake intake;
  private final double intakeRollerSpeed;
  private double start;
  private double end;
  private double progress = 0;
  private Timer timer = new Timer();

  public ExtendIntake(Intake m_intake) {
    intake = m_intake;
    intakeRollerSpeed = 1;
    addRequirements(m_intake);
  }

  public ExtendIntake(Intake m_intake, double m_intakeRollerSpeed) {
    intake = m_intake;
    intakeRollerSpeed = m_intakeRollerSpeed;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    start = intake.getIntakePosition();
    end = 13;
    timer.reset();
    timer.start();
  }

  @Override
  public void execute() {
    progress = RobotHelper.calculateProgress(start, end, intake.getIntakePosition());

    if (progress > 0.70) {
      intake.setIntakeSpeed(0.2);
    } else {
      intake.setIntakeSpeed(0.8);
    }

    if (intake.isIntakeBeyondFrame()) {
      intake.SetIntakeRollerspeed(intakeRollerSpeed);
    }
  }

  @Override
  public void end(boolean interrupted) {
    intake.setIntakeSpeed(0);
    timer.stop();
    timer.reset();
  }

  @Override
  public boolean isFinished() {
    return progress > 0.90 && timer.get() > 0.1;
  }
}
