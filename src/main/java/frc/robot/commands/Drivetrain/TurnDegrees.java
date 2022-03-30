// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utility.RobotHelper;

public class TurnDegrees extends CommandBase {
  private final Drivetrain m_drivetrain;
  private double start;
  private double end;
  private double degrees;

  public TurnDegrees(Drivetrain drivetrain, double degrees) {
    m_drivetrain = drivetrain;
    this.degrees = degrees;
    addRequirements(drivetrain);
  }

  @Override
  public void initialize() {
    start = m_drivetrain.getHeading();
    end = start + this.degrees;
  }

  @Override
  public void execute() {
    if (Math.signum(start - end) == 1) {
      m_drivetrain.driveArcade(0,
          RobotHelper.piecewiseMotorController(-0.5, -0.2, start, end, m_drivetrain.getHeading(), true));
    } else {
      m_drivetrain.driveArcade(0,
          RobotHelper.piecewiseMotorController(0.5, 0.2, start, end, m_drivetrain.getHeading(), true));
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_drivetrain.driveArcade(0, 0);
  }

  @Override
  public boolean isFinished() {
    return RobotHelper.isDoneTraveling(start, end, m_drivetrain.getHeading(), 0.9);
  }
}
