package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.DriveBaseTask;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="Robot: timed sofee event does something :')", group="Robot")
public class timedFakeAutonomousSoThatItDoesSomething extends OpMode{
    public void motorSpeed(double speed){
        leftBackDrive.setPower(speed);
        leftFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
    }

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

    public CHubIMU imu = null;
    RobotWebcam webcam = null;
    myBoyDrivebase drive = null;
    ConeStateFinder.ConeState coneState = null;
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;
    public int conePosition;
    boolean clawOpen = false;

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
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
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
        conePosition = getConePosition();

    }
    @Override
    public void loop() {
        //strafe left to put in cone
        if (runtime.seconds() <= 1.5) {
            leftBackDrive.setPower(1);
            leftFrontDrive.setPower(-1);
            rightBackDrive.setPower(1);
            rightFrontDrive.setPower(-1);
        }else if (runtime.seconds() <= 2.5){
            leftClaw.setPosition(1.0);
            rightClaw.setPosition(0.45);
        }else if (runtime.seconds() <= 4.0) { //strafe back
            leftBackDrive.setPower(-1);
            leftFrontDrive.setPower(1);
            rightBackDrive.setPower(-1);
            rightFrontDrive.setPower(1);
        } else if (conePosition == 0 && runtime.seconds() <= 5.5) { //left cone
            motorSpeed(0.5); //forward
            if (runtime.seconds() <= 7.0) { //strafe left
                leftBackDrive.setPower(1);
                leftFrontDrive.setPower(-1);
                rightBackDrive.setPower(1);
                rightFrontDrive.setPower(-1);
            }
        }else if (conePosition == 1 && runtime.seconds() <= 5.5) { //middle cone
            motorSpeed(0.5); //forward
        }else if (conePosition == 2 && runtime.seconds() <= 5.5){ //right cone
            motorSpeed(0.5); //forward
            if(runtime.seconds() <= 7.0){ //strafe right
                leftBackDrive.setPower(-1);
                leftFrontDrive.setPower(1);
                rightBackDrive.setPower(-1);
                rightFrontDrive.setPower(1);
            }
        }else{ //stop
            motorSpeed(0);
            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }
    @Override
    public void stop(){
    }
}
