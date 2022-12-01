package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public abstract class DriveBase {
    Wheel fr,br,fl,bl;
    CHubIMU imu;

    private DriveBaseTask previousTask;
    private DriveBaseTask currentTask;
    private int taskCount = 0;
    private boolean taskComplete = true;
    private DriveBaseState stateAtAssignmentOfTask;
    //private double


    public DriveBase(Wheel fr, Wheel br, Wheel fl, Wheel bl, CHubIMU imu) {
        this.fr = fr;
        this.br = br;
        this.fl = fl;
        this.bl = bl;
        this.imu = imu;
    }

    public DriveBaseState getDriveBaseState() {
        return new DriveBaseState(this.fr.getMotor(), this.br.getMotor(), this.fl.getMotor(), this.bl.getMotor(), this.imu);
    }

    public void setTask(DriveBaseTask task) {
        this.stateAtAssignmentOfTask = this.getDriveBaseState();
        this.previousTask = this.currentTask;
        this.currentTask = task;
        this.taskCount++;
    }

    public DriveBaseTask getTask() {
        return this.currentTask;
    }

    public abstract boolean isTaskComplete();

    public abstract void doTask();

    public boolean anyMotorBusy() {
        return this.fr.isBusy() || this.br.isBusy() || this.fl.isBusy() || this.bl.isBusy();
    }

    public boolean allMotorsReachedTarget() {
        return !this.fr.isBusy() && !this.br.isBusy() && !this.fl.isBusy() && !this.bl.isBusy();
    }

}
