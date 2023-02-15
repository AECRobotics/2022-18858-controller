package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.HolonomicDriveBase;
import org.firstinspires.ftc.teamcode.TeamUtils.Wheel;

public class myBoyDrivebase extends HolonomicDriveBase {
    public static double WHEEL_RADIUS = 48; //mm
    public static double GEAR_RATIO = 20;
    public static int TICKS_PER_RAW_REV = 28;
    public myBoyDrivebase(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, BNO055IMU imu) {
        super(
                new Wheel(fr, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(br, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(fl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(bl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, Math.PI/2.0),
                new CHubIMU(imu, AxesOrder.XYZ)
        );
        this.getBl().setDirection(DcMotorSimple.Direction.REVERSE);
        this.getFl().setDirection(DcMotorSimple.Direction.REVERSE);
        this.getBr().setDirection(DcMotorSimple.Direction.FORWARD);
        this.getFr().setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public myBoyDrivebase(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, CHubIMU imu) {
        super(
                new Wheel(fr, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(br, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(fl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, 0.0),
                new Wheel(bl, WHEEL_RADIUS, GEAR_RATIO, TICKS_PER_RAW_REV, Math.PI/2),
                imu
        );
        this.getBl().setDirection(DcMotorSimple.Direction.REVERSE);
        this.getFl().setDirection(DcMotorSimple.Direction.REVERSE);
        this.getBr().setDirection(DcMotorSimple.Direction.FORWARD);
        this.getFr().setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
