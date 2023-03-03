package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;

@TeleOp(name="New Autonomous Template", group="Robot")
//@Disabled
public class NewAutonomousTemplate extends MyBoyAutonomous {
    ConeStateFinder.ConeState coneState = ConeStateFinder.ConeState.UNKNOWN;

    @Override
    public void runOpMode() {
        this.internalInit();

        //put per autonomous init stuff here

        while(opModeInInit()) {
            this.internalInitLoop(); //MyBoyAutonomous.internalInitLoop() does not call telemetry.update()

            //put per autonomous init loop stuff here

            telemetry.update(); //hence why it is here
        }

        //waitForStart();
        this.internalStart(); //sets coneState variable

        //this.webcam.switchToJunctionLocatorMode(); //doesn't get used because MyBoyWebcam is for webcam1 which was originally intended for both junction locating and cone state finding and it is now only for junction locating and is already in MyBoyWebcam.JunctionLocator mode
        telemetry.addLine(coneState.name());
        telemetry.update();

        //autonomous code goes here

        this.internalStop();
        /*
        built in functions and variables that may be useful:

        MyBoyAutonomous.spoolMotor
        MyBoyAutonomous.webcam
        MyBoyAutonomous.aprilTagRecognitionWebcam

        MyBoyAutonomous.switchToJunctionLocatorMode()
        MyBoyAutonomous.openClaw()
        MyBoyAutonomous.closeClaw()
        MyBoyAutonomous.alignToJunction()
        MyBoyAutonomous.getConeState()
        MyBoyAutonomous.clamp()
        MyBoyAutonomous.within()

        HolonomicAutonomous.driveBase

        HolonomicAutonomous.driveBase.forward()
        HolonomicAutonomous.driveBase.strafe()
        HolonomicAutonomous.driveBase.turn()
        HolonomicAutonomous.driveBase.turnToHeading()
        sleep()
        */
    }
}
