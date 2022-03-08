// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Drivetrain.DriveToPosition;
import frc.robot.commands.Shooter.MoveServoToPosition;
import frc.robot.commands.Shooter.ShootBallVelocityControl;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;
import frc.robot.subsystems.Shooter.ShooterDirection;
import frc.robot.subsystems.Shooter.ShooterHeight;

public class HighShotAndDriveBack extends SequentialCommandGroup {
  /** Creates a new HighShotAndDriveBack. */
  public HighShotAndDriveBack(Shooter cmd_Shooter, Drivetrain cmd_drivetrain, Sorter cmd_sorter) {
    addCommands(
        new MoveServoToPosition(cmd_Shooter, ShooterDirection.SHOOT_BACK),
        new ShootBallVelocityControl(cmd_Shooter, cmd_sorter, ShooterHeight.SHOOT_HIGH, 5000),
        new DriveToPosition(cmd_drivetrain, -36));
  }
}
