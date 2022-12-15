package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;

import java.io.File;

@Disabled
@TeleOp(name="Generate New Calibration Data", group="Calibration")
public class GenerateNewCalibrationData extends OpMode
{

    CHubIMU imu;
    @Override
    public void init() {
        BNO055IMU imuB = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new CHubIMU(imuB);
    }

    @Override
    public void init_loop() {
        telemetry.addData("Compass calibration status", imu.getGyroCalibrationStatus());
        telemetry.addData("Accelerometer calibration status", imu.getAccelerometerCalibrationStatus());
        telemetry.addData("Magnetometer calibration status", imu.getMagnetometerCalibrationStatus());
    }

    @Override
    public void start() {
        imu.startPositionTracking();
    }


    @Override
    public void loop() {
        Position estimatedPosition = imu.getPosition();
        telemetry.addData("X", estimatedPosition.x);
        telemetry.addData("Y", estimatedPosition.y);
        telemetry.addData("Z", estimatedPosition.z);
    }

    @Override
    public void stop() {
        imu.stopPositionTracking();
        BNO055IMU.CalibrationData calibrationData = imu.getImu().readCalibrationData();

        // Save the calibration data to a file. You can choose whatever file
        // name you wish here, but you'll want to indicate the same file name
        // when you initialize the IMU in an opmode in which it is used. If you
        // have more than one IMU on your robot, you'll of course want to use
        // different configuration file names for each.
        String filename = "AdafruitIMUCalibration.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, calibrationData.serialize());
        telemetry.log().add("saved to '%s'", filename);
    }
}
