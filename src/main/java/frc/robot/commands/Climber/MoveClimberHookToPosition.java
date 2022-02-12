// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Climber;
import frc.robot.utility.RobotHelper;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MoveClimberHookToPosition extends PIDCommand {
  /** Creates a new ExtendClimberHook. */

  public MoveClimberHookToPosition(Climber climber, double distance) {
    super(
        // The controller that the command will use
        new PIDController(4, 0, 0),
        // This should return the measurement
        climber::getClimberHookPosition,
        // This should return the setpoint (can also be a constant)
        RobotHelper.ConvertInchesToMotorTicks(Constants.CLIMBER_HOOK_GEAR_RATIO, distance, 3, 2048, false),
        // This uses the output
        output -> {
          // Use the output here
          climber.setClimberHookSpeed(output);
        }, climber);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
