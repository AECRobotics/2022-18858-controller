package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.DriveBaseTask;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;

import java.util.HashMap;

@Disabled
@Autonomous(name="Test Autonomous", group="Robot")
public class TestAutonomous extends OpMode {
    public CHubIMU imu = null;
    RobotWebcam webcam = null;
    myBoyDrivebase drive = null;
    ConeStateFinder.ConeState coneState = null;
    AprilTagDetectionWebcam aprilWebcam = null;
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
        imu = new CHubIMU(imub, AxesOrder.XYZ);
        drive = new myBoyDrivebase(rightFrontDrive, rightBackDrive, leftFrontDrive, leftBackDrive, imu);
    }
    @Override
    public void init_loop() {
        telemetry.addLine("Gyro: " + imu.getGyroCalibrationStatus());
    }

    @Override
    public void start() {

    }

    private boolean debug = false;

    @Override
    public void loop() {
        telemetry.addLine("" + imu.getOrientation());
        telemetry.addLine("" + drive.getHeading());
        telemetry.addLine("" + drive.getTaskCount());
        //telemetry.addLine(ConeStateFinder.debugOutput);
        telemetry.addLine(drive.getTask().getTaskType().name());
        telemetry.addLine(drive.isTaskComplete() + "");
        telemetry.addLine(drive.getTask().getState() + "");
        telemetry.addLine(debug ? drive.distanceToTurn() + "" : "not right task");
        //telemetry.addLine(drive.allMotorsNotBusy() + "");
        //telemetry.addLine(drive.getFr().getCurrentPosition() + ", " + drive.getFr().getTargetPosition());
        //telemetry.addLine(drive.getBr().getCurrentPosition() + ", " + drive.getBr().getTargetPosition());
        telemetry.addLine("" + drive.getTaskCount());

        if(drive.isTaskComplete()) {
            HashMap<String, Double> parameters = new HashMap<String, Double>();
            switch(drive.getTaskCount()) {
                case 0:
                    debug = true;
                    parameters.put("speed", 0.2);
                    parameters.put("degrees", 45.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 1:
                    debug = true;
                    parameters.put("speed", 0.2);
                    parameters.put("degrees", 90.0);
                    drive.setTask(new DriveBaseTask(DriveBaseTask.TaskType.TURN_DEGREES, parameters));
                    break;
                case 2:
                    debug = false;
                    drive.setMotorPower(0.0);
            }
        }
        drive.doTasks();
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
