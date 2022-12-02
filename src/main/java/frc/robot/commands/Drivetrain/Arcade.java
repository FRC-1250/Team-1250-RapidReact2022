// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class Arcade extends CommandBase {
  private final Drivetrain drivetrain;
  private final BooleanSupplier boost;
  private final DoubleSupplier yInputSupplier;
  private final DoubleSupplier rotationInputSupplier;
  private final Double throttle;
  private final Double DEADBAND_THRESHOLD = 0.05;
  private double yInput = 0;
  private double rotationInput = 0;
  private double actualY = 0;
  private double actualRotation = 0;

  public Arcade(BooleanSupplier boost, Double throttle, DoubleSupplier yInput, DoubleSupplier rotationInput,
      Drivetrain drivetrain) {
    this.boost = boost;
    this.throttle = throttle;
    this.yInputSupplier = yInput;
    this.rotationInputSupplier = rotationInput;
    this.drivetrain = drivetrain;
    addRequirements(drivetrain);
  }

  @Override
  public void execute() {
    yInput = MathUtil.applyDeadband(yInputSupplier.getAsDouble(), DEADBAND_THRESHOLD);
    rotationInput = MathUtil.applyDeadband(rotationInputSupplier.getAsDouble(), DEADBAND_THRESHOLD);

    if (boost.getAsBoolean()) {
      actualY = yInput;
      actualRotation = rotationInput;
    } else {
      actualY = yInput * throttle;
      actualRotation = rotationInput * throttle;
    }

    drivetrain.driveArcade(actualY, actualRotation);
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.driveArcade(0, 0);
  }
}
