// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Sorter;

public class ShootBallVelocityControl extends CommandBase {
  private final Shooter shooter;
  private final double targetSpeedInTicks;
  private final Sorter sorter;
  private final boolean shootFrontOfRobot;
  private double acceptancePercentage = 0.05;
  private boolean shooterPrimed = false;
  private long shotTimerInMs = 0;

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, double m_targetSpeed,
      boolean m_shootFrontOfRobot) {
    shooter = m_shooter;
    sorter = m_sorter;
    shootFrontOfRobot = m_shootFrontOfRobot;
    targetSpeedInTicks = m_targetSpeed;
    addRequirements(m_shooter, m_sorter);
  }

  public ShootBallVelocityControl(Shooter m_shooter, Sorter m_sorter, double m_targetSpeed,
      boolean m_shootFrontOfRobot, long m_shotTimerInMs) {
    shooter = m_shooter;
    sorter = m_sorter;
    shootFrontOfRobot = m_shootFrontOfRobot;
    targetSpeedInTicks = m_targetSpeed;
    shotTimerInMs = m_shotTimerInMs;
    addRequirements(m_shooter, m_sorter);
  }

  @Override
  public void initialize() {
    if (shotTimerInMs > 0) {
      shotTimerInMs += System.currentTimeMillis();
    }
  }

  @Override
  public void execute() {
    if (shootFrontOfRobot) {
      shooter.setShooterServoPosition(0.25);
    } else {
      shooter.setShooterServoPosition(0.75);
    }

    shooter.setShooterRpm(targetSpeedInTicks);
    shooterPrimed = shooter.getRearShooterRpm() > targetSpeedInTicks - (targetSpeedInTicks * acceptancePercentage);

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
