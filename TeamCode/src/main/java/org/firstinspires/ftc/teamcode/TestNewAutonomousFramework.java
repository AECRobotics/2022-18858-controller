package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyAutonomous;
import org.firstinspires.ftc.teamcode.TeamUtils.UnitConversion;
import org.firstinspires.ftc.teamcode.TeamUtils.Motor.Wheel;

@Autonomous(name="Test New Autonomous Framework", group="Debug")
public class TestNewAutonomousFramework extends MyBoyAutonomous {
    public Wheel motor = null;

    @Override
    public void runOpMode() {
        this.internalInit();

        motor = driveBase.getBl();

        waitForStart();

        this.internalStart();
        //motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motor.setPower(0.2);
        //motor.setTargetPosition(-1000);
        telemetry.addLine("thing-1");
        telemetry.update();
        driveBase.strafe(0.5, 0.66);
        telemetry.addLine("thing0");
        telemetry.update();
        driveBase.forward(0.5, 1.17);
        telemetry.addLine("thing2");
        telemetry.update();
        driveBase.strafe(0.5, -0.3);
/*        long startedTime = System.nanoTime();
        while(startedTime+UnitConversion.SECONDS_PER_NANOSECOND*5 > System.nanoTime()) {
            telemetry.addLine(motor.getTargetPosition() + "");
            telemetry.addLine(motor.getCurrentPosition() + "");
            telemetry.addLine(motor.getMotor().getTargetPosition() + "");
            telemetry.addLine(motor.getMotor().getCurrentPosition() + "");
            telemetry.addLine(motor.internalRealPositionDifference+"");
            telemetry.update();
        }
        motor.setTargetPosition(500);
        startedTime = System.nanoTime();
        while(startedTime+ UnitConversion.SECONDS_PER_NANOSECOND*5 > System.nanoTime()) {
            telemetry.addLine(motor.getTargetPosition() + "");
            telemetry.addLine(motor.getCurrentPosition() + "");
            telemetry.addLine(motor.getMotor().getTargetPosition() + "");
            telemetry.addLine(motor.getMotor().getCurrentPosition() + "");
            telemetry.addLine(motor.internalRealPositionDifference+"");
            telemetry.update();
        }
        motor.setTargetPosition(0);
        startedTime = System.nanoTime();
        while(startedTime+ UnitConversion.SECONDS_PER_NANOSECOND*5 > System.nanoTime()) {
            telemetry.addLine(motor.getTargetPosition() + "");
            telemetry.addLine(motor.getCurrentPosition() + "");
            telemetry.addLine(motor.getMotor().getTargetPosition() + "");
            telemetry.addLine(motor.getMotor().getCurrentPosition() + "");
            telemetry.addLine(motor.internalRealPositionDifference+"");
            telemetry.update();
        }
        motor.setTargetPosition(500);
        startedTime = System.nanoTime();
        while(startedTime+ UnitConversion.SECONDS_PER_NANOSECOND*5 > System.nanoTime()) {
            telemetry.addLine(motor.getTargetPosition() + "");
            telemetry.addLine(motor.getCurrentPosition() + "");
            telemetry.addLine(motor.getMotor().getTargetPosition() + "");
            telemetry.addLine(motor.getMotor().getCurrentPosition() + "");
            telemetry.addLine(motor.internalRealPositionDifference+"");
            telemetry.update();
        }
        /*driveBase.strafe(0.5, 0.5);
        driveBase.forward(0.5, 0.5);
        driveBase.strafe(0.5, -0.5);
        spoolMotor.setRetractedDistance(5.0);
        while(!spoolMotor.reachedTarget()) {
            sleep(2);
        }*/
        while(opModeIsActive()) {}
    }
}
