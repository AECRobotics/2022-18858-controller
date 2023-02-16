package org.firstinspires.ftc.teamcode.TeamUtils.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Wheel extends Motor {
    private final double radius;
    private final double circumference;

    public Wheel(DcMotor motor, double radius, double gearRatio, double ticksPerRawRev, double maximumBacklash) {
        super(motor, gearRatio, ticksPerRawRev, maximumBacklash);
        this.radius = radius;
        this.circumference = 2*Math.PI*radius;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getCircumference() {
        return this.circumference;
    }

    public int convertMToEncoderTicks(double dist) {
        return this.convertMMToEncoderTicks(dist*1000);
    }

    public int convertMMToEncoderTicks(double dist) {
        return (int)(((dist*super.getTicksPerRev())/this.circumference)+0.5);
    }

    public double convertRotationsToEncoderTicks(double rotations) {
        return rotations*super.getTicksPerRev();
    }

    public void addToWheelTarget(int ticks) {
        this.setTargetPosition(this.getTargetPosition()+ticks);
    }

    public void turnWheelDistance(double distance) {
        this.turnWheelDistance(distance, this.getTargetPosition());
    }

    public void turnWheelDistance(double distance, int from) {
        this.turnWheelTicks(convertMToEncoderTicks(distance), from);
    }

    public void turnWheelTicks(int ticks, int from) {
        this.setTargetPosition(from+ticks);
    }
}
