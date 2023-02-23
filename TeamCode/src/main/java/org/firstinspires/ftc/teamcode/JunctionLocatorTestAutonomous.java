package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;

public class JunctionLocatorTestAutonomous extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    double angleTarget = 50;
    double widthTarget = 40;

    private boolean withinTolerance(double value, double target, double tolerance) {
        return Math.abs(value-target) <= tolerance;
    }

    private double clamp(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void alignToJunction() {
        double angle = 0.0;
        double width = 0.0;
        do {
            angle = this.webcam.getAngle();
            width = this.webcam.getWidth();
            double angleDiff = angle-angleTarget;
            double widthDiff = width-widthTarget;
            angleDiff = clamp(-0.5, angleDiff/10, 0.5);
            widthDiff = clamp(-0.5, angleDiff/10, 0.5);
            driveBase.drive(widthDiff, angleDiff, 0.0);
        } while (withinTolerance(angle, 50, 10) && withinTolerance(width, 40, 5));
        driveBase.setMotorPower(0.0);
    }

    @Override
    public void runOpMode() {
        this.internalInit();

        waitForStart();
        this.internalStart();

        coneState = this.webcam.getConeState();
        this.webcam.switchToJunctionLocatorMode();

        closeClaw();
        spoolMotor.setRetractedDistance(50);
        sleep(500);
        driveBase.strafe(0.5, 0.66);
        driveBase.forward(0.5, 1.17);
        driveBase.strafe(0.5, -0.3);
        spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT);
        sleep(2500);
        alignToJunction();
        openClaw();
    }
}
