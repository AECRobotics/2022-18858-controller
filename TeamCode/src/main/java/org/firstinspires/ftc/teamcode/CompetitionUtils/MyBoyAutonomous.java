package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeamUtils.Autonomous.HolonomicAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Spool;
import org.firstinspires.ftc.teamcode.TeamUtils.UnitConversion;
import org.firstinspires.ftc.teamcode.TeamUtils.Vector2;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MyBoyAutonomous extends HolonomicAutonomous {
    //RobotWebcam webcam = null;
    //ConeStateFinder stateFinder = null;
    //AprilTagDetectionWebcam aprilWebcam = null;
    protected MyBoyWebcam webcam = null;
    protected AprilTagDetectionWebcam aprilTagDetectionWebcam = null;
    HashMap<Integer, ConeStateFinder.ConeState> tagToStateMap;
    public Spool spoolMotor = null;
    public ClawController clawController;
    double angleTarget = 305;//90;
    double widthTarget = 145;//152;
    double alignmentSpeed = 0.1;
    double webcamAngle = 46.225;//33.557;
    public ConeStateFinder.ConeState coneState;

    int[] viewportContainerIds = null;

    public boolean withinTolerance(double value, double target, double tolerance) {
        return Math.abs(value-target) <= tolerance;
    }

    public double clamp(double min, double value, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void switchToJunctionLocatorMode() {
        if(this.webcam == null) {
            //telemetry.addLine("pre nulling");
            //telemetry.update();
            this.aprilTagDetectionWebcam.close();
            this.aprilTagDetectionWebcam = null;
            telemetry.addLine("pre new camera");
            telemetry.update();
            this.webcam = new MyBoyWebcam(viewportContainerIds[0], hardwareMap.get(WebcamName.class, "webcam1"), tagToStateMap);
        }
    }

    public void alignToJunction() {
        System.out.println((System.nanoTime()/UnitConversion.SECONDS_PER_NANOSECOND) + " starting alignment");
        if(this.webcam == null || this.webcam.mode != MyBoyWebcam.CameraMode.JUNCTION_LOCATOR) {
            System.out.println("invalid webcam, terminating");
            return;
        }
        driveBase.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        double angle = 0.0;
        double width = 0.0;
        long start = System.nanoTime();
        do {
            System.out.println((System.nanoTime()/UnitConversion.SECONDS_PER_NANOSECOND) + " looping");
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

            telemetry.addLine(angle + ", " + width);
            telemetry.addLine(angleDiff + ", " + widthDiff);
            telemetry.update();
            driveBase.drive(rotated.y, rotated.x, 0.0);
            /*if(System.nanoTime() > start+(UnitConversion.SECONDS_PER_NANOSECOND*10)) {
                System.out.println("timed out");
                break;
            }*/
        } while ((!withinTolerance(angle, angleTarget, 20) || !withinTolerance(width, widthTarget, 10)));
        System.out.println((System.nanoTime()/UnitConversion.SECONDS_PER_NANOSECOND) + " while done, terminating");
        driveBase.setMotorPower(0.0);
    }

    public MyBoyAutonomous() {}

    public void openClaw() {
        clawController.open();
    }

    public void closeClaw() {
        clawController.close();
    }

    public ConeStateFinder.ConeState getConeState() {
        ArrayList<AprilTagDetection> currentDetections = this.aprilTagDetectionWebcam.getTags();

        for(AprilTagDetection tag : currentDetections) {
            if(this.tagToStateMap.containsKey(tag.id)) {
                return this.tagToStateMap.get(tag.id);
            }
        }
        return ConeStateFinder.ConeState.UNKNOWN;
    }

    protected void internalInit() {
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backleft"); //1
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontleft"); //0
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backright"); //4
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontright"); //2
        spoolMotor = new Spool(hardwareMap.get(DcMotor.class, "spoolmotorgobilda"), 1.0, GoBildaSpoolConstants.TICKS_PER_REV, 0.0, GoBildaSpoolConstants.SPOOL_RADIUS, GoBildaSpoolConstants.SPOOL_WIDTH, 0.907, GoBildaSpoolConstants.THREAD_DIAMETER);
        clawController = new ClawController(hardwareMap);
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        spoolMotor.setPower(1.0);
        //webcam = new RobotWebcam(hardwareMap.get(WebcamName.class, "webcam"));
        //AprilTagDetectionWebcam aprilWebcam = new AprilTagDetectionWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam"));
        tagToStateMap = new HashMap<>();
        tagToStateMap.put(5, ConeStateFinder.ConeState.LEFT);
        tagToStateMap.put(10, ConeStateFinder.ConeState.MIDDLE);
        tagToStateMap.put(15, ConeStateFinder.ConeState.RIGHT);
        //stateFinder = new ConeStateFinder(aprilWebcam, tagToStateMap);
        //
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        /**
         * This is the only thing you need to do differently when using multiple cameras.
         * Instead of obtaining the camera monitor view and directly passing that to the
         * camera constructor, we invoke {@link OpenCvCameraFactory#splitLayoutForMultipleViewports(int, int, OpenCvCameraFactory.ViewportSplitMethod)}
         * on that view in order to split that view into multiple equal-sized child views,
         * and then pass those child views to the constructor.
         */
        viewportContainerIds = OpenCvCameraFactory.getInstance()
                .splitLayoutForMultipleViewports(
                        cameraMonitorViewId, //The container we're splitting
                        2, //The number of sub-containers to create
                        OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY); //Whether to split the container vertically or horizontally

        this.aprilTagDetectionWebcam = new AprilTagDetectionWebcam(viewportContainerIds[0], hardwareMap.get(WebcamName.class, "webcam2"));
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        BNO055IMU imub = hardwareMap.get(BNO055IMU.class, "imu");
        driveBase = new myBoyDrivebase(frontRight, backRight, frontLeft, backLeft, imub);
        //imu = drive.getImu();
    }

    protected void internalInitLoop() {
        coneState = getConeState();
        telemetry.addLine("Gyro status: " + this.driveBase.getImu().getGyroCalibrationStatus().name());
        telemetry.addLine("Cone state:  " + coneState.name());
    }

    protected void internalStart() {
        //coneState = getConeState();
        //telemetry.addLine("internal start");
        //telemetry.update();
        this.switchToJunctionLocatorMode();
    }

    protected void internalStop() {
        driveBase.setMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        driveBase.setMotorPower(0.0);
        this.webcam.close();
    }
}
