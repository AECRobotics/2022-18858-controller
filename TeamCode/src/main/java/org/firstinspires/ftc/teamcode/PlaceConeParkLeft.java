package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;

public class PlaceConeParkLeft extends MyBoyAutonomous {
    private ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

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

        closeClaw();
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
        spoolMotor.setRetractedDistance(ArmHeightPositions.LOW_PLACEMENT);
        sleep(1000);
        alignToJunction();
        openClaw();
        sleep(500);
        driveBase.forward(0.5, -0.14);
        driveBase.turnToHeading(0.5, 0.0);
        driveBase.strafe(0.5,
                (coneState == ConeStateFinder.ConeState.MIDDLE) ? -0.365 :
                        (coneState == ConeStateFinder.ConeState.LEFT) ? -1.0005 :
                                (coneState == ConeStateFinder.ConeState.RIGHT ? 0.32285 : -0.365)
        );
    }
}
