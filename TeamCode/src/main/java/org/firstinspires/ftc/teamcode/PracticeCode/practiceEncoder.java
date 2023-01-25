package org.firstinspires.ftc.teamcode.PracticeCode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
@Disabled
//@Autonomous(name="Robot: Auto with encoder My Boy", group="Robot")
public class practiceEncoder extends OpMode{
    private int autoThingsCount = 0;
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }
    public void encoderDrive(double speed, double leftInches, double rightInches){
        int newlfTarget;
        int newlbTarget;
        int newrfTarget;
        int newrbTarget;
        if(!leftBackDrive.isBusy() && autoThingsCount == 0){
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
            autoThingsCount++;
            motorSpeed(speed);
        }/*else if(!leftBackDrive.isBusy() && autoThingsCount == 1){
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
            autoThingsCount++;
            motorSpeed(speed);
        }  */
    }
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public BNO055IMU imu = null;
    static final double COUNTS_PER_MOTOR_REV = 1680.0;
    static final double DRIVE_GEAR_REDUCTION = 7.0/9.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI);
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

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        double MotorAngleCompensationFactor = 1/((Math.sin((Math.atan(7/6.5))*Math.sin(Math.PI/4)+(Math.cos((Math.atan(7/6.5)))*Math.cos(Math.PI/4)))));
        MotorAngleCompensationFactor*=(1.276143212);//MotorAngleCompensationFactor*0.75
        double inchValue = (Math.sqrt(7.0*7.0 + 6.5*6.5)*(2*Math.PI)/4)*MotorAngleCompensationFactor;
        encoderDrive(1, 39.37, 39.37); //can change num values
        //encoderDrive(1.0, inchValue, -inchValue);
        telemetry.addData("gyro", this.imu.getAngularOrientation().firstAngle);
    }
    @Override
    public void stop(){
    }
}
