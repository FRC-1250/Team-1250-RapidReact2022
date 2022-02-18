package frc.robot.utility;

public class RobotHelper {

    /**
     * 
     * @param gearRatio           the ratio of the number of rotations of a driver
     *                            gear to the number of rotations of a driven gear
     *                            i.e 3 input and 1 output revs = 1/3
     * @param distance            the distance required to cover in inches
     * @param radiusOfOutputWheel size of the output wheel
     * @param invertOutput        negates the output value
     * @return The number of revolutions required for the input motor rotate the
     *         output wheel the specified distance
     */

    public static double ConvertInchesToMotorRevolutions(double gearRatio, double distance, double radiusOfOutputWheel,
            boolean invertOutput) {
        return ConvertInchesToMotorTicks(gearRatio, distance, radiusOfOutputWheel, 1.0, invertOutput);
    }

    /**
     * 
     * @param gearRatio           the ratio of the number of rotations of a driver
     *                            gear to the number of rotations of a driven gear
     *                            i.e 3 input and 1 output revs = 1/3
     * @param distance            the distance required to cover in inches
     * @param radiusOfOutputWheel size of the output wheel
     * @param tickRatio           the ratio of the number of rotations of a motor to
     *                            the number of ticks
     * @param invertOutput        negates the output value
     * @return The number of ticks required for the input motor rotate the output
     *         wheel the specified distance
     */
    public static double ConvertInchesToMotorTicks(double gearRatio, double distance, double radiusOfOutputWheel,
            double tickRatio, boolean invertOutput) {
        if (invertOutput) {
            return tickRatio * (distance / (gearRatio * (2 * Math.PI * radiusOfOutputWheel)));
        } else {
            return -tickRatio * (distance / (gearRatio * (2 * Math.PI * radiusOfOutputWheel)));
        }
    }

    /**
     * 
     * @param inputValue the value checked against the threshold to be returned
     * @param threshold  the threshold value that must be breached for the
     *                   inputValue be returned
     * @return 0 or the inputValue if it is above or below the provided threshold
     */
    public static double Deadband(double inputValue, double threshold) {
        if (inputValue >= +threshold || inputValue <= -threshold) {
            return inputValue;
        } else {
            return 0;
        }
    }

    /**
     * 
     * @param inputValue the value checked against the threshold to be returned
     * @param threshold  the threshold value that must be not breached for the
     *                   inputValue to be returned
     * @return the inputValue or the threshold if the inputValue is greater or less
     *         than the threshold
     */

    public static double Limit(double inputValue, double threshold) {
        if (inputValue >= +threshold) {
            return +threshold;
        } else if (inputValue <= -threshold) {
            return -threshold;
        } else {
            return inputValue;
        }
    }

    /**
     * 
     * @param upperSpeed the upper speed limit to use in the piecewise function,
     *                   between -1 and 1. If negative, it should be less than the
     *                   lower speed limit. If positive, it should greater than the
     *                   lower speed limit.
     * @param lowerSpeed the lower speed limit to use in the piecewise function,
     *                   between -1 and 1. If negative, it should be greater than
     *                   the upper speed limit. If positive, it should less than the
     *                   upper speed limit.
     * @param start      the value of the position where the motor or device is
     *                   starting (ticks, angle, rotations).
     * @param end        the value of the position where the motor or device needs
     *                   to be (ticks, angle,rotations).
     * @param current    the value of the position where the motor or device
     *                   currently is (ticks, angle, rotations).
     * @return a motor output of the upper speed limit that transitions into the
     *         lower speed limit through a linear ramp that is biased to negative
     *         being "forward". i.e. the upper speed will be
     *         used for the first 70% of the route, than a speed between the upper
     *         and lower speed limits between 71% to 89% and then from 90% to 100%
     *         the lower speed limit.
     */

    public static double piecewiseMotorController(double upperSpeed, double lowerSpeed, double start,
            double end, double current) {
        return piecewiseMotorController(upperSpeed, 0.7, lowerSpeed, 0.9, start, end, current);
    }

    private static double piecewiseMotorController(double upperSpeed, double rampStartPercent,
            double lowerSpeed, double rampEndPercent, double start, double end, double current) {
        double diffTotal = Math.abs(start - end);
        double progressPercent = Math.abs((start - current) / diffTotal);
        boolean isNegative = Math.signum(start - end) == -1;
        double speed = 0;

        if (progressPercent > rampStartPercent && progressPercent < rampEndPercent) {
            speed = linearRamp(upperSpeed, rampStartPercent, lowerSpeed, rampEndPercent, progressPercent);
        } else if (progressPercent >= rampEndPercent && progressPercent <= 100) {
            speed = lowerSpeed;
        } else if (progressPercent >= 0 && progressPercent <= rampStartPercent) {
            speed = upperSpeed;
        }

        // Robots forward = negative
        if (isNegative) {
            return -speed;
        } else {
            return speed;
        }
    }

    private static double linearRamp(double upperSpeed, double rampStartPercent, double lowerSpeed,
            double rampEndPercent, double progressPercent) {
        double slope = 0;
        double b = 0;
        double y = 0;

        slope = (upperSpeed - lowerSpeed) / (rampStartPercent - rampEndPercent);
        b = upperSpeed - (slope * rampStartPercent);
        y = slope * progressPercent + b;

        return y;
    }
}
