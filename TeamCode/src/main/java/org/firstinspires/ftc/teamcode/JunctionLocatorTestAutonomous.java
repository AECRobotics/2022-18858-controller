package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Vector2;

@TeleOp(name="Test junction locator Autonomous", group="Robot")
public class JunctionLocatorTestAutonomous extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    double angleTarget = 90;
    double widthTarget = 152;
    double alignmentSpeed = 0.2;
    double webcamAngle = 33.557;

    private boolean withinTolerance(double value, double target, double tolerance) {
        return Math.abs(value-target) <= tolerance;
    }

    private double clamp(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void alignToJunction() {
        driveBase.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        double angle = 0.0;
        double width = 0.0;
        do {
            angle = this.webcam.getAngle();
            width = this.webcam.getWidth();
            telemetry.addLine(angle + ", " + width);
            double angleDiff = angle-angleTarget;
            double widthDiff = width-widthTarget;
            Vector2 diff = new Vector2(angleDiff, widthDiff);
            double diffMag = diff.magnitude();
            double diffAngle = diff.angle();
            telemetry.addLine(diffAngle + ", " + diffMag);
            diffAngle-=((90-webcamAngle)*Math.PI/180);
            Vector2 rotated = new Vector2(diffAngle);
            rotated = rotated.multiply(-Math.min(diffMag*alignmentSpeed/400, alignmentSpeed));
            //rotated.y*=(-1);
            //diff = diff.normalized();
            //Vector2 rotated = new Vector2(-webcamAngle*Math.PI/180);
            //rotated = rotated.multiply(rotated.dot(diff)).multiply(Math.min(mag/10, alignmentSpeed));

            //telemetry.addLine(rotated.x + ", " + rotated.y);
            
            //angleDiff = clamp(-alignmentSpeed, rotated.x/10, alignmentSpeed);
            //widthDiff = clamp(-alignmentSpeed, rotated.y/10, alignmentSpeed);
            telemetry.addLine(rotated.y + ", " + rotated.x);
            telemetry.update();
            driveBase.drive(rotated.y, rotated.x, 0.0);
        } while (!withinTolerance(angle, angleTarget, 10) || !withinTolerance(width, widthTarget, 5));
        driveBase.setMotorPower(0.0);
    }

    @Override
    public void runOpMode() {
        this.internalInit();

        waitForStart();
        this.internalStart();

        coneState = this.webcam.getConeState();
        this.webcam.switchToJunctionLocatorMode();

        /*closeClaw();
        spoolMotor.setRetractedDistance(50);
        sleep(500);
        telemetry.addLine("thing-1");
        telemetry.update();
        driveBase.strafe(0.5, 0.66);
        telemetry.addLine("thing");
        telemetry.update();
        driveBase.forward(0.5, 1.17);
        telemetry.addLine("thing2");
        telemetry.update();
        driveBase.strafe(0.5, -0.3);
        telemetry.addLine("thing3");
        telemetry.update();
        spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT);
        sleep(2500);*/
        alignToJunction();
        openClaw();
    }
}
