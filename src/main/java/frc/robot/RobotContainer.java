// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.Auton.TwoBallHighHangarSide;
import frc.robot.commands.Auton.TwoBallHighTerminalSIde;
import frc.robot.commands.Auton.OneBallHigh;
import frc.robot.commands.Auton.OneBallLow;
import frc.robot.commands.Climber.ExtendClimber;
import frc.robot.commands.Climber.ExtendClimberWithPosition;
import frc.robot.commands.Climber.ResetClimberTicks;
import frc.robot.commands.Climber.RetractClimber;
import frc.robot.commands.Drivetrain.Drive;
import frc.robot.commands.Drivetrain.DriveStraight;
import frc.robot.commands.Drivetrain.DriveToPositionByInches;
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
import frc.robot.subsystems.Shooter.ShooterHeight;

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
  JoystickButton touchpad = new JoystickButton(driveGamepad, PS4Controller.Button.kTouchpad.value);

  Joystick operatorGamepad = new Joystick(1);
  JoystickButton Y = new JoystickButton(operatorGamepad, 4);
  JoystickButton B = new JoystickButton(operatorGamepad, 3);
  JoystickButton A = new JoystickButton(operatorGamepad, 2);
  JoystickButton X = new JoystickButton(operatorGamepad, 1);
  JoystickButton LB = new JoystickButton(operatorGamepad, 5);
  JoystickButton RB = new JoystickButton(operatorGamepad, 6);
  int dpadleft = 270;
  int dpadright = 90;
  int dpaddown = 180;
  int dpadup = 0;
  boolean singlePlayer = false;
  long shuffleBoardUpdateTimer = 0;
  long shuffleBoardUpdateCooldown = 100;
  long configChangeTimer = 0;
  long configChangeCooldown = 250;

  private final Sorter m_sorter = new Sorter();
  private final SystemMonitor m_systemMonitor = SystemMonitor.getInstance();
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Shooter m_shooter = new Shooter();
  private final Intake m_intake = new Intake();
  private final Climber m_climber = new Climber();
  private final Limelight m_limelight = new Limelight();
  private SendableChooser<Command> m_chooser = new SendableChooser<>();
  private static RobotDriveType m_robotDriveType;
  private static Robotstate m_robotstate;
  private NetworkTableEntry robotstateNT;

  public enum Robotstate {
    INTAKE,
    CLIMB,
    CLIMB_MID,
    CLIMB_LOW,
    SHOOT_HIGH_BACK,
    SHOOT_HIGH_FENDER_BACK,
    SHOOT_LOW_BACK,
    SHOOT_HIGH,
    SHOOT_HIGH_FENDER,
    SHOOT_LOW
  }

  public enum RobotDriveType {
    ARCADE,
    TANK
  }

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    m_drivetrain.setDefaultCommand(new Drive(m_drivetrain, driveGamepad));
    m_sorter.setDefaultCommand(new IndexBall(m_sorter, m_shooter, m_intake));
    m_shooter.setDefaultCommand(new ShooterIdle(m_shooter, m_intake));
    m_chooser.setDefaultOption("High shot + taxi", new OneBallHigh(m_shooter, m_drivetrain, m_sorter));
    m_chooser.addOption("2 Ball High Shot - Close Balls", new TwoBallHighHangarSide(m_intake, m_shooter, m_drivetrain, m_sorter));
    m_chooser.addOption("2 Ball High shot - Far Right Ball", new TwoBallHighTerminalSIde(m_intake, m_shooter, m_drivetrain, m_sorter));
    m_chooser.addOption("Low shot + taxi", new OneBallLow(m_shooter, m_drivetrain, m_sorter));
    m_chooser.addOption("taxi", new DriveToPositionByInches(m_drivetrain, 24));
    Constants.PRIMARY_TAB.add("Auto", m_chooser).withSize(2, 1).withPosition(7, 0);
    Constants.PRIMARY_TAB.addCamera("Limelight", "Limelight", "http://10.12.50.11:5800").withSize(6, 4).withPosition(0,0);
    robotstateNT = Constants.PRIMARY_TAB.add("Robot state", "").withPosition(7, 1).withSize(2, 1).getEntry();
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
    // Shooting
    shootHigh.whileActiveOnce(new ShootBallVelocityControl(m_shooter, m_sorter, ShooterHeight.SHOOT_HIGH));
    track.whileActiveOnce(new MoveToTarget(m_limelight, m_drivetrain));
    shootHighFender.whileActiveOnce(new ShootBallVelocityControl(m_shooter, m_sorter, ShooterHeight.SHOOT_HIGH_FENDER));
    shootLow.whileActiveOnce(new ShootBallVelocityControl(m_shooter, m_sorter, ShooterHeight.SHOOT_LOW));

    // Climbing
    extendClimber.whileActiveOnce(new ExtendClimber(m_climber));
    extendClimberToLow.whenActive(new ExtendClimberWithPosition(m_climber, ClimbHeight.CLIMB_LOW_RUNG));
    extendClimberToMid.whenActive(new ExtendClimberWithPosition(m_climber, ClimbHeight.CLIMB_MID_RUNG));
    retractClimber.whileActiveOnce(new RetractClimber(m_climber, m_systemMonitor));

    // Intaking
    extendIntake.whenActive(new ExtendIntake(m_intake));
    retractIntake.whenActive(new RetractIntake(m_intake));

    // Drive mods
    // Driver R2 is attached to normal driving and driving straight as a throttle
    // input!
    l2.whileActiveOnce(new DriveStraight(m_drivetrain, driveGamepad));
    Constants.SYSTEM_MONITOR_TAB.add(new ResetClimberTicks(m_climber));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_chooser.getSelected();
  }

  public void setCoast() {
    m_drivetrain.configureCoast();
  }

  public void setBrake() {
    m_drivetrain.configureBrake();
  }

  public static RobotDriveType getDriveType() {
    return m_robotDriveType;
  }

  public static Robotstate getRobotState() {
    return m_robotstate;
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
    boolean isShootBackButtonPressed = false;
    boolean isShootHighFenderButtonPressed = false;
    boolean isShootHighModeButtonPressed = false;
    boolean isShootLowModeButtonPressed = false;
    boolean isClimbModeButtonPressed = false;
    boolean isClimbMidPressed = false;
    boolean isClimbLowPressed = false;

    if (singlePlayer) {
      isClimbMidPressed = driveGamepad.getPOV() == dpadup;
      isClimbLowPressed = driveGamepad.getPOV() == dpaddown;
      isClimbModeButtonPressed = driveGamepad.getPOV() == dpadleft;
      isShootHighModeButtonPressed = triangle.get();
      isShootLowModeButtonPressed = cross.get();
      isShootHighFenderButtonPressed = circle.get();
    } else {
      isClimbMidPressed = operatorGamepad.getPOV() == dpadup;
      isClimbLowPressed = operatorGamepad.getPOV() == dpaddown;
      isClimbModeButtonPressed = operatorGamepad.getPOV() == dpadleft;
      isShootHighModeButtonPressed = Y.get();
      isShootLowModeButtonPressed = A.get();
      isShootHighFenderButtonPressed = B.get();
      isShootBackButtonPressed = RB.get();
    }

    if (isShootBackButtonPressed && isShootHighModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_HIGH_BACK;
    } else if (isShootBackButtonPressed && isShootHighFenderButtonPressed) {
      m_robotstate = Robotstate.SHOOT_HIGH_FENDER_BACK;
    } else if (isShootBackButtonPressed && isShootLowModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_LOW_BACK;
    } else if (isShootHighModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_HIGH;
    } else if (isShootHighFenderButtonPressed) {
      m_robotstate = Robotstate.SHOOT_HIGH_FENDER_BACK;
    } else if (isShootLowModeButtonPressed) {
      m_robotstate = Robotstate.SHOOT_LOW;
    } else if (isClimbModeButtonPressed) {
      m_robotstate = Robotstate.CLIMB;
    } else if (isClimbMidPressed) {
      m_robotstate = Robotstate.CLIMB_MID;
    } else if (isClimbLowPressed) {
      m_robotstate = Robotstate.CLIMB_LOW;
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
      shuffleBoardUpdateTimer = System.currentTimeMillis() + shuffleBoardUpdateCooldown;
    }
  }

  // Shoot high mode
  Trigger shootHigh = new Trigger() {
    @Override
    public boolean get() {
      return (Robotstate.SHOOT_HIGH == m_robotstate || Robotstate.SHOOT_HIGH_BACK == m_robotstate) && r1.get();
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
      return (Robotstate.SHOOT_LOW == m_robotstate || Robotstate.SHOOT_LOW_BACK == m_robotstate) && r1.get();
    }
  };

  // Shoot high fender
  Trigger shootHighFender = new Trigger() {
    @Override
    public boolean get() {
      return (Robotstate.SHOOT_HIGH_FENDER == m_robotstate || Robotstate.SHOOT_HIGH_FENDER_BACK == m_robotstate)
          && r1.get();
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
      return (Robotstate.INTAKE == m_robotstate && r1.get()) ;
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
      return Robotstate.CLIMB_LOW == m_robotstate && l1.get();
    }
  };

  Trigger extendClimberToMid = new Trigger() {
    @Override
    public boolean get() {
      return Robotstate.CLIMB_MID == m_robotstate && l1.get();
    }
  };

  Trigger retractClimber = new Trigger() {
    @Override
    public boolean get() {
      return (Robotstate.CLIMB == m_robotstate || Robotstate.CLIMB_LOW == m_robotstate
          || Robotstate.CLIMB_MID == m_robotstate) && r1.get();
    }
  };
}
