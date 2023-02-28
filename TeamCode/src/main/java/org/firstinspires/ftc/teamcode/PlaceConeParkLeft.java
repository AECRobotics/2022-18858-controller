package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;

@TeleOp(name="New Autonomous Template", group="Robot")
@Disabled
public class PlaceConeParkLeft extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    @Override
    public void runOpMode() {
        this.internalInit();

        while(!opModeIsActive()) {
            this.internalInitLoop(); //MyBoyAutonomous.internalInitLoop() does not call telemetry.update()

            telemetry.update(); //hence why it is here
        }

        waitForStart();
        this.internalStart(); //sets coneState variable

        telemetry.addLine(coneState.name());
        telemetry.update();

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

        telemetry.addLine(coneState.name());
        telemetry.update();

        closeClaw();
        spoolMotor.setRetractedDistance(50);
        sleep(500);
        driveBase.strafe(0.5, 0.66);
        driveBase.forward(0.5, 1.17);
        driveBase.strafe(0.5, -0.3);
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