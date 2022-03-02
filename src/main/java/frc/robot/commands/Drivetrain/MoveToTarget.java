// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Limelight;

public class MoveToTarget extends CommandBase {
  private final Limelight limelight;
  private final Drivetrain drivetrain;

  /**
   * Example and values from
   * https://docs.limelightvision.io/en/latest/cs_aimandrange.html
   */
  double KpAim = -0.1;
  double KpDistance = -0.1;
  double min_aim_command = 0.05;
  double tx = 0;
  double ty = 0;
  double tv = 0;
  double heading_error = 0;
  double distance_error = 0;
  double steering_adjust = 0;
  double distance_adjust = 0;
  double left_command = 0;
  double right_command = 0;

  public MoveToTarget(Limelight m_limelight, Drivetrain m_drivetrain) {
    limelight = m_limelight;
    drivetrain = m_drivetrain;
    addRequirements(m_drivetrain);
  }

  @Override
  public void execute() {
    // tx = limelight.getXOffset();
    // ty = limelight.getYOffset();
    // heading_error = -tx;
    // distance_error = -ty;
    // steering_adjust = 0.0;

    // if (tx > 1.0) {
    //   steering_adjust = KpAim * heading_error - min_aim_command;
    // } else if (tx < 1.0) {
    //   steering_adjust = KpAim * heading_error + min_aim_command;
    // }
    // distance_adjust = KpDistance * distance_error;
    // left_command += steering_adjust + distance_adjust;
    // right_command -= steering_adjust + distance_adjust;
    // drivetrain.driveTank(left_command, right_command);
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.driveTank(0, 0);
  }
}
