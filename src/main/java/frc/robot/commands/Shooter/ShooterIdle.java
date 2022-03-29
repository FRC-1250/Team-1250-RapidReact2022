// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterDirection;
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
    if (intake.isIntakeBeyondBumpers()) {
      if (shooter.isUptakeSensorTripped()) {
        shooter.setUptakeConveyorSpeed(0);
      } else {
        shooter.setUptakeConveyorSpeed(0.25);
      }
    } else {
      shooter.setUptakeConveyorSpeed(0);
    }

    switch (RobotContainer.getRobotState()) {
      case SHOOT_HIGH_BACK:
        shooter.setShooterRpm(ShooterHeight.SHOOT_HIGH.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_BACK.servoPosition);
        break;
      case SHOOT_HIGH_FENDER_BACK:
        shooter.setShooterRpm(ShooterHeight.SHOOT_HIGH_FENDER.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_BACK_FEDNER.servoPosition);
        break;
      case SHOOT_LOW_BACK:
        shooter.setShooterRpm(ShooterHeight.SHOOT_LOW.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_BACK.servoPosition);
        break;
      case SHOOT_HIGH:
        shooter.setShooterRpm(ShooterHeight.SHOOT_HIGH.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_FRONT.servoPosition);
        break;
      case SHOOT_HIGH_FENDER:
        shooter.setShooterRpm(ShooterHeight.SHOOT_HIGH_FENDER.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_FRONT_FENDER.servoPosition);
        break;
      case SHOOT_LOW:
        shooter.setShooterRpm(ShooterHeight.SHOOT_LOW.rpmInTicks);
        shooter.setShooterServoPosition(ShooterDirection.SHOOT_FRONT.servoPosition);
        break;
      default:
        shooter.setShooterRpm(0);
        break;
    }
  }
}
