package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant.PracticeCode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Random;

@Disabled
@Autonomous(name="Outdated My Boy", group="Robot")
public class firstTryAutonomous extends OpMode{
    private Random r = new Random();
    private int autoThingsCount = 0;

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
    public void encoderDrive(double speed, double leftInches, double rightInches){
        if(!leftBackDrive.isBusy() && autoThingsCount == 0){

            leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
            leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
            rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));
            rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));

            leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
            autoThingsCount++;
            motorSpeed(speed);
        }else if(!leftBackDrive.isBusy() && autoThingsCount == 1){


            leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
            leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH));
            rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));
            rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH));

            leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
            autoThingsCount++;
            motorSpeed(speed);
        }
    }
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public BNO055IMU imu = null;
    static final double COUNTS_PER_MOTOR_REV = 28.0;
    static final double DRIVE_GEAR_REDUCTION = 20.0;
    static final double WHEEL_DIAMETER_MM = 96.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_MM * Math.PI);
    @Override
    public void init(){
        telemetry.addData("Status", "Initialized");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
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
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
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
    @Override
    public void loop(){
        double MotorAngleCompensationFactor = 1/((Math.sin((Math.atan(12.25/20))*Math.sin(Math.PI/4)+(Math.cos((Math.atan(12.25/20)))*Math.cos(Math.PI/4)))));
        MotorAngleCompensationFactor*=(1);//MotorAngleCompensationFactor*0.75
        double mmValue = (Math.sqrt(12.25*12.25 + 20*20)*(2*Math.PI)/4)*MotorAngleCompensationFactor;

        if(getConePosition() == 0)
        {
            if(!motorBusyCheck())
            {

                leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
                motorSpeed(1.0);
             }else if(!motorBusyCheck() && motorTargetCheck())
             {

                leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setPower(1);
                leftFrontDrive.setPower(-1);
                rightBackDrive.setPower(1);
                rightFrontDrive.setPower(-1);
             }else if(!motorBusyCheck() && motorTargetCheck())
             {
                motorSpeed(0);
             }
        }else if(getConePosition() == 1)
        {
            if(!motorBusyCheck())
            {

                leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
                motorSpeed(1.0);
             }else if(!motorBusyCheck() && motorTargetCheck())
             {
                motorSpeed(0);
             }
        }else if(getConePosition() == 2)
        {
            if(!motorBusyCheck())
            {

                leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(metersToInches(1.0) * COUNTS_PER_INCH));
                leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
                motorSpeed(1.0);
             }else if(!motorBusyCheck() && motorTargetCheck())
             {

                leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + (int)(metersToInches(0.93) * COUNTS_PER_INCH));
                leftFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setMode(RunMode.RUN_TO_POSITION);
                rightFrontDrive.setMode(RunMode.RUN_TO_POSITION);
                rightBackDrive.setMode(RunMode.RUN_TO_POSITION);
                leftBackDrive.setPower(-1);
                leftFrontDrive.setPower(1);
                rightBackDrive.setPower(-1);
                rightFrontDrive.setPower(1);
             }else if(!motorBusyCheck() && motorTargetCheck())
             {
                motorSpeed(0);
             }
        }
        telemetry.addData("gyro", imu.getAngularOrientation().firstAngle);
    }
    public double metersToInches(double meters){
        double inches = meters * 39.37;
        return inches;
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
