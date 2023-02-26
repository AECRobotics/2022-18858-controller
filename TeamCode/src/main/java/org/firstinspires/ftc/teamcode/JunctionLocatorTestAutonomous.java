package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.Vector2;

@TeleOp(name="Test junction locator Autonomous", group="Robot")
public class JunctionLocatorTestAutonomous extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    double angleTarget = 305;//90;
    double widthTarget = 145;//152;
    double alignmentSpeed = 0.2;
    double webcamAngle = 46.225;//33.557;

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
            double angleDiff = angle-angleTarget;
            double widthDiff = width-widthTarget;
            Vector2 diff = new Vector2(angleDiff, widthDiff);
            double diffMag = diff.magnitude();
            double diffAngle = diff.angle();
            diffAngle-=((90-webcamAngle)*Math.PI/180);
            Vector2 rotated = new Vector2(diffAngle);
            rotated = rotated.multiply(Math.min(diffMag*alignmentSpeed/400, alignmentSpeed));
            //the /400 is because of reasons.
            //

            rotated.y*=(-1);
            //frankly this wouldn't be here if I did the math properly but I clearly didn't and it works right now.
            driveBase.drive(rotated.y, rotated.x, 0.0);
        } while (!withinTolerance(angle, angleTarget, 10) || !withinTolerance(width, widthTarget, 5));
        driveBase.setMotorPower(0.0);
    }

    @Override
    public void runOpMode() {
        this.internalInit();
        CHubIMU imu = driveBase.getImu();
        while(!opModeIsActive()) {
            telemetry.addLine("" + imu.getGyroCalibrationStatus().name());
            telemetry.update();
        }
        telemetry.addLine("thing");
        telemetry.update();

        waitForStart();
        this.internalStart();

        coneState = this.webcam.getConeState();
        this.webcam.switchToJunctionLocatorMode();
        telemetry.addLine(coneState.name());
        telemetry.update();

        /*closeClaw();
        spoolMotor.setRetractedDistance(50);
        sleep(500);
        //telemetry.addLine("thing-1");
        //telemetry.update();
        driveBase.strafe(0.5, 0.66);
        //telemetry.addLine("thing");
        //telemetry.update();
        driveBase.forward(0.5, 1.17);
        //telemetry.addLine("thing2");
        //telemetry.update();
        driveBase.strafe(0.5, -0.3);
        //telemetry.addLine("thing3");
        //telemetry.update();
        spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT);
        sleep(1000);
        alignToJunction();
        openClaw();
        driveBase.forward(0.5, -0.14);*/
        driveBase.turnToHeading(0.5, 0.0);
        /*driveBase.strafe(0.5,
                (coneState == ConeStateFinder.ConeState.MIDDLE) ? -0.365 :
                        (coneState == ConeStateFinder.ConeState.LEFT) ? -1.0005 :
                                (coneState == ConeStateFinder.ConeState.RIGHT ? 0.32285 : -0.365)
        );*/
    }
}
