// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.FireLED;
import frc.robot.commands.Sortball;
import frc.robot.commands.Tankdrive;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LEDS;
import frc.robot.subsystems.Sorter;

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
  JoystickButton cross = new JoystickButton(driveGamepad, PS4Controller.Button.kCross.value);
  JoystickButton triangle = new JoystickButton(driveGamepad, PS4Controller.Button.kTriangle.value);
  JoystickButton circle = new JoystickButton(driveGamepad, PS4Controller.Button.kCircle.value);
  JoystickButton square = new JoystickButton(driveGamepad, PS4Controller.Button.kSquare.value);
  JoystickButton r1 = new JoystickButton(driveGamepad, PS4Controller.Button.kR1.value);
  // The robot's subsystems and commands are defined here...
  private final Sorter m_sorter = new Sorter();
  private final LEDS m_leds = new LEDS();
  private final Drivetrain m_drivetrain = new Drivetrain();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    m_drivetrain.setDefaultCommand(new Tankdrive(m_drivetrain, driveGamepad));
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
    cross.whileActiveOnce(new Sortball(m_sorter));
    r1.whileActiveOnce(new FireLED(m_leds));
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
}
