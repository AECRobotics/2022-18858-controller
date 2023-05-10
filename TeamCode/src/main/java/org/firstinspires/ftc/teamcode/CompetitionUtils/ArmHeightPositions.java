package org.firstinspires.ftc.teamcode.CompetitionUtils;
 import com.qualcomm.robotcore.hardware.DcMotor;

 import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
 import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;

public class ArmHeightPositions {
    public static double COUNTS_PER_MM = (GoBildaSpoolConstants.TICKS_PER_REV)/(GoBildaSpoolConstants.SPOOL_CIRCUMFERENCE);
    public static double mmToTicks(double mm, DcMotor motor){
        double ticks = mm*COUNTS_PER_MM;
        if(ticks >= 0 && ticks <= MAXIMUM_TICKS) {
            return ticks;
        } else if (ticks > MAXIMUM_TICKS) {
            return MAXIMUM_TICKS;
        } else {
            return motor.getCurrentPosition();
        }
    }
    public static double mmToTicksSpool(double mm, Spool motor) {
        double ticks = mm * COUNTS_PER_MM;
        if (ticks >= 0 && ticks <= MAXIMUM_TICKS) {
            return ticks;
        } else if (ticks > MAXIMUM_TICKS) {
            return MAXIMUM_TICKS;
        } else {
            return motor.getCurrentPosition();
        }
    }


    public static int MAXIMUM_TICKS = 4055;
    public static double HIGH_PLACEMENT = 1180;
    public static double MEDIUM_PLACEMENT = 800;
    public static double LOW_PLACEMENT = 500;
    public static double GROUND_PLACEMENT = 0.0;
}
