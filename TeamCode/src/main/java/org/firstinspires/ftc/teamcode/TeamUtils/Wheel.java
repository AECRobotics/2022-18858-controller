package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Wheel extends Motor {
    private final double radius;
    private final double circumference;

    public Wheel(DcMotor motor, double radius, double gearRatio, double ticksPerRawRev, double maximumBacklash) {
        super(motor, gearRatio, ticksPerRawRev, maximumBacklash);
        this.radius = radius;
        this.circumference = 2*Math.PI*radius;
    }

    public double convertMMToEncoderTicks(double dist) {
        return (dist*super.getTicksPerRev())/this.circumference;
    }

    public double convertRotationsToEncoderTicks(double rotations) {
        return rotations*super.getTicksPerRev();
    }
}
