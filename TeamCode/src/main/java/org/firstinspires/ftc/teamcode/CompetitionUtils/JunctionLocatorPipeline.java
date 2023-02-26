package org.firstinspires.ftc.teamcode.CompetitionUtils;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.firstinspires.ftc.teamcode.TeamUtils.Camera.ColorProcessing;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.HashMap;

public class JunctionLocatorPipeline extends OpenCvPipeline {
    public static String debugOutput = "";
    private double desiredDistance = 20.0;
    private double desiredAngle = -20.0;
    int color1 = 0x00b28342;
    int color2 = 0x007e4f1b;
    int color3 = 0x00b2ae8f;
    private int position = 0;
    private int width = 0;

    public JunctionLocatorPipeline() {
    }

    public Mat processFrame(Mat input) {
        debugOutput = "";
        Bitmap bmp = null;
        try {
            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
            //Imgproc.cvtColor(seedsImage, input, Imgproc.COLOR_GRAY2RGBA, 4);
            bmp = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(input, bmp);
            //System.out.println(bmp);
        }
        catch (Exception e){e.printStackTrace(); return input;}
        //System.out.println("FFFFFFFUUU");
        HashMap<Integer, Boolean> colorMatchMap = new HashMap<>();
        HashMap<Integer, Integer> colorMatchCountMap = new HashMap<>();
        for(int x = 0; x < bmp.getWidth(); x++) {
            for(int y = 0; y < bmp.getHeight(); y+=100) {
                int color = bmp.getPixel(x,y);
                if(!colorMatchMap.containsKey(color)) {
                    colorMatchMap.put(color, ColorProcessing.matchesColor(color, color1) || ColorProcessing.matchesColor(color, color2) || ColorProcessing.matchesColor(color, color3));
                }
                if(colorMatchMap.get(color)) {
                    if(colorMatchCountMap.containsKey(x)) {
                        colorMatchCountMap.put(x,colorMatchCountMap.get(x)+1);
                    } else {
                        colorMatchCountMap.put(x, 1);
                    }
                }
            }
        }
        //debugOutput+=("" + colorMatchCountMap);
        //System.out.println(colorMatchCountMap);
        /*int matchCount = 0;
        position = 0;
        //int minimum = Integer.MAX_VALUE;
        //int maximum = 0;
        for(Integer key : colorMatchCountMap.keySet()) {
            int count = colorMatchCountMap.get(key);
            position+=(key*count);
            matchCount+=count;
            if(count > 3) {
                if(key < minimum) {
                    minimum = key;
                } else if(key > maximum) {
                    maximum = key;
                }
            }
        }*/
        width = 0;
        int widestStart = 0;
        int widestWidth = 0;
        int widestPosition = 0;
        int widestMatchCount = 0;
        int currentStart = 0;
        int currentWidth = 0;
        int currentPosition = 0;
        int currentMatchCount = 0;
        for(int x = 0; x < bmp.getWidth(); x++) {
            int count = 0;
            if(colorMatchCountMap.containsKey(x)) {
                count = colorMatchCountMap.get(x);
            }
            if(count > 3) {
                if(currentWidth == 0) {
                    currentStart = x;
                    currentMatchCount+=count;
                    currentPosition+=(count*x);
                }
                currentWidth++;
            } else {
                currentWidth = 0;
                currentPosition = 0;
                currentMatchCount = 0;
            }
            if(currentWidth > widestWidth) {
                widestWidth = currentWidth;
                widestStart = currentStart;
                widestPosition = currentPosition;
                widestMatchCount = currentMatchCount;
            }
        }
        //width = widestWidth;
        try {
            position = widestPosition/=widestMatchCount;
        } catch (ArithmeticException e) {
            position = 0;
        }
        width = widestWidth;
        return input;
    }

    public int getPosition() {
        return this.position;
    }

    public int getWidth() {
        return this.width;
    }
}
