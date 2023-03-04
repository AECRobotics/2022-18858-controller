package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;

@TeleOp(name="Place Cone and Park,  LEFT", group="Robot")
public class PlaceConeParkLeft extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    @Override
    public void runOpMode() {
        this.internalInit();

        while(opModeInInit()) {
            this.internalInitLoop(); //MyBoyAutonomous.internalInitLoop() does not call telemetry.update()


            telemetry.update();
        }

        waitForStart();
        this.internalStart(); //sets coneState variable

        telemetry.addLine(coneState.name());
        telemetry.update();

        closeClaw();
        spoolMotor.setRetractedDistance(50);
        sleep(500);
        driveBase.strafe(0.5, 0.66);
        driveBase.forward(0.5, 1.17);
        driveBase.strafe(0.5, -0.3);
        System.out.println("spoolMotor");
        spoolMotor.setRetractedDistance(ArmHeightPositions.LOW_PLACEMENT);
        System.out.println("sleep");
        sleep(1000);
        System.out.println("align");
        this.alignToJunction();
        System.out.println("open");
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