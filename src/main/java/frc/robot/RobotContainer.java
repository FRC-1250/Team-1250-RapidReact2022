// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Climber.ExtendClimber;
import frc.robot.commands.Climber.ExtendClimberWithPosition;
import frc.robot.commands.Climber.RetractClimber;
import frc.robot.commands.Drivetrain.Drive;
import frc.robot.commands.Drivetrain.DriveStraight;
import frc.robot.commands.Drivetrain.MoveToTarget;
import frc.robot.commands.Intake.ExtendIntake;
import frc.robot.commands.Intake.RetractIntake;
import frc.robot.commands.Shooter.ShootBallVelocityControl;
import frc.robot.commands.Shooter.ShooterIdle;
import frc.robot.commands.Sorter.IndexBall;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.SystemMonitor;
import frc.robot.subsystems.Sorter;
import frc.robot.subsystems.Climber.ClimbHeight;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  PS4Controller driveGamepad = new PS4Controller(0);
  JoystickButton r1 = new JoystickButton(driveGamepad, PS4Controller.Button.kR1.value);
  JoystickButton r2 = new JoystickButton(driveGamepad, PS4Controller.Button.kR2.value);
  JoystickButton l1 = new JoystickButton(driveGamepad, PS4Controller.Button.kL1.value);
  JoystickButton l2 = new JoystickButton(driveGamepad, PS4Controller.Button.kL2.value);
  JoystickButton cross = new JoystickButton(driveGamepad, PS4Controller.Button.kCross.value);
  JoystickButton triangle = new JoystickButton(driveGamepad, PS4Controller.Button.kTriangle.value);
  JoystickButton circle = new JoystickButton(driveGamepad, PS4Controller.Button.kCircle.value);
  JoystickButton square = new JoystickButton(driveGamepad, PS4Controller.Button.kSquare.value);
  JoystickButton options = new JoystickButton(driveGamepad, PS4Controller.Button.kOptions.value);
  JoystickButton share = new JoystickButton(driveGamepad, PS4Controller.Button.kShare.value);

  Joystick operatorGamepad = new Joystick(1);
  JoystickButton Y = new JoystickButton(operatorGamepad, 4);
  JoystickButton B = new JoystickButton(operatorGamepad, 3);
  JoystickButton A = new JoystickButton(operatorGamepad, 2);
  JoystickButton X = new JoystickButton(operatorGamepad, 1);

  boolean singlePlayer = false;
  long shuffleBoardUpdateTimer = 0;
  long shuffleBoardUpdateCooldown = 100;
  long configChangeTimer = 0;
  long configChangeCooldown = 250;

  private final Sorter m_sorter = new Sorter();
  private final SystemMonitor m_systemMonitor = new SystemMonitor();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Shooter m_shooter = new Shooter();
  private final Intake m_intake = new Intake();
  private final Climber m_climber = new Climber();
  private final Limelight m_limelight = new Limelight();
  private static RobotDriveType m_robotDriveType;
  private Robotstate m_robotstate;
  private NetworkTableEntry robotstateNT;
  private NetworkTableEntry singlePlayerNT;;

  private enum Robotstate {
    INTAKE,
    CLIMB,
    SHOOT_HIGH,
    SHOOT_LOW
  }

  public enum RobotDriveType {
    ARCADE,
    TANK
  }

  public static RobotDriveType getDriveType() {
    return m_robotDriveType;
  }

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    m_drivetrain.setDefaultCommand(new Drive(m_drivetrain, driveGamepad, 1));
    m_sorter.setDefaultCommand(new IndexBall(m_sorter, m_shooter, m_intake));
    m_shooter.setDefaultCommand(new ShooterIdle(m_shooter, m_intake));
    robotstateNT = Constants.PRIMARY_TAB.add("Robot state", "").withPosition(0, 4).getEntry();
    singlePlayerNT = Constants.PRIMARY_TAB.add("Single player", false).withPosition(0, 5).getEntry();

    m_robotstate = Robotstate.INTAKE;
    m_robotDriveType = RobotDriveType.TANK;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    shootHigh.whileActiveOnce(new ShootBallVelocityControl(m_shooter, m_sorter, 21500, true));
    track.whileActiveOnce(new MoveToTarget(m_limelight, m_drivetrain));
    shootLow.whileActiveOnce(new ShootBallVelocityControl(m_shooter, m_sorter, 7500, true));
    extendClimber.whileActiveOnce(new ExtendClimber(m_climber));
    extendClimberToLow.whileActiveOnce(new ExtendClimberWithPosition(m_climber, ClimbHeight.CLIMB_MID_RUNG));
    retractClimber.whileActiveOnce(new RetractClimber(m_climber, m_systemMonitor));
    extendIntake.whenActive(new ExtendIntake(m_intake));
    retractIntake.whenActive(new RetractIntake(m_intake));

    r2.whileActiveOnce(new Drive(m_drivetrain, driveGamepad, 0.5));
    l2.whileActiveOnce(new DriveStraight(m_drivetrain, driveGamepad, 1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }

  public void changeNumberOfOperators() {
    if (options.get() && configChangeTimer < System.currentTimeMillis()) {
      singlePlayer = !singlePlayer;
      configChangeTimer = System.currentTimeMillis() + configChangeCooldown;
    }
  }

  public void changeDriveMode() {
    if (share.get() && configChangeTimer < System.currentTimeMillis()) {
      if (RobotDriveType.TANK == getDriveType()) {
        m_robotDriveType = RobotDriveType.ARCADE;
      } else if (RobotDriveType.ARCADE == getDriveType()) {
        m_robotDriveType = RobotDriveType.TANK;
      }
      configChangeTimer = System.currentTimeMillis() + configChangeCooldown;
    }
  }

  public void setRobotState() {
    boolean isShootHighModeButtonPressed = false;
    boolean isShootLowModeButtonPressed = false;
    boolean isClimbModeButtonPressed = false;

    if (singlePlayer) {
      isShootHighModeButtonPressed = triangle.get();
      isShootLowModeButtonPressed = cross.get();
      isClimbModeButtonPressed = square.get();
    } else {
      isShootHighModeButtonPressed = Y.get();
      isShootLowModeButtonPressed = A.get();
      isClimbModeButtonPressed = X.get();
    }

    if (isShootHighModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_HIGH;
    } else if (isShootLowModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_LOW;
    } else if (isClimbModeButtonPressed) {
      m_robotstate = Robotstate.CLIMB;
    } else {
      m_robotstate = Robotstate.INTAKE;
    }
  }

  public void updateShuffleBoard() {
    if (shuffleBoardUpdateTimer < System.currentTimeMillis()) {
      m_climber.updateShuffleBoard();
      m_drivetrain.updateShuffleBoard();
      m_shooter.updateShuffleBoard();
      m_intake.updateShuffleBoard();
      m_systemMonitor.updateShuffleBoard();
      robotstateNT.setString(m_robotstate.toString());
      singlePlayerNT.setBoolean(singlePlayer);
      shuffleBoardUpdateTimer = System.currentTimeMillis() + shuffleBoardUpdateCooldown;
    }
  }

  // Shoot high mode
  Trigger shootHigh = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.SHOOT_HIGH == m_robotstate && r1.get();
    }
  };

  Trigger track = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.SHOOT_HIGH == m_robotstate && l1.get();
    }
  };

  // Shoot low mode
  Trigger shootLow = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.SHOOT_LOW == m_robotstate && r1.get();
    }
  };

  // Intake mode
  Trigger extendIntake = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.INTAKE == m_robotstate && l1.get();
    }
  };

  Trigger retractIntake = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.INTAKE == m_robotstate && r1.get();
    }
  };

  // Climb mode
  Trigger extendClimber = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.CLIMB == m_robotstate && l1.get();
    }
  };

  Trigger extendClimberToLow = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.CLIMB == m_robotstate && l2.get();
    }
  };

  Trigger retractClimber = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.CLIMB == m_robotstate && r1.get();
    }
  };
}
