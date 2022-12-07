package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

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

    public int getTaskCount() {
        return this.taskCount;
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

    public abstract void doTasks();

    public void setMotorModes(DcMotor.RunMode mode) {
        this.fr.setMode(mode);
        this.fl.setMode(mode);
        this.br.setMode(mode);
        this.bl.setMode(mode);
    }

    public void setMotorPower(double power) {
        this.fr.setPower(power);
        this.fl.setPower(power);
        this.br.setPower(power);
        this.bl.setPower(power);
    }

    public void setMotorTargets(int target) {
        this.fr.setTargetPosition(target);
        this.fl.setTargetPosition(target);
        this.br.setTargetPosition(target);
        this.bl.setTargetPosition(target);
    }

    public void addToMotorTargets(int ticks) {
        this.fr.setTargetPosition(this.fr.getTargetPosition()+ticks);
        this.fl.setTargetPosition(this.fl.getTargetPosition()+ticks);
        this.br.setTargetPosition(this.br.getTargetPosition()+ticks);
        this.bl.setTargetPosition(this.bl.getTargetPosition()+ticks);
    }

    public void turnMotorsDistance(double distance) {
        this.fr.setTargetPosition(this.fr.getTargetPosition()+this.fr.convertMToEncoderTicks(distance));
        this.fl.setTargetPosition(this.fl.getTargetPosition()+this.fl.convertMToEncoderTicks(distance));
        this.br.setTargetPosition(this.br.getTargetPosition()+this.br.convertMToEncoderTicks(distance));
        this.bl.setTargetPosition(this.bl.getTargetPosition()+this.bl.convertMToEncoderTicks(distance));
    }

    public boolean anyMotorBusy() {
        return this.fr.isBusy() || this.br.isBusy() || this.fl.isBusy() || this.bl.isBusy();
    }

    public boolean allMotorsReachedTarget() {
        return !this.fr.isBusy() && !this.br.isBusy() && !this.fl.isBusy() && !this.bl.isBusy();
    }

}
