package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeamUtils.Autonomous.HolonomicAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;
import org.firstinspires.ftc.teamcode.TeamUtils.UnitConversion;
import org.firstinspires.ftc.teamcode.TeamUtils.Vector2;

import java.util.HashMap;

public abstract class MyBoyAutonomous extends HolonomicAutonomous {
    //RobotWebcam webcam = null;
    //ConeStateFinder stateFinder = null;
    //AprilTagDetectionWebcam aprilWebcam = null;
    protected MyBoyWebcam webcam = null;
    public Spool spoolMotor = null;
    ClawController clawController;
    double angleTarget = 305;//90;
    double widthTarget = 145;//152;
    double alignmentSpeed = 0.2;
    double webcamAngle = 46.225;//33.557;

    public boolean withinTolerance(double value, double target, double tolerance) {
        return Math.abs(value-target) <= tolerance;
    }

    public double clamp(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void alignToJunction() {
        if(this.webcam.mode != MyBoyWebcam.CameraMode.JUNCTION_LOCATOR) {
            return;
        }
        driveBase.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        double angle = 0.0;
        double width = 0.0;
        long start = System.nanoTime();
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
            rotated = rotated.multiply(-Math.min(diffMag*alignmentSpeed/400, alignmentSpeed));
            rotated.x*=(-1);

            //telemetry.addLine(angleDiff + ", " + widthDiff);
            //telemetry.update();
            driveBase.drive(rotated.y, rotated.x, 0.0);
        } while ((!withinTolerance(angle, angleTarget, 20) || !withinTolerance(width, widthTarget, 10)) && System.nanoTime() > start+ UnitConversion.SECONDS_PER_NANOSECOND*5);
        driveBase.setMotorPower(0.0);
    }

    public MyBoyAutonomous() {}

    public void openClaw() {
        clawController.open();
    }

    public void closeClaw() {
        clawController.close();
    }

    protected void internalInit() {
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "backleft"); //1
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft"); //0
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "backright"); //4
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright"); //2
        spoolMotor = new Spool(hardwareMap.get(DcMotor.class, "spoolmotorgobilda"), 1.0, GoBildaSpoolConstants.TICKS_PER_REV, 0.0, GoBildaSpoolConstants.SPOOL_RADIUS, GoBildaSpoolConstants.SPOOL_WIDTH, 0.907, GoBildaSpoolConstants.THREAD_DIAMETER);
        clawController = new ClawController(hardwareMap);
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        spoolMotor.setPower(1.0);
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
