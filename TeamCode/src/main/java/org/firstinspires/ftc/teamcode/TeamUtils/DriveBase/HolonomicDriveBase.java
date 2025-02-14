package org.firstinspires.ftc.teamcode.TeamUtils.DriveBase;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.UnitConversion;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Wheel;

public class HolonomicDriveBase extends DriveBase {
    public HolonomicDriveBase(Wheel fr, Wheel br, Wheel fl, Wheel bl, CHubIMU imu) {
        super(fr,br,fl,bl,imu);
    }

    public Telemetry telemetry = null;

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void drive(double forward, double strafe, double turn) {
        //+forward is forward, -forward is backward
        //+stafe is right, -stafe is left,
        //+turn is clockwise, -turn is counterclockwise




        /*double powabr = (Math.sin(angle+(Math.PI/4.0))*speed);
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
        this.br.getMotor().setPower(-(powabr-turn));*/

        double lbPow = forward - strafe + turn;
        double rbPow = forward + strafe - turn;
        double lfPow = forward + strafe + turn;
        double rfPow = forward - strafe - turn;
        double divisor = Math.max(Math.max(Math.abs(lfPow), Math.abs(lbPow)), Math.max(Math.abs(rfPow), Math.abs(rbPow)));
        if(divisor > 1.0)
        {
            lbPow/=divisor;
            rbPow/=divisor;
            lfPow/=divisor;
            rfPow/=divisor;
        }
        this.bl.setPower(lbPow);
        this.br.setPower(rbPow);
        this.fl.setPower(lfPow);
        this.fr.setPower(rfPow);
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
                case DRIVE_DISTANCE:
                    output = this.allMotorsReachedTarget() && this.numberOfMotorsBusy() <= 2;
                    break;
                case STRAFE_DISTANCE:
                    output = this.allMotorsReachedTarget() && this.numberOfMotorsBusy() <= 2;
                    break;
                case WAIT_FOR:
                    output = (this.stateAtAssignmentOfTask.timeStamp+this.getTask().getParameters().get("seconds")* UnitConversion.SECONDS_PER_NANOSECOND) <= System.nanoTime();
                    break;
                case TURN_DEGREES:
                    output = (Math.abs(this.distanceToTurn()) <= 0.1);
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

    public void forward(double power, double distance) {
        //System.out.println("debug2");
        this.resetCurrentToTarget();
        this.stateAtAssignmentOfTask = this.getDriveBaseState();
        this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
        this.fl.setPower(power);
        this.bl.setPower(power);
        this.fr.setPower(power);
        this.br.setPower(power);
        this.fl.turnWheelDistance(distance, this.stateAtAssignmentOfTask.flTarget);
        this.bl.turnWheelDistance(distance, this.stateAtAssignmentOfTask.blTarget);
        this.fr.turnWheelDistance(distance, this.stateAtAssignmentOfTask.frTarget);
        this.br.turnWheelDistance(distance, this.stateAtAssignmentOfTask.brTarget);
        //System.out.println("debug3");
        while(!this.allMotorsReachedTarget() || this.numberOfMotorsBusy() >= 2) {
            //System.out.println("fl" + this.fl.getTargetPosition() + ", " + this.fl.getCurrentPosition() + "," + this.stateAtAssignmentOfTask.flTarget);
            //System.out.println("bl" + this.bl.getTargetPosition() + ", " + this.bl.getCurrentPosition() + "," + this.stateAtAssignmentOfTask.blTarget);
            //System.out.println((this.allMotorsReachedTarget() ? "true" : "false") + " " + this.numberOfMotorsBusy());
            //System.out.println("debug4");
            try {
                sleep(2);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void strafe(double power, double distance) {
        this.resetCurrentToTarget();
        this.stateAtAssignmentOfTask = this.getDriveBaseState();
        this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
        this.fl.setPower(power);
        this.br.setPower(power);
        this.fr.setPower(power);
        this.bl.setPower(power);
        this.fl.turnWheelDistance(distance, this.stateAtAssignmentOfTask.flTarget);
        this.br.turnWheelDistance(distance, this.stateAtAssignmentOfTask.brTarget);
        this.fr.turnWheelDistance(-distance, this.stateAtAssignmentOfTask.frTarget);
        this.bl.turnWheelDistance(-distance, this.stateAtAssignmentOfTask.blTarget);
        while(!this.allMotorsReachedTarget() || this.numberOfMotorsBusy() >= 2) {
            //System.out.println("fl" + this.fl.getTargetPosition() + ", " + this.fl.getCurrentPosition() + "," + this.stateAtAssignmentOfTask.flTarget);
            //System.out.println("bl" + this.bl.getTargetPosition() + ", " + this.bl.getCurrentPosition() + "," + this.stateAtAssignmentOfTask.blTarget);
            try {
                sleep(2);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void turn(double power, double degrees) {
        this.stateAtAssignmentOfTask = this.getDriveBaseState();
        this.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        double startHeading = this.getHeading();
        double diff = this.distanceToTurn(degrees);
        while(Math.abs(diff) > 0.1) {
            //double startHeading = this.getHeading();
            diff = this.distanceToTurn(degrees);
            if(Math.abs(diff) <= 5) {
                power = 0.05;
            } else if(Math.abs(diff) <= 15) {
                power = 0.1;
            }
            power = Math.signum(diff)*power;
            //telemetry.addData("debug",String.format("%.5f, %.5f, %.5f, %.5f", destination, diff, speed, current));
            //telemetry.addData("debug2", String.format("%.5f, %.5f, %.5f", diff, DriveBase.PIDishThingMultiplier, this.task.getSpeed()));
            this.fr.setPower(power);
            this.br.setPower(power);
            this.fl.setPower(-power);
            this.bl.setPower(-power);
            try {
                sleep(2);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        setMotorPower(0.0);
    }

    public void turnToHeading(double power, double heading) {
        this.stateAtAssignmentOfTask = this.getDriveBaseState();
        double diff = this.distanceToTurn(this.getHeading(), heading);
        //System.out.println("==========================");
        while(Math.abs(diff) > 0.1) {
            //double startHeading = this.getHeading();
            //System.out.println(diff + "");
            diff = this.distanceToTurn(this.getHeading(), heading);
            if(Math.abs(diff) <= 5) {
                power = 0.05;
            } else if(Math.abs(diff) <= 15) {
                power = 0.1;
            }
            power = Math.signum(diff)*power;
            //telemetry.addData("debug",String.format("%.5f, %.5f, %.5f, %.5f", destination, diff, speed, current));
            //telemetry.addData("debug2", String.format("%.5f, %.5f, %.5f", diff, DriveBase.PIDishThingMultiplier, this.task.getSpeed()));
            this.fr.setPower(power);
            this.br.setPower(power);
            this.fl.setPower(-power);
            this.bl.setPower(-power);
            try {
                sleep(2);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        setMotorPower(0.0);
    }

    public void forward() {
        double distanceInMeters = this.getTask().getParameters().get("meters");
        double power = this.getTask().getParameters().get("speed");
        this.fl.setPower(power);
        this.bl.setPower(power);
        this.fr.setPower(power);
        this.br.setPower(power);
        this.fl.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.flTarget);
        this.bl.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.blTarget);
        this.fr.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.frTarget);
        this.br.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.brTarget);
    }

    public void strafe() {
        double distanceInMeters = this.getTask().getParameters().get("meters");
        //+distance right, -distance left
        double power = Math.abs(this.getTask().getParameters().get("speed"));
        this.fl.setPower(power);
        this.br.setPower(power);
        this.fr.setPower(power);
        this.bl.setPower(power);
        this.fl.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.flTarget);
        this.br.turnWheelDistance(distanceInMeters, this.stateAtAssignmentOfTask.brTarget);
        this.fr.turnWheelDistance(-distanceInMeters, this.stateAtAssignmentOfTask.frTarget);
        this.bl.turnWheelDistance(-distanceInMeters, this.stateAtAssignmentOfTask.blTarget);
    }

    public double distanceToTurn() { //this function has absolutely no error handling, if you call it when driving instead of turning it will absolutely return invalid information and especially if you call it without setting a task first
        double current = this.getHeading();
        double destination = (this.stateAtAssignmentOfTask.heading+(180-this.getTask().getParameters().get("degrees")));
        return this.distanceToTurn(current, destination);
    }

    public double distanceToTurn(double degrees) {
        return this.distanceToTurn(this.getHeading(), this.getHeading()+(180-degrees));
    }

    public double distanceToTurn(double cHeading, double nHeading) { //this function has absolutely no error handling, if you call it when driving instead of turning it will absolutely return invalid information and especially if you call it without setting a task first
        double current = this.getHeading();
        double destination = 180-nHeading;//(this.stateAtAssignmentOfTask.heading+(180-this.getTask().getParameters().get("degrees")));
        destination%=(2*180);
        destination-=180;
        destination-=current;
        //current-=current;
        double diff = destination;//(-EulerAngle.quaternionToEuler(this.imu.getQuaternionOrientation()).y)-destination; // pi
        double sign = diff/Math.abs(diff);

        double diff1 = Math.abs(diff);
        double diff2 = Math.abs((2*180)-Math.abs(diff));
        if(diff2 < diff1) {
            diff = diff2*sign*-1;
        }
        //diff%=Math.PI;
        //diff*=sign;
        return diff;
    }

    public void turn() {
        double speed = this.getTask().getParameters().get("speed");
        double diff = this.distanceToTurn();
        if(Math.abs(diff) <= 5) {
            speed = 0.05;
        } else if(Math.abs(diff) <= 15) {
            speed = 0.1;
        }
        speed = Math.signum(diff)*speed;
        //telemetry.addData("debug",String.format("%.5f, %.5f, %.5f, %.5f", destination, diff, speed, current));
        //telemetry.addData("debug2", String.format("%.5f, %.5f, %.5f", diff, DriveBase.PIDishThingMultiplier, this.task.getSpeed()));
        this.fr.setPower(speed);
        this.br.setPower(speed);
        this.fl.setPower(-speed);
        this.bl.setPower(-speed);

    }

    private void startTask() {
        //telemetry.addLine("debug2");
        this.getTask().startTask();
        switch(this.getTask().getTaskType()) {
            case DRIVE_DISTANCE:
                this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
                this.forward();
                break;
            case STRAFE_DISTANCE:
                this.setMotorModes(DcMotor.RunMode.RUN_TO_POSITION);
                this.strafe();
                break;
            case WAIT_FOR:
                break;
            case TURN_DEGREES:
                this.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
            case PLACEHOLDER:
                break;
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }

    private void doTask() {
        switch(this.getTask().getTaskType()) {
            case DRIVE_DISTANCE:
                break;
            case STRAFE_DISTANCE:
                break;
            case WAIT_FOR:
                break;
            case TURN_DEGREES:
                this.turn();
                break;
            case PLACEHOLDER:
                break;
            default:
                throw new RuntimeException("Task type not implemented in this drivebase");
        }
    }

    private void finishTask() {
        switch(this.getTask().getTaskType()) {
            case DRIVE_DISTANCE:
                break;
            case STRAFE_DISTANCE:
                break;
            case WAIT_FOR:
                break;
            case TURN_DEGREES:
                this.setMotorPower(0.0);
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
            finishTask();
        }
        return;
    }
}
