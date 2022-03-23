// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Drivetrain.DriveToPositionByInches;
import frc.robot.commands.Intake.ExtendIntake;
import frc.robot.commands.Intake.RetractIntake;
import frc.robot.commands.Shooter.MoveServoToPosition;
import frc.robot.commands.Shooter.ShootBallVelocityControl;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;
import frc.robot.subsystems.Shooter.ShooterDirection;
import frc.robot.subsystems.Shooter.ShooterHeight;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BallPickupAndHighShot extends SequentialCommandGroup {
  /** Creates a new BallPickupAndShoot. */

  public BallPickupAndHighShot(Intake cmd_intake, Shooter cmd_Shooter, Drivetrain cmd_drivetrain, Sorter cmd_sorter) {
    addCommands(
        new MoveServoToPosition(cmd_Shooter, ShooterDirection.SHOOT_BACK),
        new ExtendIntake(cmd_intake),
        new ParallelCommandGroup(
            new IndexBallAuto(cmd_sorter, cmd_Shooter, cmd_intake),
            new SequentialCommandGroup(
                new DriveToPositionByInches(cmd_drivetrain, 48),
                new WaitCommand(2),
                new RetractIntake(cmd_intake))),
        new DriveToPositionByInches(cmd_drivetrain, -48),
        new ShootBallVelocityControl(cmd_Shooter, cmd_sorter, ShooterHeight.SHOOT_HIGH, 5000));
  }
}
