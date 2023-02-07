package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class CHubIMU {
    enum CalibrationState {
        CALIBRATING,
        CALIBRATED,
        FAILED
    }
    BNO055IMU imu;
    CHubIMUGyroCalibrator gyroCalibrator;
    CHubIMUAccelCalibrator accelCalibrator;
    CHubIMUMagneCalibrator magneCalibrator;
    AxesOrder defaultAxesOrder = AxesOrder.XYZ;
    public CHubIMU(BNO055IMU imu) {
        this.imu = imu;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        imu.initialize(parameters);
        this.gyroCalibrator = new CHubIMUGyroCalibrator(this.imu);
        this.accelCalibrator = new CHubIMUAccelCalibrator(this.imu);
        this.magneCalibrator = new CHubIMUMagneCalibrator(this.imu);
        this.gyroCalibrator.start();
        this.accelCalibrator.start();
        this.magneCalibrator.start();
    }

    public CHubIMU(BNO055IMU imu, AxesOrder defaultAxesOrder) {
        new CHubIMU(imu);
        this.defaultAxesOrder = defaultAxesOrder;
    }

    public CHubIMU(BNO055IMU imu, String calibrationFile) {
        this(imu);
        File file = AppUtil.getInstance().getSettingsFile(calibrationFile);
        String data = ReadWriteFile.readFile(file);
        BNO055IMU.CalibrationData calibrationData = BNO055IMU.CalibrationData.deserialize(data);
        imu.writeCalibrationData(calibrationData);
    }

    public CalibrationState getGyroCalibrationStatus() {
        CalibrationState state;
        if(this.gyroCalibrator.isCalibrated()) {
            state = CalibrationState.CALIBRATED;
        } else if(this.gyroCalibrator.isRunning()) {
            state = CalibrationState.CALIBRATING;
        } else {
            state = CalibrationState.FAILED;
        }
        return state;
    }

    public CalibrationState getAccelerometerCalibrationStatus() {
        CalibrationState state;
        if(this.accelCalibrator.isCalibrated()) {
            state = CalibrationState.CALIBRATED;
        } else if(this.accelCalibrator.isRunning()) {
            state = CalibrationState.CALIBRATING;
        } else {
            state = CalibrationState.FAILED;
        }
        return state;
    }

    public CalibrationState getMagnetometerCalibrationStatus() {
        CalibrationState state;
        if(this.magneCalibrator.isCalibrated()) {
            state = CalibrationState.CALIBRATED;
        } else if(this.magneCalibrator.isRunning()) {
            state = CalibrationState.CALIBRATING;
        } else {
            state = CalibrationState.FAILED;
        }
        return state;
    }

    public void startPositionTracking() {
        this.imu.startAccelerationIntegration(
                new Position(DistanceUnit.METER, 0.0,0.0,0.0, System.nanoTime()),
                new Velocity(DistanceUnit.METER, 0.0,0.0,0.0, System.nanoTime()),
                15
        );
    }

    public BNO055IMU getImu() {
        return this.imu;
    }

    public Position getPosition() {
        return this.imu.getPosition();
    }

    public void stopPositionTracking() {
        this.imu.stopAccelerationIntegration();
    }

    public Orientation getOrientation() {
        return this.imu.getAngularOrientation(AxesReference.INTRINSIC, defaultAxesOrder, AngleUnit.DEGREES);
    }
}
