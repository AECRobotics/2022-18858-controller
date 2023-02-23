package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeamUtils.Autonomous.HolonomicAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;

import java.util.HashMap;

public abstract class MyBoyAutonomous extends HolonomicAutonomous {
    //RobotWebcam webcam = null;
    //ConeStateFinder stateFinder = null;
    //AprilTagDetectionWebcam aprilWebcam = null;
    protected MyBoyWebcam webcam = null;
    public Spool spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;

    public MyBoyAutonomous() {}

    public void openClaw() {
        leftClaw.setPosition(ClawPositions.leftServoOpen);
        rightClaw.setPosition(ClawPositions.rightServoOpen);
    }

    public void closeClaw() {
        leftClaw.setPosition(ClawPositions.rightServoClosed);
        rightClaw.setPosition(ClawPositions.rightServoClosed);
    }

    protected void internalInit() {
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "backleft"); //1
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft"); //0
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "backright"); //4
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright"); //2

        //webcam = new RobotWebcam(hardwareMap.get(WebcamName.class, "webcam"));
        //AprilTagDetectionWebcam aprilWebcam = new AprilTagDetectionWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam"));
        HashMap<Integer, ConeStateFinder.ConeState> tagToStateMap = new HashMap<>();
        tagToStateMap.put(5, ConeStateFinder.ConeState.LEFT);
        tagToStateMap.put(10, ConeStateFinder.ConeState.MIDDLE);
        tagToStateMap.put(15, ConeStateFinder.ConeState.RIGHT);
        //stateFinder = new ConeStateFinder(aprilWebcam, tagToStateMap);
        this.webcam = new MyBoyWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam"), tagToStateMap);

        BNO055IMU imub = hardwareMap.get(BNO055IMU.class, "imu");
        driveBase = new myBoyDrivebase(rightFrontDrive, rightBackDrive, leftFrontDrive, leftBackDrive, imub);
        //imu = drive.getImu();
    }

    protected void internalStart() {

    }
}
