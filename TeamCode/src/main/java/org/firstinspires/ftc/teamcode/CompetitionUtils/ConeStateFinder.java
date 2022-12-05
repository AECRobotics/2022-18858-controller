package org.firstinspires.ftc.teamcode.CompetitionUtils;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;

import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;

import java.util.HashMap;

public class ConeStateFinder {
    public enum ConeState {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private static double checkAreaWidth = 0.5;
    private static double checkAreaHeight = 0.5;
    @ColorInt
    private static int s1Color = 0xff42f5ef; //#42f5ef left
    @ColorInt
    private static int s2Color = 0xff90f542; //#90f542 middle
    @ColorInt
    private static int s3Color = 0xfff542bf; //#f542bf right

    private static double colorSimilarityThreshold = 10.0;

    public static double sqrdColorDistance(int c1, int c2) {
        //4 dimensional euclidean distance formula without square root because square root is slow and comparisons between distances remain true even without square root
        //find differences
        int ad = (c1>>24)-(c2>>24);
        int ar = (c1>>16)-(c2>>16);
        int ag = (c1>>8)-(c2>>8);
        int ab = (c1)-(c2);
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

    public static ConeState getConeState(RobotWebcam webcam) {
        Bitmap frame = webcam.getWebcamFrame();
        int width = frame.getWidth();
        int height = frame.getHeight();
        HashMap<Integer, Integer> colorOccurences = new HashMap<Integer, Integer>();

        for(int x = (int)(width*(0.5-(checkAreaWidth/2.0))); x < (int)(width*((0.5-(checkAreaWidth/2.0)))+0.5); x++) {
            for(int y = (int)(height*(0.5-(checkAreaHeight/2.0))); y < (int)(height*((0.5-(checkAreaHeight/2.0)))+0.5); y++) {
                int c = frame.getPixel(x,y);
                colorOccurences.put(c, colorOccurences.containsKey(c) ? colorOccurences.get(c)+1 : 1);
            }
        }
        int s1Count = 0;
        int s2Count = 0;
        int s3Count = 0;
        for(Integer key : colorOccurences.keySet()) {
            if(sqrdColorDistance(key, s1Color) <= colorSimilarityThreshold) {
                s1Count+=colorOccurences.get(key);
            } else if(sqrdColorDistance(key, s2Color) <= colorSimilarityThreshold) {
                s2Count+=colorOccurences.get(key);
            } else if(sqrdColorDistance(key, s3Color) <= colorSimilarityThreshold) {
                s3Count+=colorOccurences.get(key);
            }
        }
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
