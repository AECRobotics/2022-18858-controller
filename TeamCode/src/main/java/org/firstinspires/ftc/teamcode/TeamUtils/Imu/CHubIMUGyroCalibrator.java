package org.firstinspires.ftc.teamcode.TeamUtils.Imu;

import com.qualcomm.hardware.bosch.BNO055IMU;

public class CHubIMUGyroCalibrator implements Runnable {
    private static int calibrators = 0;
    public static int tries = 15;
    private Thread t;
    private boolean calibrated = false;
    private boolean running = false;

    private BNO055IMU imu;
    public CHubIMUGyroCalibrator(BNO055IMU imu) {
        this.imu = imu;
    }

    public void run() {
        this.running = true;
        try {

            for(int i = 0; i < CHubIMUGyroCalibrator.tries && !this.calibrated; i++) {
                Thread.sleep(1000);
                this.calibrated = this.imu.isGyroCalibrated();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.running =  false;
    }

    public boolean isCalibrated() {
        return this.calibrated;
    }

    public boolean isRunning() {
        return this.running;
    }


    public void start() {
        if(t == null) {
            CHubIMUGyroCalibrator.calibrators++;
            t = new Thread(this, "" + CHubIMUGyroCalibrator.calibrators);
            t.start();
        }
    }
}
