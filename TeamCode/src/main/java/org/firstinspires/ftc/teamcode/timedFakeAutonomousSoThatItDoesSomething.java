package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.HolonomicDriveBase;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="Robot: timed sofee event does something :')", group="Robot")
public class timedFakeAutonomousSoThatItDoesSomething extends OpMode{
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }

    public myBoyDrivebase drive = null;
    public CHubIMU imu = null;
    RobotWebcam webcam = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;
    public int conePosition;
    boolean clawOpen = false;


    public int getConePosition() {
        //0 = left most
        //1 = middle
        //2 = right most
        //return (int)Math.floor(r.nextDouble()*3.0);
        switch (ConeStateFinder.getConeState(webcam)) {
            case LEFT:
                return 0;
            case MIDDLE:
                return 1;
            case RIGHT:
                return 2;
            default:
                return -1;
        }
    }



    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init(){
        telemetry.addData("Status", "Initialized");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        webcam = new RobotWebcam(hardwareMap.get(WebcamName.class, "webcam"));
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BNO055IMU imub = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new CHubIMU(imub);
        drive = new myBoyDrivebase(rightFrontDrive, rightBackDrive, leftFrontDrive, leftBackDrive, imu);
    }
    @Override
    public void init_loop(){
        telemetry.addData("Status", "Webcam ready");
        telemetry.update();
    }
    @Override
    public void start(){
        runtime.reset();
        conePosition = getConePosition();
        resetRuntime();
    }
    @Override
    public void loop() {
        telemetry.addData("time:", getRuntime());
        telemetry.addData("cone:", conePosition);
        /*Autonomous including useless strafe left to terminal and "drop" cone (then pick it up again)
        //strafe left to put in cone
        if (runtime.seconds() <= 3) {
            drive.drive(0.0,-0.25,0.0);
            telemetry.addData("time:", getRuntime());
        }else if (runtime.seconds() <= 7.0){
            motorSpeed(0);
            leftClaw.setPosition(ClawPositions.leftServoOpen);
            rightClaw.setPosition(ClawPositions.rightServoOpen);
            telemetry.addData("time:", getRuntime());
        }else if (runtime.seconds() <= 9.0) { //strafe back
            telemetry.addData("time:", getRuntime());
            drive.drive(0.0, 0.25, 0.0);
            leftClaw.setPosition(ClawPositions.leftServoClosed);
            rightClaw.setPosition(ClawPositions.rightServoClosed);
            telemetry.addData("time:", getRuntime());
        } else if (conePosition == 0 && runtime.seconds() <= 11.25) { //left cone
            drive.drive(0.25, 0.0, 0.0); //forward
        }else if (conePosition == 0 && runtime.seconds() <= 13.75) { //strafe left
            drive.drive(0.0,-0.25, 0.0);
        }else if (conePosition == 1 && runtime.seconds() <= 11.25) { //middle cone
            drive.drive(0.25, 0.0, 0.0); //forward
        }else if (conePosition == 2 && runtime.seconds() <= 11.25) {//right cone
            drive.drive(0.25, 0.0, 0.0); //forward
        }else if(conePosition == 2 && runtime.seconds() <= 13.75){ //strafe right
            drive.drive(0.0, 0.25, 0.0);
        }else{ //stop
            motorSpeed(0);
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    } */
        if (conePosition == 0 && runtime.seconds() <= 2.25) { //left cone
            drive.drive(0.25, 0.0, 0.0); //drive forward
        } else if (conePosition == 0 && runtime.seconds() <= 4.75) {
            drive.drive(0.0, -0.25, 0.0); //strafe left
        } else if (conePosition == 1 && runtime.seconds() <= 2.25) { //middle cone
            drive.drive(0.25, 0.0, 0.0); //drive forward
        } else if (conePosition == 2 && runtime.seconds() <= 2.25) { //right cone
            drive.drive(0.25, 0.0, 0.0); //drive forward
        } else if (conePosition == 2 && runtime.seconds() <= 4.75) {
            drive.drive(0.0, 0.25, 0.0); //strafe right
        } else { //stop
            motorSpeed(0);
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }
    @Override
    public void stop(){
    }
}
