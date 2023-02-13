package org.firstinspires.ftc.teamcode.CompetitionUtils;

import org.firstinspires.ftc.teamcode.TeamUtils.AprilTagDetectionWebcam;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;

public class ConeStateFinderThread extends Thread {
    public ConeStateFinder.ConeState detectedState = ConeStateFinder.ConeState.UNKNOWN;
    public boolean running = false;
    public boolean stopped = false;
    public void run() {
        running = true;
        stopped = false;
        AprilTagDetectionWebcam webcam = ConeStateFinder.getWebcam();
        while(this.running) {
            this.detectedState = ConeStateFinder.getConeStateAprilTag(webcam);
        }
        stopped = true;
    }
}
