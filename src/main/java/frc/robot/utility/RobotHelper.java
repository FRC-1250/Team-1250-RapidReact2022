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
            return distance / (gearRatio * tickRatio * (2 * Math.PI * radiusOfOutputWheel));
        } else {
            return distance / (gearRatio * tickRatio * (2 * Math.PI * radiusOfOutputWheel));
        }
    }

    public static double Deadband(double inputValue, double threshold) {
        if (inputValue >= +threshold || inputValue <= -threshold) {
            return inputValue;
        } else {
            return 0;
        }
    }

    public static double Limit(double inputValue, double threshold) {
        if (inputValue >= +threshold) {
            return +threshold;
        } else if (inputValue <= -threshold) {
            return -threshold;
        } else {
            return inputValue;
        }
    }
}
