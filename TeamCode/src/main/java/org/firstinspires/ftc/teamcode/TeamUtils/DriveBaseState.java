package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveBaseState {
    public int frTick, brTick, flTick, blTick;
    public int frTarget, brTarget, flTarget, blTarget;
    public double frPow, brPow, flPow, blPow;
    public long timeStamp;

    public double heading;

    public DriveBaseState(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, CHubIMU imu) {
        this.frTick = fr.getCurrentPosition();
        this.brTick = br.getCurrentPosition();
        this.flTick = fl.getCurrentPosition();
        this.blTick = bl.getCurrentPosition();
        this.frPow = fr.getPower();
        this.brPow = br.getPower();
        this.flPow = fl.getPower();
        this.blPow = bl.getPower();
        this.frTarget = fr.getTargetPosition();
        this.brTarget = br.getTargetPosition();
        this.flTarget = fl.getTargetPosition();
        this.blTarget = bl.getTargetPosition();
        this.heading = imu.getOrientation().firstAngle;
        this.timeStamp = System.nanoTime();
    }
}
