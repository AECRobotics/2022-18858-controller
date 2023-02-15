package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.Wheel;

public class TestNewAutonomousFramework extends MyBoyAutonomous {
    public Wheel motor;

    public void externalInit() {
        motor = driveBase.getBl();
    }

    public void externalStart() {
        motor.setTargetPosition(1000);
        telemetry.addLine(motor.getMotor().getTargetPosition() + "");
        telemetry.update();
        sleep(5000);
        motor.setTargetPosition(0);
        telemetry.addLine(motor.getMotor().getTargetPosition() + "");
        telemetry.update();
        /*driveBase.strafe(0.5, 0.5);
        driveBase.forward(0.5, 0.5);
        driveBase.strafe(0.5, -0.5);
        spoolMotor.setRetractedDistance(5.0);
        while(!spoolMotor.reachedTarget()) {
            sleep(2);
        }*/
    }
}
