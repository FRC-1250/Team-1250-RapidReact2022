// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Tank extends CommandBase {
  
  BooleanSupplier boost;
  double throttle;
  DoubleSupplier leftinput, rightinput; 
  Drivetrain drivetrain;

 /** Creates a new Tank. */
  public Tank(BooleanSupplier boost, double throttle, DoubleSupplier leftinput, DoubleSupplier rightinput,Drivetrain drivetrain) {
    this. boost = boost;
    // Use addRequirements() here to declare subsystem dependencies.
    this. throttle = throttle;
    this. leftinput = leftinput;
    this. rightinput = rightinput;
    this. drivetrain = drivetrain;
  addRequirements(drivetrain);

  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double actualleft;
    double actualright;
    actualleft = 0.0;
    actualright = 0.0;
    if (boost.getAsBoolean()){
      actualleft = leftinput.getAsDouble();
      actualright = rightinput.getAsDouble();
    }else{
      actualleft = leftinput.getAsDouble() * throttle;
      actualright = rightinput.getAsDouble() * throttle;
      }
      drivetrain.driveTank(actualleft,actualright);
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
