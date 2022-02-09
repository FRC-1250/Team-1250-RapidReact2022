// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /*
 "---Chassis---
    RoboRIO (120 Ω)
    CANdle
    Pidgeon IMU
    Drive Rear Right
    Drive Rear Left
    Drive Front Left
    Drive Front Right
    Intake Sorter Rear
    Intake Extender Right
    Intake Conveyor Lateral
    Intake Extender Left
    Pwr Dist. Hub (no term)
    ---Shooter---
    Shooter Conveyor Vertical
    Shooter Roller Front
    Shooter Roller Rear
    ---Hangar---
    Hangar Hook Primary
    ---Deployed Intake---
    Intake Roller External
    120 Ω Terminator
    "
    */				                            

    public static final int CANDLE_CAN_ID = 10;
    public static final int CANDLE_LED_COUNT = 8;
    public static final int CANDLE_EXPECTED_DEVICES_COUNT = 15;

    public static final int PIGEON_CAN_ID = 11;

    public static final int DRIVETRAIN_BACK_RIGHT_CAN_ID = 20;
    public static final int DRIVETRAIN_BACK_LEFT_CAN_ID = 21;
    public static final int DRIVETRAIN_FRONT_LEFT_CAN_ID = 22;
    public static final int DRIVETRAIN_FRONT_RIGHT_CAN_ID = 23;
    public static final double DRIVETRAIN_GEAR_RATIO = 1 / 10.75;

    public static final int sorter_CAN_ID = 30;
    public static final int intakeDeployRight_CAN_ID = 31;
    public static final int sorterCollect_CAN_ID = 32;
    public static final int intakeDeployLeft_CAN_ID = 33;
    public static final int intakeRoller_CAN_ID = 60;
    public static final double INTAKE_DEPLOY_GEAR_RATIO = 1 / 7;
    
    public static final int uptakeConveyor_CAN_ID = 40;
    public static final int shooterBottom_CAN_ID = 41;
    public static final int shooterTop_CAN_ID = 42;
    public static final int SHOOTER_ANGLE_LEFT_PWN_ID = 0;
    public static final int SHOOTER_ANGLE_RIGHT_PWN_ID = 1;
    public static final int SHOOTER_UPTAKE_CONVEYOR_SENSOR_DIO_ID = 0;

    public static final double shooter_P = 1; 
    public static final double shooter_I = 0; 
    public static final double shooter_D = 0; 
    public static final double shooter_F = 0.05115; 

    public static final int climberHook_CAN_ID = 50;
    public static final int ratchetServo_PWM_ID = 2;
    public static final double CLIMBER_HOOK_GEAR_RATIO = 1 / 20;

}   
