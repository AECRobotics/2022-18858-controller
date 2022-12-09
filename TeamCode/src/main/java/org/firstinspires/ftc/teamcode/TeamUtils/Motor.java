package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.hardware.DcMotor;

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
        this.getMotor().setMode(mode);
    }
    public void setPower(double power) {
        this.getMotor().setPower(power);
    }
    public void setTargetPosition(int target) {
        this.getMotor().setTargetPosition(target);
    }
    public int getTargetPosition() {
        return this.getMotor().getTargetPosition();
    }
    public int getCurrentPosition() { return this.getMotor().getCurrentPosition(); }
    public boolean isBusy() {
        return this.getMotor().isBusy();
    }
}
