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
  private final double e_driveReduction = 0.7;
  double e_fullSendVal = 0.0;

  public Drive(Drivetrain drivetrain, PS4Controller Dualshock4) {
    addRequirements(drivetrain);
    e_Drivetrain = drivetrain;
    e_Dualshock4 = Dualshock4;
  }

  /**
   * e_driveReduction is a innate drive speed reduction that is always in place.
   * Sacrifaces some speed for control.
   */
  @Override
  public void execute() {
    //Logic to remove  all drive throttles 
    if(e_Dualshock4.getR2Axis() > 0.3){
      e_fullSendVal = 1.0 - e_driveReduction;
    } else{
      e_fullSendVal = 0.0;
    }
    if (RobotContainer.RobotDriveType.TANK == RobotContainer.getDriveType()) {
      double inputLeft = e_Dualshock4.getLeftY() * (e_driveReduction + e_fullSendVal);
      double inputRight = e_Dualshock4.getRightY() * (e_driveReduction + e_fullSendVal);
      e_Drivetrain.driveTank(inputLeft, inputRight);
    } else if (RobotContainer.RobotDriveType.ARCADE == RobotContainer.getDriveType()) {
      double inputY = e_Dualshock4.getLeftY() * (e_driveReduction + e_fullSendVal);
      double inputX = e_Dualshock4.getRightX() * (e_driveReduction + e_fullSendVal);
      e_Drivetrain.driveArcade(inputY, inputX);
    }
  }
}
