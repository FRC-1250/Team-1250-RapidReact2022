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
    public static final int DRIVETRAIN_FRONT_LEFT_CAN_ID = 0;
    public static final int DRIVETRAIN_BACK_LEFT_CAN_ID = 1;
    public static final int DRIVETRAIN_FRONT_RIGHT_CAN_ID = 2;
    public static final int DRIVETRAIN_BACK_RIGHT_CAN_ID = 3;
    public static final int DRIVETRAIN_PIGEON_CAN_ID = 4;

    public static final int intakeRoller_CAN_ID = 5;
    public static final int intakeDeployRight_CAN_ID = 6;
    public static final int intakeDeployLeft_CAN_ID = 7;

    public static final int uptakeConveyor_CAN_ID = 8;
    public static final int shooterTop_CAN_ID = 9;
    public static final int shooterBottom_CAN_ID = 10;

    public static final int sorter_CAN_ID = 22;
    public static final int sorterCollect_CAN_ID = 21;

    public static final double shooter_P = 1; 
    public static final double shooter_I = 0; 
    public static final double shooter_D = 0; 
    public static final double shooter_F = 0.05115; 
}   
