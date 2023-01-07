package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="All Function Teleop", group="Robot")
public class fullyFunctionalTeleop extends OpMode{
    public DcMotor leftBackDrive = null;
    public DcMotor rightBackDrive = null;
    public DcMotor leftFrontDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor spoolMotor = null;
    public Servo rightClaw = null;
    public Servo leftClaw = null;

    boolean clawOpen = false;
    static final double COUNTS_PER_MOTOR_REV = 1680.0;
    static final double SPOOL_GEAR_REDUCTION = 1/60;
    static final double SPOOL_DIAMETER = 23.0; //mm
    static final double SPOOL_CIRCUMFERENCE = 2 * Math.PI * (SPOOL_DIAMETER/2);
    static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV * SPOOL_GEAR_REDUCTION)/(SPOOL_DIAMETER * Math.PI); //HOW MUCH MM PER TICK
    static final double SPOOL_TICKS = COUNTS_PER_MM * SPOOL_CIRCUMFERENCE;
    double spoolPosition;
    double spoolTarget;

    boolean isLastGamepadDpadRight = false;
    boolean lastGamepadDpadLeft = false;
    boolean lastGamepadDpadUp = false;
    boolean lastGamepadDpadDown = false;
//current target pos / spoolticks
    public int ticksToMm(int ticks){
        return spoolMotor.getTargetPosition()/(int)SPOOL_TICKS;
    }
    public void arm(double mmHeight){
        spoolMotor.setTargetPosition(spoolMotor.getCurrentPosition() + (int)(mmHeight*SPOOL_TICKS));
        spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        spoolMotor.setPower(0.5);
    }

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
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setTargetPosition(0);
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

    @Override
    public void loop(){
        double drive = gamepad1.left_stick_y;
        double turn  =  -gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_x;


        double lbPow = drive + strafe + turn;
        double rbPow = drive + strafe - turn;
        double lfPow = drive - strafe + turn;
        double rfPow = drive - strafe - turn;
        double divisor = Math.max(Math.max(lfPow, lbPow), Math.max(rfPow, rbPow));
        if(divisor > 0.5)
        {
            lbPow/=divisor;
            rbPow/=divisor;
            lfPow/=divisor;
            rfPow/=divisor;
        }
        if(gamepad1.left_bumper) {
            clawOpen = true;
        } else if(gamepad1.right_bumper) {
            clawOpen = false;
        }
        if(clawOpen) {
            leftClaw.setPosition(1.0);
            rightClaw.setPosition(0.45);
        } else {
            leftClaw.setPosition(1.0);
            rightClaw.setPosition(0.28);
        }
       if(gamepad1.a){
            //bottom
           spoolTarget = 20; //mm
        }
        if(gamepad1.b){ //add condition to check for height
            // low
            spoolTarget = 470; //mm
            ticksToMm(spoolMotor.getCurrentPosition()); //current spool ticks in mm?
        }
        if(gamepad1.x){
            //medium
            spoolTarget = 725; //mm
        }
        if(gamepad1.y){
            //high
            spoolTarget = 1000; //mm
        }
        /*

        4 heights
        max - 147cm = 1.47 m
        min - 2cm = 0.02 m
        low - 33.5 + 13.5 = 47 cm
        mid - 59 + 13.5 = 72.5 cm
        hi - 84.5 + 13.5 = 98 cm = approx. 1 m
        11.5

        drivers -
            harris :)
            rainy :)
            eryx :)
            audrey :)

        the drivers are driving me insane >:(
        we also want the dpad for adjustments HOLD THE BUTTONS >:(
        we also want the lr and lf buttons to open the claw
        we as in the drivers >:(

        dpad - I
        abxy - III
         */
        if(gamepad1.dpad_up && !lastGamepadDpadUp) {
            spoolMotor.setTargetPosition(spoolMotor.getTargetPosition() + 1);
        }
        if(gamepad1.dpad_down && !lastGamepadDpadDown) {

        }
        if (gamepad1.dpad_right){
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }
        if (gamepad1.dpad_left){
            spoolMotor.setPower(0);
        }
        leftFrontDrive.setPower(lfPow);
        leftBackDrive.setPower(lbPow);
        rightFrontDrive.setPower(rfPow);
        rightBackDrive.setPower(rbPow);
        telemetry.addLine("Left Bumper to open claw, Right Bumper to close");
        telemetry.addLine("Dpad up and down to bring arm up and down slowly for adjustments");
    }
    @Override
    public void stop(){
    }
}