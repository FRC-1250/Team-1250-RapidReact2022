// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber.ClimbHeight;
import frc.robot.utility.RobotHelper;

public class ExtendClimberWithPosition extends CommandBase {
  private final Climber m_climber;
  private final ClimbHeight m_climbHeight;
  private long unjamServoTimer = 0;
  private double start;
  private double end;

  public ExtendClimberWithPosition(Climber climber, ClimbHeight climbHeight) {
    m_climber = climber;
    m_climbHeight = climbHeight;
    addRequirements(climber);
  }

  @Override
  public void initialize() {
    unjamServoTimer = System.currentTimeMillis() + 100;
    start = m_climber.getClimberHookPosition();
    end =  m_climbHeight.heightInTicks;
  }

  @Override
  public void execute() {
    m_climber.setRatchetPosition(0.75);
    if (unjamServoTimer > System.currentTimeMillis()) {
      m_climber.setClimberHookSpeed(0.5);
    } else {
      m_climber.setClimberHookSpeed(-0.75);
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.setClimberHookSpeed(0);
    m_climber.extendClimberHasMoved = true;
  }

  @Override
  public boolean isFinished() {
    return RobotHelper.isDoneTraveling(start, end, m_climber.getClimberHookPosition());
  }
}
