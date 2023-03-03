package org.firstinspires.ftc.teamcode.CurrentRubySofeeCode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ArmHeightPositions;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;


@TeleOp(name="All Function Teleop", group="Robot")
public class FullyFunctionalTeleop extends OpMode{
    DcMotor leftBackDrive = null;
    DcMotor rightBackDrive = null;
    DcMotor leftFrontDrive = null;
    DcMotor rightFrontDrive = null;
    DcMotor spoolMotor = null;
    Servo rightClaw = null;
    Servo leftClaw = null;
    //CRServo leftCRServo;
    //CRServo rightCRServo;
    //boolean isNotGamepadRight;
    //double leftPower;
    //double rightPower;

    //static final double COUNTS_PER_MM = (GoBildaSpoolConstants.TICKS_PER_REV)/(112.0);
    boolean clawOpen = false;
    double spoolTarget;

    boolean isLastGamepadRightBumper = false;
    boolean isLastGamepadLeftBumper = false;
    /*
    boolean lastGamepadDpadLeft = false;
    boolean lastGamepadDpadUp = false;
    boolean lastGamepadDpadDown = false;

     */
//current target pos / spoolticks
    /*public double mmtoTicks(double mm){
        double ticks = mm*COUNTS_PER_MM;
        if(ticks >= 0 && ticks <= 4400) {
            return ticks;
        } else {
            return spoolMotor.getCurrentPosition();
        }
    }

     */

    public double ticksToMM(int ticks) {
        return ((double)ticks)/ ArmHeightPositions.COUNTS_PER_MM;
    }

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");

        //leftCRServo = hardwareMap.crservo.get("leftCRServo");
        //rightCRServo = hardwareMap.crservo.get("rightCRServo");

        spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotorgobilda");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setTargetPosition(0);
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
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;
        //telemetry.addData("left stick", gamepad1.left_stick_y);

        double lbPow = 0.0;// = drive - strafe + turn;
        double rbPow = 0.0;// = drive + strafe - turn;
        double lfPow = 0.0;// = drive + strafe + turn;
        double rfPow = 0.0;// = drive - strafe - turn;

        if(Math.abs(drive) >= Math.abs(turn) && Math.abs(drive) >= Math.abs(strafe)) {
            lbPow = drive;
            rbPow = drive;
            lfPow = drive;
            rfPow = drive;
        } else if(Math.abs(strafe) > Math.abs(drive) && Math.abs(strafe) >= Math.abs(turn)) {
            lbPow = -strafe;
            rbPow = strafe;
            lfPow = strafe;
            rfPow = -strafe;
        } else if(Math.abs(turn) > Math.abs(strafe)) {
            lbPow = turn;
            rbPow = -turn;
            lfPow = turn;
            rfPow = -turn;
        }
        /*
        if(gamepad1.dpad_left && !isLastGamepadLeftBumper){
            leftPower = -1.0;
            rightPower = 1.0;
        }
        else{
            leftPower = 0.0;
            rightPower = 0.0;
        }

         */

        if(gamepad1.right_bumper) {
            clawOpen = true;
            //isLastGamepadRightBumper = true;
        } else if(gamepad1.left_bumper) {
            clawOpen = false;
            //isLastGamepadRightBumper = false;
        }
        if(clawOpen) {
            leftClaw.setPosition(ClawPositions.leftServoOpen);
            rightClaw.setPosition(ClawPositions.rightServoOpen);
        } else {
            leftClaw.setPosition(ClawPositions.leftServoClosed);
            rightClaw.setPosition(ClawPositions.rightServoClosed);
        }
        if(gamepad1.a){
           //bottom
           spoolTarget = ArmHeightPositions.mmToTicks(ArmHeightPositions.GROUND_PLACEMENT, spoolMotor); //mm
           spoolMotor.setTargetPosition((int)spoolTarget);
           //spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor)); //sets new target pos to height (mm) in ticks
           spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           spoolMotor.setPower(0.5);
        }
        if(gamepad1.b){
            // low
            spoolTarget = ArmHeightPositions.mmToTicks(ArmHeightPositions.LOW_PLACEMENT, spoolMotor); //mm
            spoolMotor.setTargetPosition((int)spoolTarget);
            // spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }
        if(gamepad1.x){
            //medium
            spoolTarget = ArmHeightPositions.mmToTicks(ArmHeightPositions.MEDIUM_PLACEMENT, spoolMotor); //mm
            spoolMotor.setTargetPosition((int)spoolTarget);
            //spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }
        if(gamepad1.y){
            //high
            spoolTarget = ArmHeightPositions.mmToTicks(ArmHeightPositions.HIGH_PLACEMENT, spoolMotor);; //mm
            spoolMotor.setTargetPosition((int)spoolTarget);
            //spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }
        /*

        4 heights
        max - 147cm = 1.47 m
        min - 2cm = 0.02 m
        low - 33.5 + 13.5 = 47 cm
        mid - 59 + 13.5 = 72.5 cm
        hi - 84.5 + 13.5 = 98 cm = approx. 1 m

        spool radius = 11.5

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
        if(gamepad1.dpad_up) {
            spoolMotor.setPower(1.0);
            spoolTarget=ticksToMM(spoolMotor.getCurrentPosition())+10;
            //spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor));
        } else if(gamepad1.dpad_down) {
            spoolMotor.setPower(1.0);
            spoolTarget=ticksToMM(spoolMotor.getCurrentPosition())-10;
            //spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spoolMotor.setTargetPosition((int)ArmHeightPositions.mmToTicks(spoolTarget, spoolMotor));
        }else{
            spoolMotor.setPower(0.7);
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        /* else {
            spoolMotor.setPower(0.0);
        }
        */
        /*if (gamepad1.dpad_right && spoolMotor.getTargetPosition()<=maxHeight){
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }

         */
        //leftCRServo.setPower(leftPower);
        //rightCRServo.setPower(rightPower);
        leftFrontDrive.setPower(lfPow);
        leftBackDrive.setPower(lbPow);
        rightFrontDrive.setPower(rfPow);
        rightBackDrive.setPower(rbPow);
        telemetry.addLine("Left Bumper to open claw, Right Bumper to close");
        telemetry.addLine("Dpad controls: up moves arm up, down moves arm down");
        telemetry.addLine("A: bottom, B: low, X: medium, Y: high");
        telemetry.addData("target:",spoolMotor.getTargetPosition());
        //telemetry.addLine("left-nothing, right-makes the arm go to position set at half speed");
    }
    @Override
    public void stop(){
    }
}