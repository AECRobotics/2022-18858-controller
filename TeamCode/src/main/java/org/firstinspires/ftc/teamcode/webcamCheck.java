package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.TeamUtils.RobotWebcam;

@TeleOp(name="test webcam cone state finder", group = "Robot")
public class webcamCheck extends OpMode{
    boolean lastGamepadB = false;
    ConeStateFinder.ConeState state = null;
    RobotWebcam webcam = null;
    long time = 0;
    public void init() {
        telemetry.addData("Status", "Initialized");
        WebcamName webcamn = hardwareMap.get(WebcamName.class, "webcam");
        webcam = new RobotWebcam(webcamn);
    }
    public void init_loop(){

    }
    public void start(){

    }
    public void loop(){
        if(gamepad1.b && ! lastGamepadB) {
            long thing = System.nanoTime();
            state = ConeStateFinder.getConeState(webcam);
            time = System.nanoTime()-thing;
        }

        lastGamepadB = gamepad1.b;
        telemetry.addLine(state != null ? state.name() : "null");
        telemetry.addLine("took " + (time/1000000) + " ms");
    }

    public void stop(){

    }
}
