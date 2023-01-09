package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Motor {
    private final DcMotor motor;
    private final double gearRatio, ticksPerRawRev, maximumBacklash;
    private final double ticksPerRev;

    public Motor(DcMotor motor, double gearRatio, double ticksPerRawRev, double maximumBacklash) {
        this.motor = motor;
        this.gearRatio = gearRatio;
        this.ticksPerRawRev = ticksPerRawRev;
        this.maximumBacklash = maximumBacklash;
        this.ticksPerRev = this.ticksPerRawRev * this.gearRatio;
        this.motor.setTargetPosition(0);
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
        this.motor.setTargetPosition(target);
    }
    public int getTargetPosition() {
        return this.motor.getTargetPosition();
    }
    public int getCurrentPosition() { return this.motor.getCurrentPosition(); }
    public boolean isBusy() {
        return this.motor.isBusy();
    }
}
