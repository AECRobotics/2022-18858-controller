package org.firstinspires.ftc.teamcode.TeamUtils.Imu;

public class DriveBaseState {
    public int frTick, brTick, flTick, blTick;
    public int frTarget, brTarget, flTarget, blTarget;
    public double frPow, brPow, flPow, blPow;
    public long timeStamp;

    public double heading;

    public DriveBaseState(DriveBase drive) {
        this.frTick = drive.fr.getCurrentPosition();
        this.brTick = drive.br.getCurrentPosition();
        this.flTick = drive.fl.getCurrentPosition();
        this.blTick = drive.bl.getCurrentPosition();
        this.frPow = drive.fr.getPower();
        this.brPow = drive.br.getPower();
        this.flPow = drive.fl.getPower();
        this.blPow = drive.bl.getPower();
        this.frTarget = drive.fr.getTargetPosition();
        this.brTarget = drive.br.getTargetPosition();
        this.flTarget = drive.fl.getTargetPosition();
        this.blTarget = drive.bl.getTargetPosition();
        this.heading = drive.getHeading();
        this.timeStamp = System.nanoTime();
    }
}
