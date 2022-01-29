// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LEDS extends SubsystemBase {

  public enum AnimationTypes {
    ColorFlow,
    Fire,
    Larson,
    Rainbow,
    RgbFade,
    SingleFade,
    Strobe,
    Twinkle,
    TwinkleOff,
    SetAll
  }

  private final CANdle candle = new CANdle(Constants.CANDLE_CAN_ID);
  private Animation animation = null;

  public LEDS() {
    CANdleConfiguration configAll = new CANdleConfiguration();
    configAll.statusLedOffWhenActive = true;
    configAll.disableWhenLOS = false;
    configAll.stripType = LEDStripType.GRB;
    configAll.brightnessScalar = 0.1;
    configAll.vBatOutputMode = VBatOutputMode.Modulated;
    candle.configAllSettings(configAll, 100);
  }

  public void changeAnimation(AnimationTypes toChange) {
    switch (toChange) {
      case ColorFlow:
        animation = new ColorFlowAnimation(128, 20, 70, 0, 0.7, Constants.CANDLE_LED_COUNT, Direction.Forward);
        break;
      case Fire:
        animation = new FireAnimation(0.5, 0.7, Constants.CANDLE_LED_COUNT, 0.7, 0.5);
        break;
      case Larson:
        animation = new LarsonAnimation(0, 255, 46, 0, 1, Constants.CANDLE_LED_COUNT, BounceMode.Front, 3);
        break;
      case Rainbow:
        animation = new RainbowAnimation(1, 0.1, Constants.CANDLE_LED_COUNT);
        break;
      case RgbFade:
        animation = new RgbFadeAnimation(0.7, 0.4, Constants.CANDLE_LED_COUNT);
        break;
      case SingleFade:
        animation = new SingleFadeAnimation(50, 2, 200, 0, 0.5, Constants.CANDLE_LED_COUNT);
        break;
      case Strobe:
        animation = new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, Constants.CANDLE_LED_COUNT);
        break;
      case Twinkle:
        animation = new TwinkleAnimation(30, 70, 60, 0, 0.4, Constants.CANDLE_LED_COUNT, TwinklePercent.Percent6);
        break;
      case TwinkleOff:
        animation = new TwinkleOffAnimation(70, 90, 175, 0, 0.8, Constants.CANDLE_LED_COUNT,
            TwinkleOffPercent.Percent100);
        break;
      case SetAll:
        animation = null;
        break;
    }
  }

  @Override
  public void periodic() {
    candle.animate(animation);
  }
}