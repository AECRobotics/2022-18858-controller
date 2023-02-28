package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;

@TeleOp(name="test webcam cone state finder ( ͡°( ͡° ͜ʖ( ͡° ͜ʖ ͡°)ʖ ͡°) ͡°)", group = "Debug")
public class webcamCheck extends OpMode{
    boolean lastGamepadB = false;
    ConeStateFinder.ConeState state = null;
    RobotWebcam webcam = null;
    AprilTagDetectionWebcam aprilWebcam = null;
    long time = 0;
    public void init() {
        telemetry.addData("Status", "Initialized");
        WebcamName webcamn = hardwareMap.get(WebcamName.class, "webcam");
        webcam = new RobotWebcam(webcamn);
        aprilWebcam = new AprilTagDetectionWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam1"));
    }
    public void init_loop(){

    }
    public void start(){

    }
    int n = 0;
    public void loop(){
        if(n == 0) {
            telemetry.addLine(ConeStateFinder.matchesColor(0,0) + "");
        }
        telemetry.addLine("dist: " + ConeStateFinder.sqrdColorDistance(0x00000000, 0xff65a7bd));
        n++;
        if(gamepad1.b && !lastGamepadB) {
            /*Bitmap thing2 = webcam.getWebcamFrame();
            if(thing2 == null) {
                telemetry.addLine("debug4");
                //this.requestOpModeStop();
            }*/
            telemetry.addLine("debug5");
            long thing = System.nanoTime();
            state = ConeStateFinder.getConeStateAprilTag(aprilWebcam);
            time = System.nanoTime()-thing;
        }

        lastGamepadB = gamepad1.b;
        telemetry.addLine(state != null ? state.name() : "null");
        telemetry.addLine("took " + ((double)time/1000000.0) + " ms");
        telemetry.addLine("" + System.nanoTime());
        telemetry.addLine(ConeStateFinder.debugOutput);
    }

    public void stop(){
        webcam.closeCamera();
    }
}
