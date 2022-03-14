// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utility.RobotHelper;

public class DriveToPositionByTicks extends CommandBase {

  private final Drivetrain m_drivetrain;
  private final static double gyroAdjustKp = 0.03;
  private double start;
  private double end;

  public DriveToPositionByTicks(Drivetrain drivetrain, double distance) {
    m_drivetrain = drivetrain;
    end = distance;
  }

  @Override
  public void initialize() {
    m_drivetrain.resetEncoders();
    m_drivetrain.resetHeading();
    start = m_drivetrain.getEncoderPosition();
  }

  @Override
  public void execute() {
    m_drivetrain.driveArcade(
        RobotHelper.piecewiseMotorController(0.5, 0.2, start, end, m_drivetrain.getEncoderPosition(), true),
        Math.min(-(m_drivetrain.getHeading() * gyroAdjustKp), 0.2));
  }

  @Override
  public void end(boolean interrupted) {
    m_drivetrain.driveArcade(0, 0);
  }

  @Override
  public boolean isFinished() {
    return RobotHelper.isDoneTraveling(start, end, m_drivetrain.getEncoderPosition(), 0.9);
  }
}
