package org.firstinspires.ftc.teamcode.TeamUtils;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class EulerAngle {
    public double x,y,z;

    EulerAngle(double y, double x, double z) {
        this.y = y;
        this.x = x;
        this.z = z;
    }

    public static EulerAngle quaternionToEuler(Quaternion q1) {
        double sqw = q1.w*q1.w;
        double sqx = q1.x*q1.x;
        double sqy = q1.y*q1.y;
        double sqz = q1.z*q1.z;
        double heading = Math.atan2(2.0 * (q1.x*q1.y + q1.z*q1.w),(sqx - sqy - sqz + sqw));
        double bank = Math.atan2(2.0 * (q1.y*q1.z + q1.x*q1.w),(-sqx - sqy + sqz + sqw));
        double attitude = Math.asin(-2.0 * (q1.x*q1.z - q1.y*q1.w));
        return new EulerAngle(heading, attitude, bank);
    }

    public String toString() {
        return String.format("%.5f, %.5f, %.5f", y, x, z);
    }
}
