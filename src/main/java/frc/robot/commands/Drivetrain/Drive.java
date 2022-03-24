// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drivetrain;

public class Drive extends CommandBase {
  private final Drivetrain e_Drivetrain;
  private final PS4Controller e_Dualshock4;
  private final double e_driveReduction = 0.6;
  private final double e_maxDriveThrottlePercent = 1.666;

  public Drive(Drivetrain drivetrain, PS4Controller Dualshock4) {
    addRequirements(drivetrain);
    e_Drivetrain = drivetrain;
    e_Dualshock4 = Dualshock4;
  }

  /**
   * e_driveTriggerThrottle is a value that scales from 1 to the
   * e_maxDriveThrottlePercent value and is then
   * multipled by the driver input.
   * 
   * e_driveReduction is a innate drive speed reduction that is always in place.
   * Sacrifaces some speed for control.
   */
  @Override
  public void execute() {
    double e_driveTriggerThrottle = Math.max(1 + e_Dualshock4.getR2Axis(), e_maxDriveThrottlePercent);
    if (RobotContainer.RobotDriveType.TANK == RobotContainer.getDriveType()) {
      double inputLeft = e_Dualshock4.getLeftY() * e_driveReduction * e_driveTriggerThrottle;
      double inputRight = e_Dualshock4.getRightY() * e_driveReduction * e_driveTriggerThrottle;
      e_Drivetrain.driveTank(inputLeft, inputRight);
    } else if (RobotContainer.RobotDriveType.ARCADE == RobotContainer.getDriveType()) {
      double inputY = e_Dualshock4.getLeftY() * e_driveReduction * e_driveTriggerThrottle;
      double inputX = e_Dualshock4.getRightX() * e_driveReduction * e_driveTriggerThrottle;
      e_Drivetrain.driveArcade(inputY, inputX);
    }
  }
}
