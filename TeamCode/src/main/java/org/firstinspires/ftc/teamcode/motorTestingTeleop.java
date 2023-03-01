package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="Motor Testing Teleop", group="Debug")
//@Disabled
public class motorTestingTeleop extends OpMode{
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
        //spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotor");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        //spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //spoolMotor.setTargetPosition(0);
        //spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Ready to run");
        telemetry.update();
    }
    @Override
    public void init_loop(){
    }
    @Override
    public void start(){
    }

    enum Motors {
        LF,
        LB,
        RF,
        RB
    }

    Motors curMotor = Motors.LF;

    public boolean testingPower = true;
    public boolean testingTarget = false;
    public boolean hasRun = false;
    @Override
    public void loop(){

        if(gamepad1.a) {
            curMotor = Motors.LF;
        }
        if(gamepad1.b) {
            curMotor = Motors.LB;
        }
        if(gamepad1.x) {
            curMotor = Motors.RF;
        }
        if(gamepad1.y) {
            curMotor = Motors.RB;
        }
        if(gamepad1.dpad_up) {
            testingTarget = false;
        }
        if(gamepad1.dpad_down && !testingTarget) {
            testingTarget = true;
            hasRun = false;
        }
        if(gamepad1.dpad_left) {
            getCurMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getCurMotor().setPower(0.2);
        }
        double power = gamepad1.left_stick_y;
        telemetry.addLine("power: " + power);
        telemetry.addLine("testing motor: " + curMotor.name());
        telemetry.addLine("current position" + getCurMotor().getCurrentPosition());
        telemetry.addLine("target position" + getCurMotor().getTargetPosition());
        if(testingPower) {
            if(!hasRun) {
                getCurMotor().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hasRun = true;
            }
            getCurMotor().setPower(power);
        } else if(testingTarget && !hasRun) {
            getCurMotor().setTargetPosition(1000);
            getCurMotor().setMode(DcMotor.RunMode.RUN_TO_POSITION);
            getCurMotor().setPower(0.1);
            hasRun = true;
        }
        //spoolMotor.setPower(gamepad1.left_stick_y);

    }

    public DcMotor getCurMotor() {
        switch (curMotor) {
            case LB:
                return leftBackDrive;
            case LF:
                return leftFrontDrive;
            case RB:
                return rightBackDrive;
            case RF:
                return rightFrontDrive;
        }
        return null;
    }
    @Override
    public void stop(){
    }
}