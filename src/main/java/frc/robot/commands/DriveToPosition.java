// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveToPosition extends PIDCommand {

  private final Drivetrain m_drivetrain;

  /** Creates a new DriveToPos. */
  public DriveToPosition(Drivetrain drivetrain, double distance) {
    super(
        // The controller that the command will use
        new PIDController(4, 0, 0),
        // This should return the measurement
        drivetrain::getDistanceForwardBack,
        // This should return the setpoint (can also be a constant)
        distance,
        // This uses the output
        output -> {
          drivetrain.driveArcade(output, -drivetrain.getHeading());
        },
        drivetrain);
    m_drivetrain = drivetrain;
    getController().setTolerance(1);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  @Override
  public void initialize() {
    // Get everything in a safe starting state.
    m_drivetrain.resetDistance();
    m_drivetrain.resetHeading();
    super.initialize();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
