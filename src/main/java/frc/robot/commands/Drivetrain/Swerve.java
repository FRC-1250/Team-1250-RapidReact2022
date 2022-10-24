// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Swerve extends CommandBase {
  BooleanSupplier Boost;
  Double Throttle;
  DoubleSupplier y, x, rotation;
  Drivetrain drivetrain;
  /** Creates a new Swerve. */
  public Swerve(BooleanSupplier Boost, Double Throttle, DoubleSupplier y, DoubleSupplier x,DoubleSupplier rotation,Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.Boost = Boost;
    this.Throttle = Throttle;
    this.y = y;
    this.x = x;
    this.rotation = rotation;
    this.drivetrain = drivetrain;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Double Actualx;
    Double Actualy;
    Double ActualRotation;
    Actualx = 0.0;
    Actualy = 0.0;
    ActualRotation = 0.0;

    if (Boost.getAsBoolean()){
        Actualx = x.getAsDouble();
        Actualy = y.getAsDouble();
        ActualRotation = rotation.getAsDouble();
    }
    else{
      Actualx = x.getAsDouble()*Throttle;
      Actualy = y.getAsDouble()*Throttle;
      ActualRotation = rotation.getAsDouble()*Throttle;
    }
  drivetrain.driveswerve(Actualx,Actualy, ActualRotation);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
