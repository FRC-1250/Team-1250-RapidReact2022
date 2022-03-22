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
  private double e_driveThrottle;

  public Drive(Drivetrain drivetrain, PS4Controller Dualshock4, double driveThrottle) {
    addRequirements(drivetrain);
    e_Drivetrain = drivetrain;
    e_Dualshock4 = Dualshock4;
    e_driveThrottle = driveThrottle;
  }

  @Override
  public void execute() {
    e_driveThrottle = Math.max(1 - e_Dualshock4.getR2Axis(), 0.65);
    if (RobotContainer.RobotDriveType.TANK == RobotContainer.getDriveType()) {
      e_Drivetrain.driveTank((e_Dualshock4.getLeftY() * 0.8) * e_driveThrottle,
          (e_Dualshock4.getRightY() * 0.8) * e_driveThrottle);
    } else if (RobotContainer.RobotDriveType.ARCADE == RobotContainer.getDriveType()) {
      e_Drivetrain.driveArcade((e_Dualshock4.getLeftY() * 0.8) * e_driveThrottle,
          e_Dualshock4.getRightX() * e_driveThrottle);
    }
  }
}
