// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveStraight extends CommandBase {

  private final Drivetrain e_Drivetrain;
  private final PS4Controller e_Dualshock4;
  private double e_driveThrottle;
  private double e_angleAdjust;
  private final double gyroAdjustKp = 0.03;

  public DriveStraight(Drivetrain drivetrain, PS4Controller Dualshock4) {
    addRequirements(drivetrain);
    e_Drivetrain = drivetrain;
    e_Dualshock4 = Dualshock4;
  }

  @Override
  public void initialize() {
    e_Drivetrain.resetHeading();
  }

  @Override
  public void execute() {
    e_driveThrottle = e_Dualshock4.getLeftY() * Math.max(1 - e_Dualshock4.getR2Axis(), 0.5);
    e_angleAdjust = Math.min(-(e_Drivetrain.getHeading() * gyroAdjustKp), 0.2);
    e_Drivetrain.driveArcade(e_driveThrottle, e_angleAdjust);
  }
}
