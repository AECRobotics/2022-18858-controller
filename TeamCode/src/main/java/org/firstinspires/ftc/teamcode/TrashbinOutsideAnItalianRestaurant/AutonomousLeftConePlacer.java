package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.GoBildaSpoolConstants;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.DriveBaseTask;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;

import java.util.HashMap;

@Disabled
@Autonomous(name="Cone placer autonomous LEFT", group="Robot")
public class AutonomousLeftConePlacer extends OpMode {
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
        leftClaw.setPosition(ClawPositions.rightServoClosed);
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
        aprilWebcam = new AprilTagDetectionWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam"));
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
        spoolMotor.setPower(0.3);
    }
    @Override
    public void init_loop() {
        telemetry.addLine("Gyro: " + imu.getGyroCalibrationStatus());
    }

    @Override
    public void start() {
        closeClaw();
        spoolMotor.setRetractedDistance(50);
    }

    @Override
    public void loop() {
        if(coneState == null || coneState == ConeStateFinder.ConeState.UNKNOWN) {
            coneState = getConePosition();
        }
        //telemetry.addLine(ConeStateFinder.debugOutput);
        //telemetry.addLine(drive.getTask().getTaskType().name());
        //telemetry.addLine(drive.isTaskComplete() + "");
        //telemetry.addLine(drive.getTask().getState() + "");
        //telemetry.addLine(drive.allMotorsReachedTarget() + "");
        //telemetry.addLine(drive.allMotorsNotBusy() + "");
        //telemetry.addLine(drive.getFr().getCurrentPosition() + ", " + drive.getFr().getTargetPosition());
        //telemetry.addLine(drive.getBr().getCurrentPosition() + ", " + drive.getBr().getTargetPosition());
        if(drive.isTaskComplete() && coneState != null) {
            HashMap<String, Double> parameters = new HashMap<String, Double>();
            switch(drive.getTaskCount()) {
                case 0:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.61);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 1:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.65);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 2:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.05);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 3:
                    parameters.put("speed", 0.5);
                    parameters.put("degrees", 45.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 4:
                    spoolMotor.setRetractedDistance(370);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 5:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.15);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 6:
                    openClaw();
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 7:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.15);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 8:
                    spoolMotor.setRetractedDistance(50);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 9:
                    parameters.put("speed", 0.5);
                    parameters.put("degrees", -45.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 10:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.65);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 11:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.3);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                    break;
                case 12:
                    parameters.put("speed", 0.5);
                    parameters.put("degrees", -90.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 13:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.4);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 14:
                    closeClaw();
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 15:
                    spoolMotor.setRetractedDistance(200);
                    parameters.put("seconds", 1.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 16:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.65);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 17:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 135.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 18:
                    spoolMotor.setRetractedDistance(870);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 19:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.15);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 20:
                    openClaw();
                    parameters.put("seconds", 0.5);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 21:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.15);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 22:
                    spoolMotor.setRetractedDistance(50);
                    parameters.put("seconds", 5.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.WAIT_FOR, parameters));
                    break;
                case 23:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -45.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 24:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", -0.65);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_DISTANCE, parameters));
                    break;
                case 25:
                    if(coneState == ConeStateFinder.ConeState.UNKNOWN) {
                        break;
                    }
                    parameters.put("speed", 0.5);
                    parameters.put("meters", (coneState == ConeStateFinder.ConeState.MIDDLE) ? 0.0 : (coneState == ConeStateFinder.ConeState.LEFT) ? -0.64055 : (coneState == ConeStateFinder.ConeState.RIGHT ? 0.68785 : 0.0));
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_DISTANCE, parameters));
                /*case 0:
                    if(coneState == ConeStateFinder.ConeState.UNKNOWN) {
                        break;
                    }
                    parameters.put("speed", 0.5);
                    parameters.put("meters", (coneState == ConeStateFinder.ConeState.MIDDLE) ? 0.0 : (coneState == ConeStateFinder.ConeState.LEFT) ? -0.64055 : (coneState == ConeStateFinder.ConeState.RIGHT ? 0.68785 : 0.0));
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.STRAFE_TO_POSITION, parameters));
                    break;
                case 1:
                    parameters.put("speed", 0.5);
                    parameters.put("meters", 0.65);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.DRIVE_TO_POSITION, parameters));
                    break;*/
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
