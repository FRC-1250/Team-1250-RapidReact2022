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
  double KpAim = 0.075;
  double KpDistance = 0.075;
  double tx = 0;
  double ty = 0;
  double tv = 0;
  double heading_error = 0;
  double distance_error = 0;
  double y_adjust = 0;
  double x_adjust = 0;

  public MoveToTarget(Limelight m_limelight, Drivetrain m_drivetrain) {
    limelight = m_limelight;
    drivetrain = m_drivetrain;
    addRequirements(m_drivetrain);
  }

  @Override
  public void execute() {
    tx = limelight.getXOffset();
    ty = limelight.getYOffset();
    heading_error = -tx;
    distance_error = -ty;
    y_adjust = 0;
    x_adjust = 0;

    if (limelight.isTargetSeen()) {
      if (tx > 1.0 || tx < -1.0) {
        x_adjust = KpAim * heading_error;
      } else {
        x_adjust = 0;
      }

      if (ty > 1.0 || ty < -1.0) {
        y_adjust = KpDistance * distance_error;
      } else {
        y_adjust = 0;
      }
    }
    drivetrain.driveArcade(-y_adjust, x_adjust);
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.driveArcade(0, 0);
  }
}
