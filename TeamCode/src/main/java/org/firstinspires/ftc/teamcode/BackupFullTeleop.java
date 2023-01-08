package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.myBoyDrivebase;
import org.firstinspires.ftc.teamcode.TeamUtils.CHubIMU;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;
import org.firstinspires.ftc.teamcode.TeamUtils.Spool;
import org.firstinspires.ftc.teamcode.TeamUtils.Wheel;

import java.util.HashMap;


@TeleOp(name="Backup All Function Teleop", group="Robot")
public class BackupFullTeleop extends OpMode{
    public myBoyDrivebase drive = null;
    public CHubIMU imu = null;

    public Spool spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;

    enum ArmPosition {
        GROUND,
        LOW,
        MIDDLE,
        HIGH
    }

    public ArmPosition levelToArmPos(int level) {
        switch (level) {
            case 1:
                return ArmPosition.GROUND;
            case 2:
                return ArmPosition.LOW;
            case 3:
                return ArmPosition.MIDDLE;
            case 4:
                return ArmPosition.HIGH;
            default:
                return ArmPosition.GROUND;
        }
    }

    public HashMap<ArmPosition, Double> armPositionHeights = new HashMap<ArmPosition, Double>();

    boolean clawOpen = false;
    int spoolLevel = 1;
    double spoolPosition;

    boolean isLastGamepadDpadRight = false;
    boolean lastGamepadDpadLeft = false;
    boolean lastGamepadDpadUp = false;
    boolean lastGamepadDpadDown = false;

    @Override
    public void init() {
        armPositionHeights.put(ArmPosition.GROUND, 0.0);
        armPositionHeights.put(ArmPosition.LOW, 200.0);
        armPositionHeights.put(ArmPosition.MIDDLE, 400.0);
        armPositionHeights.put(ArmPosition.HIGH, 600.0);
        DcMotor leftBackDrive = hardwareMap.get(DcMotor.class, "backleft"); //1
        DcMotor leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft"); //0
        DcMotor rightBackDrive = hardwareMap.get(DcMotor.class, "backright"); //4
        DcMotor rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright"); //2

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BNO055IMU imub = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new CHubIMU(imub);
        drive = new myBoyDrivebase(rightFrontDrive, rightBackDrive, leftFrontDrive, leftBackDrive, imu);
        spoolMotor = new Spool(hardwareMap.get(DcMotor.class, "spoolmotor"), 11.5, 60.0, 28.0, 0.0, 23.0, 50.0, 15.0, 0.907);
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setTargetPosition(0);
        //spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Ready to run");
        telemetry.update();
    }
    @Override
    public void init_loop(){
    }
    @Override
    public void start(){
    }

    public void setSpoolPosition() {
        this.spoolMotor.setTargetPosition(this.spoolMotor.convertMMToEncoderPosition(
                armPositionHeights.get(levelToArmPos(spoolLevel))));
    }

    @Override
    public void loop(){
        double forward = gamepad1.left_stick_y;
        double turn  =  -gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;
        drive.drive(forward, strafe, turn);

        if(gamepad1.a) {
            clawOpen = true;
        } else if(gamepad1.b) {
            clawOpen = false;
        }
        if(clawOpen) {
            leftClaw.setPosition(1.0);
            rightClaw.setPosition(0.45);
        } else {
            leftClaw.setPosition(1.0);
            rightClaw.setPosition(0.28);
        }

        if(gamepad1.dpad_up && !lastGamepadDpadUp) {
            if(spoolLevel < 4) {
                spoolLevel++;
                setSpoolPosition();
            }
        }
        if(gamepad1.dpad_down && !lastGamepadDpadDown) {
            if(spoolLevel > 2) {
                spoolLevel--;
                setSpoolPosition();
            }
        }
        if(gamepad1.dpad_left && !lastGamepadDpadLeft) {
            armPositionHeights.put(levelToArmPos(spoolLevel), armPositionHeights.get(levelToArmPos(spoolLevel))-10.0);
            setSpoolPosition();
        }
        if(gamepad1.dpad_right && !isLastGamepadDpadRight) {
            armPositionHeights.put(levelToArmPos(spoolLevel), armPositionHeights.get(levelToArmPos(spoolLevel))+10.0);
            setSpoolPosition();
        }

        telemetry.addLine("A to open claw, B to close");
        telemetry.addLine("Dpad up and down to bring arm up and down");
    }
    @Override
    public void stop(){
    }
}