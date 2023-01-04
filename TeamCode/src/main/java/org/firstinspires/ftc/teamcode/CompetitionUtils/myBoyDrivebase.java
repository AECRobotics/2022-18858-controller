package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.HolonomicDriveBase;
import org.firstinspires.ftc.teamcode.TeamUtils.Wheel;

public class myBoyDrivebase extends HolonomicDriveBase {
    public static double WHEEL_RADIUS = 48; //mm
    public static double GEAR_RATIO = 20;
    public static int TICKS_PER_RAW_REV = 28;
    public myBoyDrivebase(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, CHubIMU imu) {
        super(
                new Wheel(fr, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(br, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(fl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(bl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
        imu);
    }
}
