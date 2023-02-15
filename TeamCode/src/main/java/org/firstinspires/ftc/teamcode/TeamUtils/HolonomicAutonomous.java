package org.firstinspires.ftc.teamcode.TeamUtils;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class HolonomicAutonomous extends LinearOpMode {
    protected HolonomicDriveBase driveBase;
    protected abstract void internalInit();
    protected abstract void internalStart();

    public HolonomicAutonomous() {}
}
