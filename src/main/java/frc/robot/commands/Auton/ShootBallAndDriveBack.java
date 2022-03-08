// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Drivetrain.DriveToPosition;
import frc.robot.commands.Shooter.ShootBallVelocityControl;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShootBallAndDriveBack extends SequentialCommandGroup {
  /** Creates a new ShootBallAndDriveBack. */
  public ShootBallAndDriveBack(Shooter cmd_Shooter, Drivetrain cmd_drivetrain, Sorter cmd_sorter) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new DriveToPosition(cmd_drivetrain, -36),
        new ShootBallVelocityControl(cmd_Shooter, cmd_sorter, 21500, false, 5000));
  }
}