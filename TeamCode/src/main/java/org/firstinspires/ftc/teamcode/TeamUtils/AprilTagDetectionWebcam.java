package org.firstinspires.ftc.teamcode.TeamUtils;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

public class AprilTagDetectionWebcam {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS because fucking why
    // NOTE: this calibration is for the C270 webcam at idfk.
    // You will need to do your own calibration for other configurations!
    double fx = 1430.0/2.0;
    double fy = 1430.0/2.0;
    double cx = 620.0/2.0;
    double cy = 480.0/2.0;

    // UNITS ARE METERS
    double tagsize = 0.03675;//0.02775;//0.037; //in meters

    public AprilTagDetectionWebcam(int cameraMonitorViewId, WebcamName webcamName) {
        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
    }

    public ArrayList<AprilTagDetection> getTags() {
        return this.aprilTagDetectionPipeline.getLatestDetections();
    }
}
