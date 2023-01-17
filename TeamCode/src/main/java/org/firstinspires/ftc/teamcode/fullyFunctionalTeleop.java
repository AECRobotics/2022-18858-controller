package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;

import java.util.HashMap;


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
    static final double SPOOL_DIAMETER = 23.0; //mm
    static final double SPOOL_CIRCUMFERENCE = Math.PI * (SPOOL_DIAMETER);
    static final double COUNTS_PER_MM = (COUNTS_PER_MOTOR_REV)/(SPOOL_CIRCUMFERENCE); //HOW MUCH MM PER TICK
    //static final double SPOOL_TICKS = COUNTS_PER_MM * SPOOL_CIRCUMFERENCE;
    int maxHeight = (int)mmtoTicks(1300);

    boolean lastGamepadDpadRight = false;
    boolean lastGamepadDpadLeft = false;
    boolean lastGamepadDpadUp = false;
    boolean lastGamepadDpadDown = false;
//current target pos / spoolticks
    public double mmtoTicks(double mm){
        return mm*COUNTS_PER_MM;
    }

    enum ArmPosition {
        GROUND,
        HOLDING,
        LOW,
        MIDDLE,
        HIGH
    }

    public BackupFullTeleop.ArmPosition levelToArmPos(int level) {
        switch (level) {
            case 1:
                return BackupFullTeleop.ArmPosition.GROUND;
            case 2:
                return BackupFullTeleop.ArmPosition.HOLDING;
            case 3:
                return BackupFullTeleop.ArmPosition.LOW;
            case 4:
                return BackupFullTeleop.ArmPosition.MIDDLE;
            case 5:
                return BackupFullTeleop.ArmPosition.HIGH;
            default:
                return BackupFullTeleop.ArmPosition.GROUND;
        }
    }

    public void setSpoolPosition() {
        spoolMotor.setTargetPosition(Math.min((int)mmtoTicks(armPositionHeights.get(levelToArmPos(spoolLevel))), maxHeight));
    }

    public HashMap<BackupFullTeleop.ArmPosition, Double> armPositionHeights = new HashMap<BackupFullTeleop.ArmPosition, Double>();
    int spoolLevel = 1;

    @Override
    public void init() {
        armPositionHeights.put(BackupFullTeleop.ArmPosition.GROUND, 0.0);
        armPositionHeights.put(BackupFullTeleop.ArmPosition.HOLDING, 100.0);
        armPositionHeights.put(BackupFullTeleop.ArmPosition.LOW, 370.0); //low junction is at 343 mm
        armPositionHeights.put(BackupFullTeleop.ArmPosition.MIDDLE, 470.0); //middle junction is at 597 mm
        armPositionHeights.put(BackupFullTeleop.ArmPosition.HIGH, 850.0); // high junction is at 851 mm

        telemetry.addData("Status", "Initialized");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backleft");
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontleft");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backright");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontright");
        spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotor");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
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
        telemetry.addData("left stick", gamepad1.left_stick_y);

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
            leftClaw.setPosition(ClawPositions.leftServoOpen);
            rightClaw.setPosition(ClawPositions.rightServoOpen);
        } else {
            leftClaw.setPosition(ClawPositions.leftServoClosed);
            rightClaw.setPosition(ClawPositions.rightServoClosed);
        }



        /*if(gamepad1.dpad_up && !lastGamepadDpadUp) {
            if(spoolLevel < 5) {
                spoolLevel++;
                setSpoolPosition();
            }
        }
        if(gamepad1.dpad_down && !lastGamepadDpadDown) {
            if(spoolLevel > 1) {
                spoolLevel--;
                setSpoolPosition();
            }
        }*/
        if(gamepad1.a) {
            spoolLevel=1;
            setSpoolPosition();
        }
        if(gamepad1.b) {
            spoolLevel=3;
            setSpoolPosition();
        }
        if(gamepad1.x) {
            spoolLevel=4;
            setSpoolPosition();
        }
        if(gamepad1.y) {
            spoolLevel=5;
            setSpoolPosition();
        }
        if(gamepad1.dpad_down && !lastGamepadDpadDown) {
            armPositionHeights.put(levelToArmPos(spoolLevel), armPositionHeights.get(levelToArmPos(spoolLevel))-10.0);
            setSpoolPosition();
        }
        if(gamepad1.dpad_up && !lastGamepadDpadUp) {
            armPositionHeights.put(levelToArmPos(spoolLevel), armPositionHeights.get(levelToArmPos(spoolLevel))+10.0);
            setSpoolPosition();
        }
        lastGamepadDpadDown = gamepad1.dpad_down;
        lastGamepadDpadUp = gamepad1.dpad_up;
        lastGamepadDpadLeft = gamepad1.dpad_left;
        lastGamepadDpadRight = gamepad1.dpad_right;
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
        /*if (gamepad1.dpad_right && spoolMotor.getTargetPosition()<=maxHeight){
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.5);
        }

         */

        leftFrontDrive.setPower(lfPow);
        leftBackDrive.setPower(lbPow);
        rightFrontDrive.setPower(-rfPow);
        rightBackDrive.setPower(-rbPow);
        telemetry.addLine("Left Bumper to open claw, Right Bumper to close");
        telemetry.addLine("Dpad controls: up-adjust target pos inc, down-adjust target pos dec");
        //telemetry.addData("target:",spoolMotor.getTargetPosition());
        telemetry.addLine("Position:" + levelToArmPos(spoolLevel).name());
        //telemetry.addLine("left-nothing, right-makes the arm go to position set at half speed");
    }
    @Override
    public void stop(){
    }
}