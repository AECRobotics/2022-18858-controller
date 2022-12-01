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

    public boolean isBusy() {
        return this.motor.isBusy();
    }
}
