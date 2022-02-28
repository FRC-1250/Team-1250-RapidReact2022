// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShootBall extends CommandBase {
  private final Shooter shooter;
  private final double targetSpeed;

  public ShootBall(Shooter m_shooter) {
    shooter = m_shooter;
    targetSpeed = 0.8;
  }

  public ShootBall(Shooter m_shooter, double m_targetSpeed) {
    shooter = m_shooter;
    targetSpeed = m_targetSpeed;
  }

  @Override
  public void execute() {
    shooter.setShooterServoPosition(0.2);
    shooter.setShooterSpeed(targetSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    shooter.setShooterSpeed(0);
  }
}
