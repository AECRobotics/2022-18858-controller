package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Spool extends Motor {
    private double spoolInnerRadius;
    private double spoolOuterRadius;
    private double spoolInnerWidth;
    private double packingFactor;
    private double threadDiameter;

    public Spool(DcMotor motor, double gearRatio, double ticksPerRawRev, double maximumBacklash, double spoolInnerRadius, double spoolInnerWidth, double packingFactor, double threadDiameter) {
        super(motor, gearRatio, ticksPerRawRev, maximumBacklash);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.spoolInnerRadius = spoolInnerRadius;
        this.spoolInnerWidth = spoolInnerWidth;
        this.packingFactor = packingFactor;
        this.threadDiameter = threadDiameter;
    }

    public int convertMMToEncoderPosition(double dist) {
        //d diameter of thread
        //f packing factor
        //r_i inner radius
        //r_o outer radius
        //W width of spool
        //L distance to take in
        //X distance of thread already taken in
        //formula comes from https://math.stackexchange.com/questions/2817776/how-to-determine-number-of-turns-needed-to-fill-spool-with-known-length-of-wire
        double x = 0.0;
        double rotations = dist/(Math.PI*(this.spoolInnerRadius+Math.sqrt(
                (this.spoolInnerRadius*this.spoolInnerRadius)+((x*this.threadDiameter*this.threadDiameter)/(4.0*this.spoolInnerWidth*packingFactor))
        )));
        return (int)(convertRotationsToEncoderPosition(rotations)+0.5);
    }

    public double convertRotationsToEncoderPosition(double rotations) {
        return rotations*super.getTicksPerRev();
    }

    public void setRetractedDistance(double mm) {
        this.setTargetPosition(this.convertMMToEncoderPosition(mm));
    }
}
