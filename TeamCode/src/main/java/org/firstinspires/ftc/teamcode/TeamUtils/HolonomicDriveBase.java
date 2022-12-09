package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HolonomicDriveBase extends DriveBase {
    public HolonomicDriveBase(Wheel fr, Wheel br, Wheel fl, Wheel bl, CHubIMU imu) {
        super(fr,br,fl,bl,imu);
    }

    public Telemetry telemetry = null;

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
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
        if(this.getTask().getState() == DriveBaseTask.TaskState.EXISTING) {
            return false;
        } else if(this.getTask().getState() == DriveBaseTask.TaskState.FINISHED) {
            return true;
        } else {
            boolean output = false;
            switch(this.getTask().getTaskType()) {
                case DRIVE_TO_POSITION:
                    output = this.allMotorsReachedTarget() && this.allMotorsNotBusy();
                    break;
                case STRAFE_TO_POSITION:
                    output = this.allMotorsReachedTarget() && this.allMotorsNotBusy();
                    break;
                case PLACEHOLDER:
                    output = true;
                    break;
                default:
                    throw new RuntimeException("Task type not implemented in this drivebase");
            }
            if(output) {
                this.getTask().finishTask();
            }
            return output;
        }
    }

    public void forward() {
        double distanceInMeters = this.getTask().getParameters().get("meters");
        double power = Math.abs(this.getTask().getParameters().get("speed"));
        power = Math.abs(distanceInMeters)*power/distanceInMeters;
        this.setMotorPower(power);
        this.turnMotorsDistance(distanceInMeters);
    }

    public void strafe() {
        double distanceInMeters = this.getTask().getParameters().get("meters");
        double power = Math.abs(this.getTask().getParameters().get("speed"));
        power = Math.abs(distanceInMeters)*power/distanceInMeters;
        this.fl.setPower(power);
        this.br.setPower(power);
        this.fr.setPower(-power);
        this.bl.setPower(-power);
        this.turnMotorsDistance(distanceInMeters);
    }

    public void startTask() {
        //telemetry.addLine("debug2");
        this.getTask().startTask();
        switch(this.getTask().getTaskType()) {
            case DRIVE_TO_POSITION:
                break;
            case STRAFE_TO_POSITION:
                break;
            case PLACEHOLDER:
                break;
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }

    public void doTask() {
        switch(this.getTask().getTaskType()) {
            case DRIVE_TO_POSITION:
                this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
                this.forward();
                break;
            case STRAFE_TO_POSITION:
                this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
                this.strafe();
                break;
            case PLACEHOLDER:
                break;
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }

    @Override
    public void doTasks() {
        if(this.getTask().getState() == DriveBaseTask.TaskState.EXISTING) {
            //telemetry.addLine("debug1");
            startTask();
        } else if(this.getTask().getState() == DriveBaseTask.TaskState.STARTED) {
            doTask();
        } else if(this.getTask().getState() == DriveBaseTask.TaskState.FINISHED) {
            return;
        }
        return;
    }
}
