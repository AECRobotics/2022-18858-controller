package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.HashMap;

public abstract class DriveBase {
    Wheel fr,br,fl,bl;
    CHubIMU imu;

    private DriveBaseTask previousTask;
    private DriveBaseTask currentTask = new DriveBaseTask(DriveBaseTask.TaskType.PLACEHOLDER, new HashMap<String, Double>());
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

    public void turnMotorsDistance(double distance) {
        this.fr.turnWheelDistance(distance);
        this.fl.turnWheelDistance(distance);
        this.br.turnWheelDistance(distance);
        this.bl.turnWheelDistance(distance);
    }

    public boolean anyMotorBusy() {
        return this.fr.isBusy() || this.br.isBusy() || this.fl.isBusy() || this.bl.isBusy();
    }

    public boolean allMotorsNotBusy() {
        return !(this.fr.isBusy() || this.br.isBusy() || this.fl.isBusy() || this.bl.isBusy());
    }

    public boolean allMotorsReachedTarget() {
        boolean atTarget = (Math.abs(this.fr.getTargetPosition()-this.fr.getCurrentPosition()) < 10) &&
                (Math.abs(this.br.getTargetPosition()-this.br.getCurrentPosition()) < 10) &&
                (Math.abs(this.fl.getTargetPosition()-this.fl.getCurrentPosition()) < 10) &&
                (Math.abs(this.bl.getTargetPosition()-this.bl.getCurrentPosition()) < 10);
        return atTarget;
    }
}
