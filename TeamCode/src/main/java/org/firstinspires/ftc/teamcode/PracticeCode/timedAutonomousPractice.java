package org.firstinspires.ftc.teamcode.PracticeCode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;

//@Autonomous(name="Robot: Auto My Boy", group="Robot")
public class timedAutonomousPractice extends OpMode{
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }

    public ConeStateFinder.ConeState getConePosition(){
        //0 = left most
        //1 = middle
        //2 = right most
        //return (int)Math.floor(r.nextDouble()*3.0);
        return ConeStateFinder.getConeState(webcam);
    }

    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    private ElapsedTime runtime = new ElapsedTime();

    RobotWebcam webcam = null;
    ConeStateFinder.ConeState coneState = null;


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
        //part 1: strafe left to put cone in terminal
        if(runtime.seconds() <= 1.5){
            leftBackDrive.setPower(1);
            leftFrontDrive.setPower(-1);
            rightBackDrive.setPower(1);
            rightFrontDrive.setPower(-1);
            //open claw to drop cone
        }else if(runtime.seconds() <= 3.0){ //strafe back
            leftBackDrive.setPower(-1);
            leftFrontDrive.setPower(1);
            rightBackDrive.setPower(-1);
            rightFrontDrive.setPower(1);
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
