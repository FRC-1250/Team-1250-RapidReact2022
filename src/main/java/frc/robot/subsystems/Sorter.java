// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sorter extends SubsystemBase {
  CANSparkMax sorter = new CANSparkMax(Constants.sorter_CAN_ID, MotorType.kBrushless);
  CANSparkMax sorterCollect = new CANSparkMax(Constants.sorterCollect_CAN_ID, MotorType.kBrushless);

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  public final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  public final Color kBlueTarget = new Color(0.25, 0.29, 0.45);
  public final Color kRedTarget = new Color(0.60, 0.32, 0.07);
  public final ColorMatch m_colorMatcher = new ColorMatch();

  private ShuffleboardTab sorterTab;
  public NetworkTableEntry sortSpeed;
  public NetworkTableEntry conveySpeed;
  public NetworkTableEntry motorSwitchDelayInMs;
  public NetworkTableEntry detectedColor;

  /** Creates a new Sorter. */
  public Sorter() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kRedTarget);

    sorterTab = Shuffleboard.getTab("Sorter");
    sortSpeed = sorterTab.add("SortSpeed", 0).getEntry();
    conveySpeed = sorterTab.add("ConveyorSpeed", 0).getEntry();
    motorSwitchDelayInMs = sorterTab.add("MotorSwitchDelayInMs", 100).getEntry();
    detectedColor = sorterTab.add("DetectedColor", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
    sorterTab.add("Sorter", this);
  }

  public void Setsortercollectspeed(double speed) {
    sorterCollect.set(speed);
  }

  public void Setsorterspeed(double speed) {
    sorter.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
