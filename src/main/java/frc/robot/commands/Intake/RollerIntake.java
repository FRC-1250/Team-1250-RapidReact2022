// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class RollerIntake extends CommandBase {

  private final Intake intake;
  private final double intakeRollerSpeed;

  public RollerIntake(Intake m_intake) {
    intake = m_intake;
    intakeRollerSpeed = 1;
    addRequirements(m_intake);
  }

  public RollerIntake(Intake m_intake, double m_intakeRollerSpeed) {
    intake = m_intake;
    intakeRollerSpeed = m_intakeRollerSpeed;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    intake.SetIntakeRollerspeed(intakeRollerSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    intake.SetIntakeRollerspeed(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
