package org.firstinspires.ftc.teamcode.TrashbinOutsideAnItalianRestaurant;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CompetitionUtils.ConeStateFinder;
import org.firstinspires.ftc.teamcode.CompetitionUtils.JunctionLocatorPipeline;
import org.firstinspires.ftc.teamcode.CompetitionUtils.MyBoyWebcam;

import java.util.HashMap;

@TeleOp(name="Junction Locator Test", group="Debug")
@Disabled
public class JunctionLocatorTest extends OpMode {
    public MyBoyWebcam webcam = null;

    public void init() {
        HashMap<Integer, ConeStateFinder.ConeState> tagToStateMap = new HashMap<>();
        tagToStateMap.put(5, ConeStateFinder.ConeState.LEFT);
        tagToStateMap.put(10, ConeStateFinder.ConeState.MIDDLE);
        tagToStateMap.put(15, ConeStateFinder.ConeState.RIGHT);
        //stateFinder = new ConeStateFinder(aprilWebcam, tagToStateMap);
        this.webcam = new MyBoyWebcam(hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()), hardwareMap.get(WebcamName.class, "webcam"), tagToStateMap);
    }

    public void init_loop() {

    }

    public void start() {
        this.webcam.switchToJunctionLocatorMode();
    }

    public void loop() {
        //telemetry.addLine(JunctionLocatorPipeline.debugOutput);
        telemetry.addLine("angle" + this.webcam.getAngle() + ", " + this.webcam.getWidth());
    }
}
