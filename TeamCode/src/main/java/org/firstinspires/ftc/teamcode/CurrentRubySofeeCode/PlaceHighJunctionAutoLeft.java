package org.firstinspires.ftc.teamcode.CurrentRubySofeeCode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.DriveBase.DriveBaseTask;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;

import java.util.HashMap;

@Autonomous(name="WIP LEFT", group="Deprecated")
public class PlaceHighJunctionAutoLeft extends OpMode {
    public CHubIMU imu = null;
    RobotWebcam webcam = null;
    myBoyDrivebase drive = null;
    ConeStateFinder.ConeState coneState = null;
    AprilTagDetectionWebcam aprilWebcam = null;
    public Spool spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;

    public void openClaw() {
        leftClaw.setPosition(ClawPositions.leftServoOpen);
        rightClaw.setPosition(ClawPositions.rightServoOpen);
    }

    public void closeClaw() {
        leftClaw.setPosition(ClawPositions.leftServoClosed);
        rightClaw.setPosition(ClawPositions.rightServoClosed);
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "backleft"); //1
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft"); //0
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "backright"); //4
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright"); //2

        //webcam = new RobotWebcam(hardwareMap.get(WebcamName.class, "webcam"));
        aprilWebcam = new AprilTagDetectionWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam2"));
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        leftBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        BNO055IMU imub = hardwareMap.get(BNO055IMU.class, "imu");
        drive = new myBoyDrivebase(rightFrontDrive, rightBackDrive, leftFrontDrive, leftBackDrive, imub);
        imu = drive.getImu();
        spoolMotor = new Spool(hardwareMap.get(DcMotor.class, "spoolmotorgobilda"), 1.0, GoBildaSpoolConstants.TICKS_PER_REV, 0.0, GoBildaSpoolConstants.SPOOL_RADIUS, GoBildaSpoolConstants.SPOOL_WIDTH, 0.907, GoBildaSpoolConstants.THREAD_DIAMETER);
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        spoolMotor.setPower(1.0);
        //ConeStateFinder.setWebcam(aprilWebcam);
        //ConeStateFinder.startCheckingState();
    }
    @Override
    public void init_loop() {
        telemetry.addLine("Gyro: " + imu.getGyroCalibrationStatus());
        coneState = getConePosition();
        telemetry.addLine("Cone State: " + getConePosition());
    }

    @Override
    public void start() {
        closeClaw();
        //spoolMotor.setRetractedDistance(50);
    }

    @Override
    public void loop() {
        if(coneState == null || coneState == ConeStateFinder.ConeState.UNKNOWN) {
            coneState = getConePosition();
        }

        if(drive.isTaskComplete() && coneState != null) {
            HashMap<String, Double> parameters = new HashMap<String, Double>();
            switch(drive.getTaskCount()) {
                case 0:
                    closeClaw();
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 1:
                    spoolMotor.setRetractedDistance(ArmHeightPositions.GROUND_PLACEMENT+50);
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.66);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 2:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 1.17);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 3:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.35);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 4:
                    spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 5:
                    parameters.put("speed", 0.2);
                    parameters.put("meters", 0.135);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 6:
                    spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT-50);
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 7:
                    openClaw();
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 8:
                    spoolMotor.setRetractedDistance(ArmHeightPositions.HIGH_PLACEMENT);
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 9:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.14);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 10:
                    spoolMotor.setRetractedDistance(ArmHeightPositions.GROUND_PLACEMENT);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 11:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.365);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 12:
                    if(coneState == ConeStateFinder.ConeState.UNKNOWN) {
                        break;
                    }
                    parameters.put("speed", 0.5);
                    parameters.put("meters", (coneState == ConeStateFinder.ConeState.MIDDLE) ? 0.0 : (coneState == ConeStateFinder.ConeState.LEFT) ? -0.6405 : (coneState == ConeStateFinder.ConeState.RIGHT ? 0.68785 : 0.0));
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
            }
        }
        drive.doTasks();
        telemetry.addLine(coneState.name());
    }

    public ConeStateFinder.ConeState getConePosition(){
        //return (int)Math.floor(r.nextDouble()*3.0);
        //return ConeStateFinder.getConeState(webcam);
        return ConeStateFinder.getConeStateAprilTag(aprilWebcam);
    }

    @Override
    public void stop(){
        drive.setMotorPower(0.0);
        webcam.closeCamera();
    }
}
