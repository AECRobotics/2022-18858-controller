package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;

public class CHubIMUAccelCalibrator implements Runnable {
    private static int calibrators = 0;
    public static int tries = Integer.MAX_VALUE;
    private Thread t;
    private boolean calibrated = false;
    private boolean running = false;

    private BNO055IMU imu;
    public CHubIMUAccelCalibrator(BNO055IMU imu) {
        this.imu = imu;
    }

    public void run() {
        this.running = true;
        try {

            for(int i = 0; i < CHubIMUAccelCalibrator.tries && !this.calibrated; i++) {
                Thread.sleep(1000);
                this.calibrated = this.imu.isAccelerometerCalibrated();
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
            CHubIMUAccelCalibrator.calibrators++;
            t = new Thread(this, "" + CHubIMUAccelCalibrator.calibrators);
            t.start();
        }
    }
}
