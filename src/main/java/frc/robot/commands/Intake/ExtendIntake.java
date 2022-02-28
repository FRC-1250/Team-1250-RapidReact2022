// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.utility.RobotHelper;

public class ExtendIntake extends CommandBase {
  private final Intake intake;
  private final double intakeRollerSpeed;
  private double start;
  private double end;

  public ExtendIntake(Intake m_intake) {
    intake = m_intake;
    intakeRollerSpeed = 1;
    addRequirements(m_intake);
  }

  public ExtendIntake(Intake m_intake, double m_intakeRollerSpeed) {
    intake = m_intake;
    intakeRollerSpeed = m_intakeRollerSpeed;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    start = intake.getIntakeDeployRightPosition();
    end = 85; // CHANGE ME
  }

  @Override
  public void execute() {
    intake.SetIntakeRollerspeed(intakeRollerSpeed);
    
    if (RobotHelper.isDoneTraveling(start, end, intake.getIntakeDeployRightPosition())) {
      intake.setIntakeDeploySpeed(0);
    } else {
      intake.setIntakeDeploySpeed(1);
    }
  }

  @Override
  public void end(boolean interrupted) {
    intake.setIntakeDeploySpeed(0);
  }
}
