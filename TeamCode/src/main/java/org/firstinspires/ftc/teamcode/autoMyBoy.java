package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Robot: Auto My Boy", group="Robot")
public class autoMyBoy extends OpMode{
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    private ElapsedTime runtime = new ElapsedTime();
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
    }
    @Override
    public void init_loop(){
    }
    @Override
    public void start(){
        runtime.reset();
    }
    @Override
    public void loop(){
        //part 1: robot moves forward at half speed for 3 seconds
        if(runtime.seconds() <= 3.0){
            motorSpeed(1.0);
        }else if(runtime.seconds() <= 3.5){ //turn right
            leftBackDrive.setPower(1);
            leftFrontDrive.setPower(1);
            rightBackDrive.setPower(-1);
            rightFrontDrive.setPower(-1);
        }else if(runtime.seconds() <= 6.5) { //drive
            motorSpeed(1.0);
        }else if(runtime.seconds() <= 11){ //left strafe
            leftBackDrive.setPower(1);
            leftFrontDrive.setPower(-1);
            rightBackDrive.setPower(1);
            rightFrontDrive.setPower(-1);
        }else if(runtime.seconds() <= 11.5){ //turn left
            leftBackDrive.setPower(-1);
            leftFrontDrive.setPower(-1);
            rightBackDrive.setPower(1);
            rightFrontDrive.setPower(1);
        }else if(runtime.seconds() <= 14.5){ //drive
            motorSpeed(1);
        }else { //stop
            motorSpeed(0);
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }
    @Override
    public void stop(){
    }
}
