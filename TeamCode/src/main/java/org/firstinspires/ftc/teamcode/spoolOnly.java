package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="spool ONLY", group="Robot")
@Disabled
public class spoolOnly extends OpMode{
    public DcMotor spoolMotor = null;
    public void init() {
        spoolMotor = hardwareMap.get(DcMotor.class, "spoolmotor");
        spoolMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        telemetry.addData("Status", "Ready to run");
        telemetry.update();
    }
    public void init_loop() {

    }
    public void start() {

    }
    public void loop() {
        if(gamepad1.dpad_up) {
            spoolMotor.setPower(0.3);
        } else if(gamepad1.dpad_down) {
            spoolMotor.setPower(-0.3);
        } else {
            spoolMotor.setPower(0.0);
        }
    }
    public void stop() {

    }
}
