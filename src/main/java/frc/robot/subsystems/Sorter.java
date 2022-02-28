// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sorter extends SubsystemBase {

  private CANSparkMax sorter = new CANSparkMax(Constants.sorter_CAN_ID, MotorType.kBrushless);
  private CANSparkMax sorterCollect = new CANSparkMax(Constants.sorterCollect_CAN_ID, MotorType.kBrushless);

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final Color kBlueTarget = new Color(0.16, 0.397, 0.45);
  private final Color kRedTarget = new Color(0.517, 0.345, 0.138);
  private final Alliance alliance;
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private NetworkTableEntry detectedColorGraphNT;
  private NetworkTableEntry detectedColorNT;
  private NetworkTableEntry proximityNT;

  public NetworkTableEntry hits;
  public NetworkTableEntry miss;

  /** Creates a new Sorter. */
  public Sorter() {
    alliance = DriverStation.getAlliance();
    configureColorMatcher();
    configureShuffleBoard();
  }

  private void configureColorMatcher() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.setConfidenceThreshold(0.85);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.PRIMARY_TAB.getLayout("Sorter", BuiltInLayouts.kList).withSize(2, 3);
    layout.add("Sorter command", this);
    proximityNT = layout.add("Ball proximity", 0).getEntry();
    detectedColorNT = layout.add("Detected color", "").getEntry();

    detectedColorGraphNT = Constants.SORTER_TAB.add("DetectedColorGraph", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
    hits = Constants.SORTER_TAB.add("Hit", 0).getEntry();
    miss = Constants.SORTER_TAB.add("Miss", 0).getEntry();
  }

  public void Setsortercollectspeed(double speed) {
    sorterCollect.set(speed);
  }

  public void Setsorterspeed(double speed) {
    sorter.set(speed);
  }

  public boolean isMyAllianceColor(Color color) {
    return (color == kBlueTarget && Alliance.Blue == alliance)
        || (color == kRedTarget && Alliance.Red == alliance);
  }

  public boolean isOpposingAllianceColor(Color color) {
    return (color == kRedTarget && Alliance.Blue == alliance)
        || (color == kBlueTarget && Alliance.Red == alliance);
  }

  public Color matchColor() {
    Color detectedColor = m_colorSensor.getColor();
    ColorMatchResult colorMatchResults = m_colorMatcher.matchColor(detectedColor);
    if (colorMatchResults == null) {
      return new Color(0, 0, 0);
    } else {
      return colorMatchResults.color;
    }
  }

  public double getColorSensorProxmity() {
    return m_colorSensor.getProximity();
  }

  public void sendProximityToShuffleBoard(double proximity) {
    proximityNT.setNumber(proximity);
  }

  public void sendDetectedColorToShuffleBoard(Color color) {
    if (color == kRedTarget) {
      detectedColorNT.setString("Red");
      detectedColorGraphNT.setNumber(1);
    } else if (color == kBlueTarget) {
      detectedColorNT.setString("Blue");
      detectedColorGraphNT.setNumber(-1);
    } else {
      detectedColorNT.setString("Null");
      detectedColorGraphNT.setNumber(0);
    }
  }

  @Override
  public void periodic() {
    proximityNT.setNumber(getColorSensorProxmity());
  }
}
