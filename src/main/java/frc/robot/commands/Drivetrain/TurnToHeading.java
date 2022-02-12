// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TurnToHeading extends PIDCommand {
  private final Drivetrain m_drivetrain;

  public TurnToHeading(Drivetrain drivetrain, double degrees) {
    super(
        // The controller that the command will use
        new PIDController(4, 0, 0),
        // This should return the measurement
        drivetrain::getHeading,
        // This should return the setpoint (can also be a constant)
        degrees,
        // This uses the output
        output -> {
          drivetrain.driveArcade(0, output);
        },
        drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    m_drivetrain = drivetrain;
    getController().setTolerance(1);
    getController().enableContinuousInput(-180, 180);
  }

  @Override
  public void initialize() {
    // Get everything in a safe starting state.
    m_drivetrain.resetEncoders();
    m_drivetrain.resetHeading();
    super.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
