// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Sorter extends SubsystemBase {

  private CANSparkMax sorter = new CANSparkMax(Constants.sorter_CAN_ID, MotorType.kBrushless);
  private CANSparkMax lateralConveyor = new CANSparkMax(Constants.sorterCollect_CAN_ID, MotorType.kBrushless);

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private final Color kBlueTarget = new Color(0.16, 0.397, 0.45);
  private final Color kBlueLightTarget = new Color(new Color8Bit(48, 115, 130));
  private final Color kBlueDarkTarget = new Color(new Color8Bit(34, 82, 93));
  private final Color kRedTarget = new Color(0.517, 0.345, 0.138);
  private final Color kRedLightTarget = new Color(new Color8Bit(141, 94, 37));
  private final Color kRedDarkTarget = new Color(new Color8Bit(121, 81, 32));
  private final Alliance alliance;
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private NetworkTableEntry detectedColorGraphNT;
  private NetworkTableEntry detectedColorNT;
  private NetworkTableEntry proximityNT;
  private NetworkTableEntry rgb;

  /** Creates a new Sorter. */
  public Sorter() {
    alliance = DriverStation.getAlliance();
    sorter.setIdleMode(IdleMode.kBrake);
    lateralConveyor.setIdleMode(IdleMode.kBrake);

    configureColorMatcher();
    configureShuffleBoard();
    SystemMonitor.getInstance().registerDevice(sorter, "sort wheel");
    SystemMonitor.getInstance().registerDevice(lateralConveyor, "lateralConveyor");
  }

  private void configureColorMatcher() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kBlueLightTarget);
    m_colorMatcher.addColorMatch(kBlueDarkTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kRedLightTarget);
    m_colorMatcher.addColorMatch(kRedDarkTarget);
  }

  private void configureShuffleBoard() {
    ShuffleboardLayout layout = Constants.SYSTEM_MONITOR_TAB.getLayout("Intake-Sort", BuiltInLayouts.kList);
    layout.add(this);
    proximityNT = layout.add("Ball proximity", 0).getEntry();
    detectedColorNT = layout.add("Detected color", "").getEntry();
    detectedColorGraphNT = layout.add("Detected color graph", 0).withWidget(BuiltInWidgets.kGraph).getEntry();
    rgb = layout.add("RGB", "").getEntry();
  }

  public void setLateralConveyorSpeed(double speed) {
    lateralConveyor.set(speed);
  }

  public void setSortWheelSpeed(double speed) {
    sorter.set(speed);
  }

  public boolean isMyAllianceColor(Color color) {
    return (isBlue(color) && Alliance.Blue == alliance)
        || (isRed(color) && Alliance.Red == alliance);
  }

  public boolean isOpposingAllianceColor(Color color) {
    return (isRed(color) && Alliance.Blue == alliance)
        || (isBlue(color) && Alliance.Red == alliance);
  }

  public boolean isRed(Color color) {
    return color == kRedTarget || color == kRedLightTarget || color == kRedDarkTarget;
  }

  public boolean isBlue(Color color) {
    return color == kBlueTarget || color == kBlueDarkTarget || color == kBlueLightTarget;
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
    if (isRed(color)) {
      detectedColorNT.setString("Red");
      detectedColorGraphNT.setNumber(1);
    } else if (isBlue(color)) {
      detectedColorNT.setString("Blue");
      detectedColorGraphNT.setNumber(-1);
    } else {
      detectedColorNT.setString("Null");
      detectedColorGraphNT.setNumber(0);
    }
    rgb.setString(String.format("R: %s, G: %s, B: %s", color.red, color.green, color.blue));
  }

  @Override
  public void periodic() {
    proximityNT.setNumber(getColorSensorProxmity());
  }
}
