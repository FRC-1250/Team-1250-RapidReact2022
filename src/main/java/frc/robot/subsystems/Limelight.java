// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

  private final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

  /*
   * tv Whether the limelight has any valid targets (0 or 1)
   * tx Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
   * ty Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
   * ta Target Area (0% of image to 100% of image)
   */
  private double tx, ty, tv, ta = -1;

  public Limelight() {
    setLEDMode(1);
  }

  public double getXOffset() {
    return tx;
  }

  public double getYOffset() {
    return ty;
  }

  public double getTargetAreaPercent() {
    return ta;
  }

  public void setLEDMode(int value) {
    if (value > 3 || value < 0)
      value = 0;
    table.getEntry("ledMode").setNumber(value);
  }

  public boolean isTargetSeen() {
    if (tv == 1) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void periodic() {
    tx = table.getEntry("tx").getDouble(-1);
    ty = table.getEntry("ty").getDouble(-1);
    tv = table.getEntry("tv").getDouble(-1);
  }
}
