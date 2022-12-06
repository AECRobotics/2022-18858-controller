package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="get direction", group = "Robot")
public class servoCheck extends OpMode{
    public Servo rightClaw = null;
    public Servo leftClaw = null;
    public void init() {
        telemetry.addData("Status", "Initialized");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
    }
    public void init_loop(){

    }
    public void start(){

    }
    public void loop(){
        telemetry.addData("right:", rightClaw.getDirection());
        telemetry.addData("left", leftClaw.getDirection());
    }
    public void stop(){

    }
}
