/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

//basically use this file as a template for autonomous and maybe teleop, only edit it if you are making changes to the template otherwise copy it.

package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Disabled
@TeleOp(name="Teleopsimple", group="Iterative Auto")
public class TeleopSimple extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //r1 probably right front
    //r2 probably right back
    //l1 probably left front
    //l2 probably left back
    protected DcMotor r1 = null;
    protected DcMotor r2 = null;
    protected DcMotor l1 = null;
    protected DcMotor l2 = null;

    @Override
    public void init() {
        r1 = hardwareMap.get(DcMotor.class, "frontright");
        r2 = hardwareMap.get(DcMotor.class, "backright");
        l1 = hardwareMap.get(DcMotor.class, "frontleft");
        l2 = hardwareMap.get(DcMotor.class, "backleft");

        r1.setTargetPosition(0);
        r2.setTargetPosition(0);
        l1.setTargetPosition(0);
        l2.setTargetPosition(0);

        r1.setDirection(DcMotor.Direction.REVERSE);
        r2.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_x;


        double lbPow = drive - strafe + turn;
        double rbPow = drive + strafe - turn;
        double lfPow = drive + strafe + turn;
        double rfPow = drive - strafe - turn;
        double divisor = Math.max(Math.max(lfPow, lbPow), Math.max(rfPow, rbPow));
        if(divisor > 1.0)
        {
            lbPow/=divisor;
            rbPow/=divisor;
            lfPow/=divisor;
            rfPow/=divisor;
        }
        l1.setPower(lfPow);
        l2.setPower(lbPow);
        r1.setPower(rfPow);
        r2.setPower(rbPow);
        //telemetry.addData("debug17", armServo.getPosition());
        //telemetry.addData("debug16", String.format("%d, %d", arm1.getTargetPosition(), arm1.getCurrentPosition()));
        //telemetry.addData("debug15", String.format("%.9f", increment));
        //telemetry.addData("debug17",armServo.getPosition());
        //telemetry.addData("debug14", String.format("%b, %b, %d",gamepad1.b, gamepad1.y, debug1));
        //telemetry.addData("pain", String.format("%d, %d, %d", arm.getCurrentPosition(), arm.getTargetPosition(), armTarget));
        //telemetry.addData("Status", String.format("Average Execution Time: %.9fms", averageLoopTime/(secInNanoSecs/1000)));
        //saveGamepadState();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        r1.setPower(0.0);
        r2.setPower(0.0);
        l1.setPower(0.0);
        l2.setPower(0.0);
    }
}
