package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant.PracticeCode;

import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
//import org.firstinspires.ftc.robotcore.external.Func;
//import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Disabled
@TeleOp(name="Robot: Auto with sensor My Boy", group="Robot")
public class sensorTestAutonomous extends OpMode{
    public int count = 0;
    int hasRun = 0;

    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }
    public void encoderDrive(double speed, double leftInches, double rightInches) {
        int newlfTarget;
        int newlbTarget;
        int newrfTarget;
        int newrbTarget;
        telemetry.addLine("debug1");
        hasRun++;
        if (!leftBackDrive.isBusy() && count==1) {
            telemetry.addLine("debug2");
            newlfTarget = leftFrontDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newlbTarget = leftBackDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newrfTarget = rightFrontDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newrbTarget = rightBackDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            leftFrontDrive.setTargetPosition(newlfTarget);
            leftBackDrive.setTargetPosition(newlbTarget);
            rightFrontDrive.setTargetPosition(newrfTarget);
            rightBackDrive.setTargetPosition(newrbTarget);

            leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
            motorSpeed(speed);
            if(motorBusyCheck() == false && count ==1 && motorTargetCheck()) {
                count++;
            }
        }else if(!leftBackDrive.isBusy() && count==3){
            telemetry.addLine("debug3");
            newlfTarget = leftFrontDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newlbTarget = leftBackDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newrfTarget = rightFrontDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newrbTarget = rightBackDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftFrontDrive.setTargetPosition(newlfTarget);
            leftBackDrive.setTargetPosition(newlbTarget);
            rightFrontDrive.setTargetPosition(newrfTarget);
            rightBackDrive.setTargetPosition(newrbTarget);

            leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
            motorSpeed(speed);
            if(motorBusyCheck() == false && count ==3 && motorTargetCheck()) {
                count++;
            }
        }
    }
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    BNO055IMU imu;
    static final double COUNTS_PER_MOTOR_REV = 1680.0;
    static final double DRIVE_GEAR_REDUCTION = 1;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI);
    public Orientation lastAngles = new Orientation();
    public double globalAngle, power = .30, correction;
    @Override
    public void init(){
        telemetry.addData("Status", "Initialized");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        leftBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu.initialize(parameters);
    }
    @Override
    public void init_loop(){
        telemetry.addData("gyro crap", imu.isGyroCalibrated());
    }
    @Override
    public void start(){
    }
    /*
    90 clock
    180 counter clock
    720 clock
     */
    @Override
    public void loop() {
        //imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if(count == 0) {
            rotate(90, 0.5);
            telemetry.addData("count: ", String.valueOf(count));
        }else if(count==1){
            encoderDrive(0.5, 39.37, 39.37);
            telemetry.addData("count: ", String.valueOf(count));
        } else if(count == 2){
            telemetry.addLine("debug4");
            rotate(-90, 0.5);
            telemetry.addData("count: ", String.valueOf(count));
            telemetry.addLine("debug6");
        } else if(count==3){
            encoderDrive(0.5, 39.37, 39.37);
            telemetry.addData("count: ", String.valueOf(count));
        }else if(count == 4) {
            rotate(180, 0.5);
            telemetry.addData("count: ", String.valueOf(count));
        }else if (count==5){
            rotate(0, 0.5);
            telemetry.addData("count: ", String.valueOf(count));
        }else if (count==6){
            motorSpeed(0);
            telemetry.addData("count: ", String.valueOf(count));
        }
        telemetry.addData("gyro", imu.getAngularOrientation().firstAngle);
        telemetry.addLine("" + hasRun);
    }
    public double getAngle()
    {
        return imu.getAngularOrientation().firstAngle;
    }
    public void rotate(double degrees, double power)
    {
        telemetry.addLine("debug5 ");
        if(degrees>=0)
        {
            leftBackDrive.setPower(-power);
            leftFrontDrive.setPower(-power);
            rightBackDrive.setPower(power);
            rightFrontDrive.setPower(power);
            if (Math.abs(getAngle()-degrees) < 2.0){
                count++;
            }
        } else if(degrees<0) {
            leftBackDrive.setPower(power);
            leftFrontDrive.setPower(power);
            rightBackDrive.setPower(-power);
            rightFrontDrive.setPower(-power);
            if (Math.abs(getAngle()-degrees) < 2.0){
                count++;
            }
        }
    }

    public boolean motorBusyCheck(){
        if (leftFrontDrive.isBusy() && rightFrontDrive.isBusy() && leftBackDrive.isBusy() && rightBackDrive.isBusy()){
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean motorTargetCheck(){
        if(leftFrontDrive.getCurrentPosition() == leftFrontDrive.getTargetPosition() && rightFrontDrive.getCurrentPosition() == rightFrontDrive.getTargetPosition() && leftBackDrive.getCurrentPosition() == leftBackDrive.getTargetPosition() && rightBackDrive.getCurrentPosition() == rightBackDrive.getTargetPosition()){
            return true;
        }
        else
        {
            return false;
        }
    }
    @Override
    public void stop(){
    }
}
