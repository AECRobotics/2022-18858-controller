package org.firstinspires.ftc.teamcode.CompetitionUtils;

import android.graphics.Bitmap;

import androidx.annotation.ColorInt;

import org.firstinspires.ftc.teamcode.TeamUtils.Camera.AprilTagRecognition.AprilTagDetectionWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.ColorProcessing;
import org.firstinspires.ftc.teamcode.TeamUtils.Camera.RobotWebcam;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.HashMap;

public class ConeStateFinder {
    public enum ConeState {
        UNKNOWN,
        LEFT,
        MIDDLE,
        RIGHT
    }

    //private static double checkAreaWidth = 0.5;
    //private static double checkAreaHeight = 0.5;
    private static int leftTagID = 5;
    private static int middleTagID = 10;
    private static int rightTagID = 15;

    @ColorInt
    private static int s1Color = 0xff65a7bd; //printed #65a7bd real #42f5ef 0xff42f5ef left
    @ColorInt
    private static int s2Color = 0xffacc573; //printed #5c8158 real #90f542 0xff90f542 middle
    @ColorInt
    private static int s3Color = 0xffbf5c7e; //printed #8c537c real #f542bf 0xfff542bf right

    private static double colorDistSimilarityThreshold = 45.0*45.0;
    private static double colorDotProdSimilarityThreshold = 0.99;
    private static double colorMagnitudeSimilarityThreshold = 249.0*249.0;
    private static double colorHueSimilarityThreshold = 21.0;
    private static double colorSatSimilarityThreshold = 26.0;
    private static double colorValSimilarityThreshold = 36.0;
    private static double knownPitch = 0.0;
    private static double knownRoll = 0.0;
    private static double knownYaw = 0.0;
    private static double knownAngleSimilarityThreshold = 1000.0;


    public static String debugOutput = "";

    private AprilTagDetectionWebcam webcam;
    private HashMap<Integer, ConeState> tagToStateMap;

    public ConeStateFinder(AprilTagDetectionWebcam webcam, HashMap<Integer, ConeState> tagToStateMap) {
        this.webcam = webcam;
        this.tagToStateMap = tagToStateMap;
    }

    public ConeState getConeState() {
        ArrayList<AprilTagDetection> currentDetections = webcam.getTags();

        for(AprilTagDetection tag : currentDetections) {
            if(this.tagToStateMap.containsKey(tag.id)) {
                return this.tagToStateMap.get(tag.id);
            }
        }
        return ConeState.UNKNOWN;
    }

    public static ConeState getConeStateAprilTag(AprilTagDetectionWebcam webcam) {
        ArrayList<AprilTagDetection> currentDetections = webcam.getTags();

        for(AprilTagDetection tag : currentDetections) {
            //if(Math.abs(tag.pose.pitch-knownPitch) < knownAngleSimilarityThreshold) {

            //}
            if(tag.id == ConeStateFinder.leftTagID) {
                return ConeState.LEFT;
            } else if(tag.id == ConeStateFinder.middleTagID) {
                return ConeState.MIDDLE;
            } else if(tag.id == ConeStateFinder.rightTagID) {
                return ConeState.RIGHT;
            }
        }
        return ConeState.UNKNOWN;
    }

    public static ConeState getConeState(RobotWebcam webcam) {
        debugOutput = "";
        Bitmap frame = webcam.getWebcamFrame();
        if(frame == null) {
            return null;
        }
        int width = frame.getWidth();
        int height = frame.getHeight();
        //debugOutput+=("hex: " + String.format("%x", frame.getPixel(0,0)) + ", ");
        HashMap<Integer, Integer> colorOccurences = new HashMap<Integer, Integer>();
        //(0.5-(checkAreaWidth/2.0))
        //((0.5-(checkAreaWidth/2.0))
        //(0.5-(checkAreaHeight/2.0))
        //((0.5-(checkAreaHeight/2.0)))+0.5
        for(int x = (int)(width*0.25); x < (int)(width*0.75); x++) {
            for(int y = (int)(height*0.25); y < (int)(height*0.75); y++) {
                int c = frame.getPixel(x,y);
                colorOccurences.put(c, colorOccurences.containsKey(c) ? colorOccurences.get(c)+1 : 1);
            }
        }
        double s1Count = 0;
        double s2Count = 0;
        double s3Count = 0;
        //HashMap<Integer, Integer> colorMatchDict = new HashMap<>();
        for(Integer key : colorOccurences.keySet()) {
            if(ColorProcessing.matchesColor(key, s1Color)) {
                s1Count+=colorOccurences.get(key);
                //colorMatchDict.put(key, 1);
            } else if(ColorProcessing.matchesColor(key, s2Color)) {
                s2Count+=colorOccurences.get(key);
                //colorMatchDict.put(key, 2);
            } else if(ColorProcessing.matchesColor(key, s3Color)) {
                s3Count+=colorOccurences.get(key);
                //colorMatchDict.put(key, 3);
            } else {
                //colorMatchDict.put(key, 0);
            }
        }

        /*for(int x = (int)(width*0.25); x < (int)(width*0.75); x++) {
            for(int y = (int)(height*0.25); y < (int)(height*0.75); y++) {
                int c = frame.getPixel(x,y);
                int match = colorMatchDict.get(c);
                int dcx = Math.abs((x-(width/2)));
                int dcy = Math.abs((y-(height/2)));
                double dist = ((double)(dcx+dcy))/((double)(width+height)/2);
                switch(match) {
                    case 1:
                        s1Count+=dist;
                        break;
                    case 2:
                        s2Count+=dist;
                        break;
                    case 3:
                        s3Count+=dist;
                        break;
                }
            }
        }*/
        //debugOutput+=("left:" + Math.floor(s1Count) + " middle: " + Math.floor(s2Count) + " right: " + Math.floor(s3Count) + ", ");
        if(s1Count < s2Count) {
            if(s2Count < s3Count) {
                return ConeState.RIGHT; //color 3
            } else {
                return ConeState.MIDDLE; //color 2
            }
        } else if(s1Count < s3Count) {
            return ConeState.RIGHT; // color 3
        } else {
            return ConeState.LEFT; //color 1
        }
        //return ConeState.LEFT;
    }
}
