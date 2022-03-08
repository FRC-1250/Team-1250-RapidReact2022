// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Shooter.ShooterDirection;

public class MoveServoToPosition extends InstantCommand {
  private final Shooter m_shooter;
  private final ShooterDirection m_shooterDirection;

  /** Creates a new ServoPosition. */
  public MoveServoToPosition(Shooter shooter, ShooterDirection shooterDirection) {
    m_shooter = shooter;
    m_shooterDirection = shooterDirection;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void initialize() {
    m_shooter.setShooterServoPosition(m_shooterDirection.servoPosition);
  }

}
