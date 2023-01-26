package org.firstinspires.ftc.teamcode.CurrentRubySofeeCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="GoBilda Arm Check", group="Robot")

public class SpoolMotorCheckGoBilda extends OpMode{
    public DcMotor spoolMotor = null;
    static final double PULSES_PER_REVOLUTION = ((((1+(46.0/17.0))) * (1+(46.0/17.0))) * 28);
    //((((1+(46/17))) * (1+(46/17))) * 28) PULSES PER REV EQUATION
    //(((1+(46/17))) * (1+(46/17))) GEAR RATIO EQUATION
    //static final double SPOOL_DIAMETER = 112/Math.PI; //mm NEED NEW
    static final double SPOOL_CIRCUMFERENCE = 112; //mm
    static final double COUNTS_PER_MM = (PULSES_PER_REVOLUTION)/(SPOOL_CIRCUMFERENCE); //HOW MUCH MM PER TICK
    //static final double SPOOL_TICKS = COUNTS_PER_MM * SPOOL_CIRCUMFERENCE;
    public double mmtoTicks(double mm){
        return mm*COUNTS_PER_MM;
    }
    double spoolTarget;
    double maxTicks = 976.0/112.0;

    public double ticksToMM(int ticks) {
        return ((double)ticks)/COUNTS_PER_MM;
    }

    public void init() {
        spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotorgobilda");
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        spoolMotor.setTargetPosition(0);
        spoolMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
    }
    public void init_loop() {

    }

    public void start() {

    }

    public void loop() {
        if(gamepad1.a){
            //bottom
            spoolTarget = 0; //mm
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.75);
        }
        if(gamepad1.b){
            // low
            spoolTarget = 370; //mm
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(1.0);
        }
        if(gamepad1.x){
            //medium
            spoolTarget  = 580; //mm
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.9);
        }
        if(gamepad1.y){
            //high
            spoolTarget = 790; //mm
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget)); //sets new target pos to height (mm) in ticks
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            spoolMotor.setPower(0.8);
        }
        /*if(gamepad1.dpad_up) {
            spoolMotor.setPower(0.3);
        } else if(gamepad1.dpad_down) {
            spoolMotor.setPower(-0.3);
        } else {
            spoolMotor.setPower(0.0);
        }
         */
        if(gamepad1.dpad_up) {
            spoolMotor.setPower(0.75);
            spoolTarget=ticksToMM(spoolMotor.getCurrentPosition())+10;
            //spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget));
        } else if(gamepad1.dpad_down) {
            spoolMotor.setPower(0.75);
            spoolTarget=ticksToMM(spoolMotor.getCurrentPosition())-10;
            //spoolMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spoolMotor.setTargetPosition((int)mmtoTicks(spoolTarget));
        }else{
            spoolMotor.setPower(0.7);
            spoolMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }
    public void stop() {

    }
}
