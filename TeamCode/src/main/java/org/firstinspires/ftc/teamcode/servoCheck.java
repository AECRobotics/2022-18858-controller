package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name="find direction", group = "Robot")
public class servoCheck extends OpMode{
    public Servo rightClaw = null; //closed +0.36 open +0.45
    public Servo leftClaw = null; //open and closed because weird crap +1.0

    double position1 = 0.0;
    double position2 = 0.0;

    boolean lastGamepadA = false;
    boolean lastGamepadY = false;
    boolean lastGamepadDpadUp = false;
    boolean lastGamepadDpadDown = false;
    boolean lastGamepadX = false;
    boolean lastGamepadB = false;
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
        if(gamepad1.y && !lastGamepadY) {
            position2+=0.01;
        }
        if(gamepad1.a && !lastGamepadA) {
            position2-=0.01;
        }
        if(gamepad1.dpad_up && !lastGamepadDpadUp) {
            position1+=0.01;
        }
        if(gamepad1.dpad_down && !lastGamepadDpadDown) {
            position1-=0.01;
        }
        if(gamepad1.x && !lastGamepadX) {
            leftClaw.setPosition(position1);
        }
        if(gamepad1.b && !lastGamepadB) {
            rightClaw.setPosition(position2);
        }

        lastGamepadA = gamepad1.a;
        lastGamepadY = gamepad1.y;
        lastGamepadDpadUp = gamepad1.dpad_up;
        lastGamepadDpadDown = gamepad1.dpad_down;
        lastGamepadX = gamepad1.x;
        lastGamepadB = gamepad1.b;
        telemetry.addData("thing",String.format("left: %.2f, right: %.2f", position1, position2));
        telemetry.addData("right:", rightClaw.getPosition());
        telemetry.addData("left", leftClaw.getPosition());
    }

    public void stop(){

    }
}
