package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

<<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/iterativeTeleop.java
@TeleOp(name="Teleop My Boy", group="Robot")
public class iterativeTeleop extends OpMode{
========
@TeleOp(name="All Function Teleop", group="Robot")
public class allFunctionTeleop extends OpMode{
>>>>>>>> 188858_code:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/allFunctionTeleop.java
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;

    boolean clawOpen = false;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotor");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Status", "Ready to run");
        telemetry.update();
    }
    @Override
    public void init_loop(){
    }
    @Override
    public void start(){
    }
    @Override
    public void loop(){
        double drive = gamepad1.left_stick_y;
        double turn  =  -gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;


        double lbPow = drive - strafe + turn;
        double rbPow = drive + strafe - turn;
        double lfPow = drive + strafe + turn;
        double rfPow = drive - strafe - turn;
        double divisor = Math.max(Math.max(lfPow, lbPow), Math.max(rfPow, rbPow));
        if(divisor > 0.7)
        {
            lbPow/=divisor;
            rbPow/=divisor;
            lfPow/=divisor;
            rfPow/=divisor;
        }
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
            rightClaw.setPosition(0.36);
        }
        int increment = 4;
        if(gamepad1.dpad_up) {
            spoolMotor.setPower(-0.1);
            spoolMotor.setTargetPosition(spoolMotor.getCurrentPosition()+increment);
        } else if(gamepad1.dpad_down) {
            spoolMotor.setPower(0.1);
            spoolMotor.setTargetPosition(spoolMotor.getCurrentPosition()+increment);
        }
        leftFrontDrive.setPower(lfPow);
        leftBackDrive.setPower(lbPow);
        rightFrontDrive.setPower(rfPow);
        rightBackDrive.setPower(rbPow);
        telemetry.addLine("A to open claw, B to close");
        telemetry.addLine("Dpad up and down to bring arm up and down");
    }
    @Override
    public void stop(){
    }
}