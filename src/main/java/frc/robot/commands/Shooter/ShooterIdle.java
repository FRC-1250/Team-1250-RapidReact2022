// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterHeight;

public class ShooterIdle extends CommandBase {
  private final Shooter shooter;
  private final Intake intake;

  public ShooterIdle(Shooter m_shooter, Intake m_intake) {
    shooter = m_shooter;
    intake = m_intake;
    addRequirements(shooter);
  }

  @Override
  public void execute() {
    if (intake.isReverseLimitSwitchPressed()) {
      shooter.setUptakeConveyorSpeed(0);
    } else {
      if (shooter.isUptakeSensorTripped()) {
        shooter.setUptakeConveyorSpeed(0);
      } else {
        shooter.setUptakeConveyorSpeed(0.5);
      }
    }

    if (RobotContainer.getRobotState() == RobotContainer.Robotstate.SHOOT_HIGH) {
      shooter.setShooterRpm(ShooterHeight.SHOOT_HIGH.rpmInTicks);
    } else if (RobotContainer.getRobotState() == RobotContainer.Robotstate.SHOOT_LOW) {
      shooter.setShooterRpm(ShooterHeight.SHOOT_LOW.rpmInTicks);
    } else {
      shooter.setShooterRpm(0);
    }
  }
}
