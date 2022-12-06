package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Teleop My Boy", group="Robot")
public class iterativeTeleop extends OpMode{
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;
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
        leftFrontDrive.setPower(lfPow);
        leftBackDrive.setPower(lbPow);
        rightFrontDrive.setPower(rfPow);
        rightBackDrive.setPower(rbPow);
    }
    @Override
    public void stop(){
    }
}