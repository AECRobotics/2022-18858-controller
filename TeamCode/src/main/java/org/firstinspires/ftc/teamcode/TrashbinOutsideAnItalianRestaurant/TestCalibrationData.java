package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.TeamUtils.Imu.CHubIMU;


@TeleOp(name="Test Calibration Data", group="Debug")
@Disabled
public class TestCalibrationData extends OpMode
{

    CHubIMU imu;
    @Override
    public void init() {
        BNO055IMU imuB = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new CHubIMU(imuB, "AdafruitIMUCalibration.json");
    }

    @Override
    public void init_loop() {
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
        Orientation estimatedOrientation = imu.getOrientation();
        telemetry.addData("", estimatedOrientation.axesOrder);
        telemetry.addData("", estimatedOrientation.firstAngle);
        telemetry.addData("", estimatedOrientation.secondAngle);
        telemetry.addData("", estimatedOrientation.thirdAngle);

    }

    @Override
    public void stop() {
        imu.stopPositionTracking();
    }
}
