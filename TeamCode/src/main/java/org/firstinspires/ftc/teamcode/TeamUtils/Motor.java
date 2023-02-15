package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Motor {
    private final DcMotor motor;
    private final double gearRatio, ticksPerRawRev, maximumBacklash;
    private final double ticksPerRev;

    private int backlashCompensation;

    private int previousInternalTargetCurrentPositionDifference = 0;
    private int internalTarget = 0;
    public int internalRealPositionDifference = 0;

    public Motor(DcMotor motor, double gearRatio, double ticksPerRawRev, double maximumBacklash) {
        this.motor = motor;
        this.gearRatio = gearRatio;
        this.ticksPerRawRev = ticksPerRawRev;
        this.maximumBacklash = maximumBacklash;
        this.ticksPerRev = this.ticksPerRawRev * this.gearRatio;
        this.backlashCompensation = (int)(this.convertRadToEncoderTicks(maximumBacklash)+0.5);
        this.previousInternalTargetCurrentPositionDifference = this.backlashCompensation;
        this.motor.setTargetPosition(0);
        this.internalTarget = 0;
        this.internalRealPositionDifference = 0;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setDirection(DcMotor.Direction direction) {
        this.motor.setDirection(direction);
    }

    public DcMotor getMotor() {
        return motor;
    }

    public double getTicksPerRev() {
        return ticksPerRev;
    }

    public double convertRadToEncoderTicks(double rads) {
        return rads/(2*Math.PI)*this.ticksPerRev;
    }

    public void setMode(DcMotor.RunMode mode) {
        this.motor.setMode(mode);
    }
    public void setPower(double power) {
        this.motor.setPower(power);
    }

    public void setTargetPosition(int target) {
        this.internalTarget = target;
        int internalExternalTargetPositionDifference = (target-this.internalRealPositionDifference)-this.motor.getCurrentPosition();
        int previousSign = Integer.signum(this.previousInternalTargetCurrentPositionDifference);
        int currentSign = Integer.signum(internalExternalTargetPositionDifference);
        int realTarget;// = currentInternalTargetCurrentPositionDifference;
        if(previousSign == currentSign) {
            realTarget = target-this.internalRealPositionDifference;
        } else if(previousSign < currentSign) {
            realTarget = (target-this.internalRealPositionDifference)+this.backlashCompensation;
            this.internalRealPositionDifference = 0;//this.backlashCompensation;
        } else if(previousSign > currentSign) {
            realTarget = (target-this.internalRealPositionDifference)-this.backlashCompensation;
            this.internalRealPositionDifference = -this.backlashCompensation;
        } else {
            throw new Error("May God have mercy on you, for reality clearly isn't willing to");
        }
        this.previousInternalTargetCurrentPositionDifference = realTarget-this.getCurrentPosition();
        this.motor.setTargetPosition(realTarget);
    }

    public int getTargetPosition() {
        return this.motor.getTargetPosition()+this.internalRealPositionDifference;
    }

    public int getCurrentPosition() {
        return this.motor.getCurrentPosition()+this.internalRealPositionDifference;
    }

    public double getPower() {
        return this.motor.getPower();
    }
    public boolean isBusy() {
        return this.motor.isBusy();
    }

    public boolean reachedTarget() {
        return this.motor.getTargetPosition()-this.getCurrentPosition() == 0;
    }
}
