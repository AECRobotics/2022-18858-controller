package org.firstinspires.ftc.teamcode.TeamUtils.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.TeamUtils.DriveBase.HolonomicDriveBase;

public abstract class HolonomicAutonomous extends LinearOpMode {
    protected HolonomicDriveBase driveBase;
    protected abstract void internalInit();
    protected abstract void internalStart();

    public HolonomicAutonomous() {}
}
