// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Tankdrive extends CommandBase {
  /** Creates a new Tankdrive. */
  private final Drivetrain e_Drivetrain;
  private final PS4Controller e_Dualshock4;

  public Tankdrive(Drivetrain drivetrain, PS4Controller Dualshock4) {
    // Use addRequirements() here to declare subsystem dependencies.
    e_Drivetrain = drivetrain;
    e_Dualshock4 = Dualshock4;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    e_Drivetrain.driveTank(e_Dualshock4.getLeftY(), e_Dualshock4.getRightY());
  }
}
