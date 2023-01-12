package org.firstinspires.ftc.teamcode.CompetitionUtils;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ConeStateFinder {
    public enum ConeState {
        LEFT,
        MIDDLE,
        RIGHT
    }

    //private static double checkAreaWidth = 0.5;
    //private static double checkAreaHeight = 0.5;
    @ColorInt
    private static int s1Color = 0xff65a7bd; //printed #65a7bd real #42f5ef 0xff42f5ef left
    @ColorInt
    private static int s2Color = 0xffacc573; //printed #5c8158 real #90f542 0xff90f542 middle
    @ColorInt
    private static int s3Color = 0xffbf5c7e; //printed #8c537c real #f542bf 0xfff542bf right

    private static double colorDistSimilarityThreshold = 23.0*23.0;
    private static double colorDotProdSimilaryThreshold = 0.98;
    private static double colorMagnitudeSimilarityThreshold = 7.0*7.0;

    public static String debugOutput = "";

    public static ArrayList<Double> normalizeColor(int c) {
        double a = (c&0xff000000)>>24;
        double r = (c&0x00ff0000)>>16;
        double g = (c&0x0000ff00)>>8;
        double b = (c&0x000000ff);
        double magnitude = Math.sqrt(a*a+r*r+g*g+b*b);
        ArrayList<Double> normalized = new ArrayList<Double>();
        normalized.add(a/magnitude);
        normalized.add(r/magnitude);
        normalized.add(g/magnitude);
        normalized.add(b/magnitude);
        return normalized;
    }

    public static double sqrdColorDistance(int c1, int c2) {
        //4 dimensional euclidean distance formula without square root because square root is slow and comparisons between distances remain true even without square root
        //find differences
        int ad = ((c1&0xff000000)>>24)-((c2&0xff000000)>>24);
        int ar = ((c1&0x00ff0000)>>16)-((c2&0x00ff0000)>>16);
        int ag = ((c1&0x0000ff00)>>8)-((c2&0x0000ff00)>>8);
        int ab = ((c1&0x000000ff))-((c2&0x000000ff));
        //square
        ad*=ad;
        ar*=ar;
        ag*=ag;
        ab*=ab;
        //return sum
        return (double)(ad+ar+ag+ab);
    }
    public static double colorDistance(int c1, int c2) {
        return Math.sqrt(sqrdColorDistance(c1, c2));
    }

    public static double dotProduct(ArrayList<Double> c1, ArrayList<Double> c2) {
        double output = 0.0;
        for(int i = 0; i < c1.size(); i++) {
            output+=(c1.get(i)*c2.get(i));
        }
        return output;
    }

    public static double getColorMagnitude(int c) {
        double a = (c&0xff000000)>>24;
        double r = (c&0x00ff0000)>>16;
        double g = (c&0x0000ff00)>>8;
        double b = (c&0x000000ff);
        return Math.sqrt(a*a+r*r+g*g+b*b);
    }

    public static boolean matchesColor(int c1, int c2) {
        boolean sqrtDistMatches = sqrdColorDistance(c1, c2) <= colorDistSimilarityThreshold;
        //debugOutput+=("dist: " + sqrdColorDistance(c1, c2) + ", ");
        if(sqrtDistMatches) {
            return true;
        } else {
            //return false;
        }
        ArrayList<Double> nc1 = ConeStateFinder.normalizeColor(c1);
        ArrayList<Double> nc2 = ConeStateFinder.normalizeColor(c2);
        //debugOutput+=("dot: " + dotProduct(nc1, nc2) + ", ");
        //debugOutput+=("mag: " + getColorMagnitude(c1) + "," + getColorMagnitude(c2) + ",");
        boolean dotProductMatches = dotProduct(nc1, nc2) >= colorDotProdSimilaryThreshold;
        if(dotProductMatches) {
            if(Math.abs(getColorMagnitude(c1) - getColorMagnitude(c2)) < colorMagnitudeSimilarityThreshold) {
                return true;
            }
        }
        return false;
    }

    public static ConeState getConeState(RobotWebcam webcam) {
        debugOutput = "";
        Bitmap frame = webcam.getWebcamFrame();
        if(frame == null) {
            return null;
        }
        int width = frame.getWidth();
        int height = frame.getHeight();
        debugOutput+=("hex: " + String.format("%x", frame.getPixel(0,0)) + ", ");
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
        HashMap<Integer, Integer> colorMatchDict = new HashMap<>();
        for(Integer key : colorOccurences.keySet()) {
            if(matchesColor(key, s1Color)) {
                //s1Count+=colorOccurences.get(key);
                colorMatchDict.put(key, 1);
            } else if(matchesColor(key, s2Color)) {
                //s2Count+=colorOccurences.get(key);
                colorMatchDict.put(key, 2);
            } else if(matchesColor(key, s3Color)) {
                //s3Count+=colorOccurences.get(key);
                colorMatchDict.put(key, 3);
            } else {
                colorMatchDict.put(key, 0);
            }
        }

        for(int x = (int)(width*0.25); x < (int)(width*0.75); x++) {
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
        }
        debugOutput+=("left:" + Math.floor(s1Count) + " middle: " + Math.floor(s2Count) + " right: " + Math.floor(s3Count) + ", ");
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
