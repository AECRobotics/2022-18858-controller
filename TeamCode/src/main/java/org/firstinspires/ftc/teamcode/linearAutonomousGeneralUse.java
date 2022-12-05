package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


import java.util.Random;
@Autonomous(name="autonomous My Boy", group="Robot")
public class linearAutonomousGeneralUse extends LinearOpMode {
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public BNO055IMU imu = null;
    static final double COUNTS_PER_MOTOR_REV = 28.0;
    static final double DRIVE_GEAR_REDUCTION = 20.0;
    static final double WHEEL_DIAMETER_IN = 3.78;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_IN * Math.PI);
    @Override
    public void runOpMode(){
        telemetry.addData("Status","Initialized");
        leftBackDrive =hardwareMap.get(DcMotor.class,"backleft");
        leftFrontDrive =hardwareMap.get(DcMotor.class,"frontleft");
        rightBackDrive =hardwareMap.get(DcMotor.class,"backright");
        rightFrontDrive =hardwareMap.get(DcMotor.class,"frontright");
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        telemetry.addData("Status","Ready to run");
        telemetry.update();

        leftBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        imu =hardwareMap.get(BNO055IMU.class,"imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu.initialize(parameters);
        telemetry.addData("gyro crap", imu.isGyroCalibrated());

        waitForStart();
        encoderDrive(1.0,39.37,39.37,39.37,39.37);
        telemetry.addLine("end of call to encoder drive");
        while(opModeIsActive()){}
        /*pseudocode for terminal red start
        encoderDrive(-1,1,1,-1,metersToInches(~0.8),timeoutS);
        open claw and drop cone
        encoderDrive(1,-1,-1,1,metersToInches(~0.8),timeoutS);

        blue start
        encoderDrive(1,-1,-1,1,metersToInches(~0.8),timeoutS);
        open claw and drop cone
        encoderDrive(-1,1,1,-1,metersToInches(~0.8),timeoutS);

        if we start away from terminal this doesn't apply :)
         */
        /*
        if(getConePosition() == 0){
            encoderDrive(1.0,1.0,1.0,1.0,metersToInches(1),5.0);
            encoderDrive(-1.0,1.0,1.0,-1.0,metersToInches(0.93),5.0);
        }else if(getConePosition() == 1){
            encoderDrive(1.0,1.0,1.0,1.0,metersToInches(1),5.0);
        }else if(getConePosition() == 2){
            encoderDrive(1.0,1.0,1.0,1.0,metersToInches(1),5.0);
            encoderDrive(1.0,-1.0,-1.0,1.0,metersToInches(0.93),5.0);
        }
        while(opModeIsActive()){}
        */
    }
    private Random r = new Random();
    public int getConePosition() {
        //0 = left most
        //1 = middle
        //2 = right most
        return (int)Math.floor(r.nextDouble()*3.0);
    }
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }

    public void encoderDrive(double speed, double inchesLF, double inchesLB, double inchesRF, double inchesRB){
        if(opModeIsActive()){
            leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(inchesLF * COUNTS_PER_INCH));
            leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(inchesLB * COUNTS_PER_INCH));
            rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(inchesRF * COUNTS_PER_INCH));
            rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(inchesRB * COUNTS_PER_INCH));

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            motorSpeed(Math.abs(speed));
        }
        telemetry.addLine("END OF IF STATEMENT IN ENCODERDRIVE");
        motorSpeed(0);
        leftBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(RunMode.RUN_USING_ENCODER);
        telemetry.addLine("end of encoder drive");
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
    public double metersToInches(double meters){
        double inches = meters * 39.37;
        return inches;
    }
}
