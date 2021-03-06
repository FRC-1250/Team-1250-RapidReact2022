// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;
import frc.robot.subsystems.Shooter.ShooterHeight;

public class ShootBallVelocityControl extends CommandBase {
  private final Shooter shooter;
  private final ShooterHeight shooterHeight;
  private final Sorter sorter;
  private double acceptancePercentage = 0.03;
  private boolean shooterPrimed = false;
  private long shotTimerInMs = 0;
  private boolean sensorTrippedPreviously = true;
  private Timer timer = new Timer();

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, ShooterHeight m_shooterHeight) {
    shooter = m_shooter;
    sorter = m_sorter;
    shooterHeight = m_shooterHeight;
    addRequirements(m_shooter, m_sorter);
  }

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, ShooterHeight m_shooterHeight,
      long m_shotTimerInMs) {
    this(m_shooter, m_sorter, m_shooterHeight);
    shotTimerInMs = m_shotTimerInMs;
  }

  @Override
  public void initialize() {
    if (shotTimerInMs > 0) {
      shotTimerInMs += System.currentTimeMillis();
    }

    timer.reset();
  }

  @Override
  public void execute() {
    shooter.setShooterRpm(shooterHeight.rpmInTicks);

    shooterPrimed = shooter.getRearShooterRpm() > shooterHeight.rpmInTicks
        - (shooterHeight.rpmInTicks * acceptancePercentage);

    if (shooterPrimed && shooter.isUptakeSensorTripped()) {
      shooter.setUptakeConveyorSpeed(1);
      sorter.setSortWheelSpeed(0);
      sensorTrippedPreviously = true;
    } else if (shooterPrimed && !shooter.isUptakeSensorTripped()) {
      if (sensorTrippedPreviously == true) {
        timer.start();
        sensorTrippedPreviously = false;
      }

      if (timer.get() > 0.3) {
        shooter.setUptakeConveyorSpeed(0.25);
        sorter.setSortWheelSpeed(-0.5);
        timer.stop();
        timer.reset();
      }
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
