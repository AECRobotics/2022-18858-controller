package org.firstinspires.ftc.teamcode.CompetitionUtils;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawController {
    private Servo rightServo;
    private Servo leftServo;

    //private CRServo rightCRServo;
    //private CRServo leftCRServo;

    public ClawController(HardwareMap hardwareMap) {
        rightServo = hardwareMap.get(Servo.class, "rightclaw");
        leftServo = hardwareMap.get(Servo.class, "leftclaw");
        //rightCRServo = hardwareMap.get(CRServo.class, "rightCRClaw");
        //leftCRServo = hardwareMap.get(CRServo.class, "leftCRClaw");
        //rightCRServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void open() {
        leftServo.setPosition(ClawPositions.leftServoOpen);
        rightServo.setPosition(ClawPositions.rightServoOpen);
    }

    public void close() {
        leftServo.setPosition(ClawPositions.leftServoClosed);
        rightServo.setPosition(ClawPositions.rightServoClosed);
    }

    /*public void intakeIn() {
        rightCRServo.setPower(0.6);
        leftCRServo.setPower(0.6);
    }

    public void intakeOut() {
        rightCRServo.setPower(-0.6);
        leftCRServo.setPower(-0.6);
    }*/
}
