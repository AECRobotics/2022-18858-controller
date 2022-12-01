package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;

public class HolonomicDriveBase extends DriveBase {
    public HolonomicDriveBase(Wheel fr, Wheel br, Wheel fl, Wheel bl, CHubIMU imu) {
        super(fr,br,fl,bl,imu);
    }

    public void drive(double speed, double angle, double turn) {
        double powabr = (Math.sin(angle+(Math.PI/4.0))*speed);
        double powabl = (Math.sin(angle-(Math.PI/4.0))*speed);
        double divisor = Math.max(Math.abs(powabr+turn), Math.abs(powabl+turn));
        divisor = Math.max(divisor, 1.0);
        powabr/=divisor;
        powabl/=divisor;
        if(Math.abs(turn) <= 0.0) {
            divisor = Math.max(Math.abs(powabr), Math.abs(powabl));
            powabr/=divisor;
            powabl/=divisor;
            powabr*=speed;
            powabl*=speed;
        }
        this.fr.getMotor().setPower(powabl+turn);
        this.bl.getMotor().setPower(powabl-turn);

        this.fl.getMotor().setPower(-(powabr+turn));
        this.br.getMotor().setPower(-(powabr-turn));
    }

    @Override
    public boolean isTaskComplete() {
        switch(this.getTask().getTaskType()) {
            case DRIVE_TO_POSITION:

            case STRAFE_TO_POSITION:
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }

    @Override
    public void doTask() {
        switch(this.getTask().getTaskType()) {
            case DRIVE_TO_POSITION:

            case STRAFE_TO_POSITION:
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }
}
