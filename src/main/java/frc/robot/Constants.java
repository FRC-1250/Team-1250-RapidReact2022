// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    // Diagnostics
    public static final int CANDLE_CAN_ID = 10;
    public static final int CANDLE_LED_COUNT = 8;
    public static final int CANDLE_EXPECTED_DEVICES_COUNT = 15;

    // Drivetrain
    public static final int DRIVETRAIN_BACK_RIGHT_CAN_ID = 15;
    public static final int DRIVETRAIN_BACK_LEFT_CAN_ID = 16;
    public static final int DRIVETRAIN_FRONT_LEFT_CAN_ID = 17;
    public static final int DRIVETRAIN_FRONT_RIGHT_CAN_ID = 18;
    public static final int PIGEON_CAN_ID = 19;
    public static final double DRIVETRAIN_GEAR_RATIO = 1 / 10.75;

    // Internal intake/sort
    public static final int sorter_CAN_ID = 25;
    public static final int intakeDeployRight_CAN_ID = 26;
    public static final int sorterCollect_CAN_ID = 27;
    public static final int intakeDeployLeft_CAN_ID = 28;
    public static final double INTAKE_DEPLOY_GEAR_RATIO = 1 / 7;
    public static final int INTAKE_RIGHT_REVERSE_LIMITSWITCH_DIO_ID = 1;
    public static final int INTAKE_LEFT_REVERSE_LIMITSWITCH_DIO_ID = 2;
    public static final double INTAKE_PASSED_BUMPER_REVOLUTION_DISTANCE = 4;
    public static final double INTAKE_PASSED_FRAME_REVOLUTION_DISTANCE = 1;

    // Climber
    public static final int climberHook_CAN_ID = 35;
    public static final int ratchetServo_PWM_ID = 2;
    public static final double CLIMBER_HOOK_GEAR_RATIO = 1 / 20;

    // PDP
    public static final int POWER_DISTRIBUTION_BOARD_CAN_ID = 40;

    // Shooter
    public static final int uptakeConveyor_CAN_ID = 45;
    public static final int shooterFront_CAN_ID = 46;
    public static final int shooterRear_CAN_ID = 47;
    public static final int SHOOTER_ANGLE_LEFT_PWN_ID = 0;
    public static final int SHOOTER_ANGLE_RIGHT_PWN_ID = 1;
    public static final int SHOOTER_UPTAKE_CONVEYOR_SENSOR_DIO_ID = 9;

    public static final double shooter_P = 1;
    public static final double shooter_I = 0;
    public static final double shooter_D = 0;
    public static final double shooter_F = 0.05115;

    // External intake/sorter
    public static final int intakeRoller_CAN_ID = 50;

    // Shuffleboard
    public static final ShuffleboardTab PRIMARY_TAB = Shuffleboard.getTab("Primary");
    // public static final ShuffleboardTab DRIVETRAIN_TAB = Shuffleboard.getTab("Drivetrain");
    // public static final ShuffleboardTab CLIMBER_TAB = Shuffleboard.getTab("Climber");
    // public static final ShuffleboardTab INTAKE_SORT_TAB = Shuffleboard.getTab("Intake-Sort");
    // public static final ShuffleboardTab SHOOTER_TAB = Shuffleboard.getTab("Shooter");
    public static final ShuffleboardTab SYSTEM_MONITOR_TAB = Shuffleboard.getTab("System");

}
