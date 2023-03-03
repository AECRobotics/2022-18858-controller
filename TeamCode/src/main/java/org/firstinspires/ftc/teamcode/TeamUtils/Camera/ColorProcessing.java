package org.firstinspires.ftc.teamcode.TeamUtils.Camera;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;

import java.util.ArrayList;

public class ColorProcessing {
    private static double colorDistSimilarityThreshold = 45.0*45.0;
    private static double colorDotProdSimilarityThreshold = 0.99;
    private static double colorMagnitudeSimilarityThreshold = 249.0*249.0;
    private static double colorHueSimilarityThreshold = 21.0;
    private static double colorSatSimilarityThreshold = 26.0;
    private static double colorValSimilarityThreshold = 36.0;

    public static ArrayList<Double> normalizeColor(int c) {
        double a = (c>>24)&0xff;
        double r = (c>>16)&0xff;
        double g = (c>>8)&0xff;
        double b = (c)&0xff;
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
        int da = ((c1>>24)&0xff)-((c2>>24)&0xff);
        int dr = ((c1>>16)&0xff)-((c2>>16)&0xff);
        int dg = ((c1>>8)&0xff)-((c2>>8)&0xff);
        int db = ((c1)&0xff)-((c2)&0xff);
        //square
        da*=da;
        dr*=dr;
        dg*=dg;
        db*=db;
        //return sum
        return (double)(da+dr+dg+db);
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

    public static double getColorMagnitudeSqrd(int c) {
        double a = (c>>24)&0xff;
        double r = (c>>16)&0xff;
        double g = (c>>8)&0xff;
        double b = (c)&0xff;
        return a*a+r*r+g*g+b*b;
    }

    public static double[] getHSV(int c) {
        double r = (c>>16)&0xff;
        double g = (c>>8)&0xff;
        double b = (c)&0xff;
        r/=255;
        g/=255;
        b/=255;
        double maxc = Math.max(r, Math.max(g, b));
        double minc = Math.min(r, Math.min(g, b));
        double v = maxc;
        if (minc == maxc) {
            //output.add(0,.0,0.0,v);
            return new double[]{0.0, 0.0, v};
        }
        double s = (maxc-minc) / maxc;
        double rc = (maxc-r) / (maxc-minc);
        double gc = (maxc-g) / (maxc-minc);
        double bc = (maxc-b) / (maxc-minc);
        double h;
        if (r == maxc) {
            h = 0.0+bc-gc;
        }
        else if (g == maxc) {
            h = 2.0+rc-bc;
        }
        else {
            h = 4.0+gc-rc;
        }
        h = (h/6.0) % 1.0;
        h*=360;
        s*=100;
        v*=100;
        return new double[]{h, s, v};
    }

    public static boolean matchesColor(int c1, int c2) {
        int matchCount = 0;
        boolean sqrtDistMatches = sqrdColorDistance(c1, c2) <= colorDistSimilarityThreshold;
        if(sqrtDistMatches) {
            matchCount++;
        }
        ArrayList<Double> nc1 = ConeStateFinder.normalizeColor(c1);
        ArrayList<Double> nc2 = ConeStateFinder.normalizeColor(c2);
        boolean dotProductMatches = dotProduct(nc1, nc2) >= colorDotProdSimilarityThreshold;
        if(dotProductMatches) {
            if(Math.abs(getColorMagnitudeSqrd(c1) - getColorMagnitudeSqrd(c2)) < colorMagnitudeSimilarityThreshold) {
                matchCount++;
            }
        }
        if(matchCount == 2) {
            return true;
        }
        double[] hc1 = getHSV(c1);
        double[] hc2 = getHSV(c2);
        boolean hueMatches = Math.abs(hc1[0]-hc2[0]) <= colorHueSimilarityThreshold;
        boolean satMatches = Math.abs(hc1[1]-hc2[1]) <= colorSatSimilarityThreshold;
        boolean valMatches = Math.abs(hc1[2]-hc2[2]) <= colorValSimilarityThreshold;
        if(hueMatches) {
            if(satMatches) {
                if(valMatches) {
                    matchCount++;
                }
            }
        }
        if(matchCount >= 2) {
            return true;
        }
        return false;
    }
}
