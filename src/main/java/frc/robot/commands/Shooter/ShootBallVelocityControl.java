// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;
import frc.robot.subsystems.Shooter.ShooterDirection;
import frc.robot.subsystems.Shooter.ShooterHeight;

public class ShootBallVelocityControl extends CommandBase {
  private final Shooter shooter;
  private final ShooterHeight shooterHeight;
  private final Sorter sorter;
  private final ShooterDirection shooterDirection;
  private double acceptancePercentage = 0.05;
  private boolean shooterPrimed = false;
  private long shotTimerInMs = 0;

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, ShooterHeight m_shooterHeight,
      ShooterDirection m_shooterDirection) {
    shooter = m_shooter;
    sorter = m_sorter;
    shooterHeight = m_shooterHeight;
    shooterDirection = m_shooterDirection;
    addRequirements(m_shooter, m_sorter);
  }

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, ShooterHeight m_shooterHeight,
      ShooterDirection m_shooterDirection, long m_shotTimerInMs) {
    this(m_shooter, m_sorter, m_shooterHeight, m_shooterDirection);
    shotTimerInMs = m_shotTimerInMs;
  }

  @Override
  public void initialize() {
    if (shotTimerInMs > 0) {
      shotTimerInMs += System.currentTimeMillis();
    }
  }

  @Override
  public void execute() {
    shooter.setShooterServoPosition(shooterDirection.servoPosition);
    shooter.setShooterRpm(shooterHeight.rpmInTicks);
    
    shooterPrimed = shooter.getRearShooterRpm() > shooterHeight.rpmInTicks
        - (shooterHeight.rpmInTicks * acceptancePercentage);

    if (shooterPrimed && shooter.isUptakeSensorTripped()) {
      shooter.setUptakeConveyorSpeed(1);
      sorter.setSortWheelSpeed(0);
    } else if (shooterPrimed && !shooter.isUptakeSensorTripped()) {
      shooter.setUptakeConveyorSpeed(0.5);
      sorter.setSortWheelSpeed(-0.5);
    }
  }

  @Override
  public boolean isFinished() {
    if (shotTimerInMs > 0) {
      return System.currentTimeMillis() > shotTimerInMs;
    } else {
      return false;
    }
  }

  @Override
  public void end(boolean interrupted) {
    shooter.setShooterRpm(0);
  }
}
