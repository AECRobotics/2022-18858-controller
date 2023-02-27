package org.firstinspires.ftc.teamcode.CurrentRubySofeeCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CompetitionUtils.ClawPositions;

@TeleOp(name="messy new claw", group="Robot")
public class NewClawMessy extends OpMode {
    CRServo leftCRServo;
    CRServo rightCRServo;
    double leftPower;
    double rightPower;
    boolean clawOpen;
    Servo rightClaw = null;
    Servo leftClaw = null;
    public void init(){
        leftCRServo = hardwareMap.crservo.get("leftCRservo");
        rightCRServo = hardwareMap.crservo.get("rightCRservo");
        rightClaw = hardwareMap.get(Servo.class, "rightclaw");
        leftClaw = hardwareMap.get(Servo.class, "leftclaw");
    }
    public void init_loop(){

    }
    public void start(){

    }
    public void loop(){
        if(gamepad1.dpad_left){
            leftPower = 0.6;
            rightPower = -0.6;
        }
        else{
            leftPower = 0.0;
            rightPower = 0.0;
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

        leftCRServo.setPower(leftPower);
        rightCRServo.setPower(rightPower);
    }
    public void stop(){

    }

}
