package org.firstinspires.ftc.teamcode.CompetitionUtils;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBoyWebcam {
    private OpenCvCamera camera;
    private AprilTagDetectionPipeline aprilTagDetectionPipeline;
    private JunctionLocatorPipeline junctionLocatorPipeline = null;
    private HashMap<Integer, ConeStateFinder.ConeState> tagToStateMap;
    // NOTE: the following calibration is for the C920 webcam at 800x600.
    static double fx = 775.79;
    static double fy = 775.79;
    static double cx = 400.898;
    static double cy = 300.79;

    // UNITS ARE METERS
    static double tagsize = 0.03675;//0.02775;//0.037; //in meters

    enum CameraMode {
        APRIL_TAG_RECOGNITION,
        JUNCTION_LOCATOR
    }

    public CameraMode mode = CameraMode.APRIL_TAG_RECOGNITION;

    public MyBoyWebcam(int cameraMonitorViewId, WebcamName webcamName, HashMap<Integer, ConeStateFinder.ConeState> tagToStateMap) {
        this.camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        //this.aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        //this.camera.setPipeline(aprilTagDetectionPipeline);
        this.junctionLocatorPipeline = new JunctionLocatorPipeline();
        this.camera.setPipeline(junctionLocatorPipeline);
        mode = CameraMode.JUNCTION_LOCATOR;
        this.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,600, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        this.tagToStateMap = tagToStateMap;
    }

    public void switchToJunctionLocatorMode() {
        this.mode = CameraMode.JUNCTION_LOCATOR;
        this.aprilTagDetectionPipeline = null;
        this.junctionLocatorPipeline = new JunctionLocatorPipeline();
        this.camera.setPipeline(this.junctionLocatorPipeline);
    }

    public double getAngle() {
        return this.junctionLocatorPipeline.getPosition();
    }

    public double getWidth() {
        return this.junctionLocatorPipeline.getWidth();
    }

    public ConeStateFinder.ConeState getConeState() {
        if(this.mode == CameraMode.APRIL_TAG_RECOGNITION) {
            ArrayList<AprilTagDetection> currentDetections = this.aprilTagDetectionPipeline.getLatestDetections();

            for(AprilTagDetection tag : currentDetections) {
                if(this.tagToStateMap.containsKey(tag.id)) {
                    return this.tagToStateMap.get(tag.id);
                }
            }
        }
        return ConeStateFinder.ConeState.UNKNOWN;
    }

    @Override
    public void finalize() {
        this.camera.closeCameraDevice();
    }


}
