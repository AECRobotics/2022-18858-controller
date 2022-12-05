package org.firstinspires.ftc.teamcode.TeamUtils;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import android.graphics.Bitmap;

import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.CompetitionUtils.VuforiaConstants;

import java.util.ArrayList;
import java.util.List;

public class RobotWebcam {
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables targets;
    VuforiaLocalizer vuforia;
    private WebcamName webcamName;
    public RobotWebcam(WebcamName webcamName) {
        this.webcamName = webcamName;
    }

    private void initializeVuforia() {
        this.parameters = new VuforiaLocalizer.Parameters();
        this.parameters.vuforiaLicenseKey = VuforiaConstants.LicenseKey;
        this.parameters.cameraName = this.webcamName;
        this.parameters.useExtendedTracking = false;
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
        /*this.targets = this.vuforia.loadTrackablesFromAsset("PowerPlay");
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targets);
        identifyTarget(0, "Red Audience Wall",   -halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0,  90);
        identifyTarget(1, "Red Rear Wall",        halfField,  -oneAndHalfTile, mmTargetHeight, 90, 0, -90);
        identifyTarget(2, "Blue Audience Wall",  -halfField,   oneAndHalfTile, mmTargetHeight, 90, 0,  90);
        identifyTarget(3, "Blue Rear Wall",       halfField,   oneAndHalfTile, mmTargetHeight, 90, 0, -90);

        final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f * (float)UnitConversion.MMPERINCH;   // eg: Enter the forward distance from the center of the robot to the camera lens
        final float CAMERA_VERTICAL_DISPLACEMENT = 6.0f * (float)UnitConversion.MMPERINCH;   // eg: Camera is 6 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT     = 0.0f * (float)UnitConversion.MMPERINCH;   // eg: Enter the left distance from the center of the robot to the camera lens

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }*/
    }

    public Bitmap getWebcamFrame() {
        VuforiaLocalizer.CloseableFrame frame;
        try {
            frame = this.vuforia.getFrameQueue().take();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        Image rgb = null;
        long numImages = frame.getNumImages();
        for (int i = 0; i < numImages; i++) {
            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                rgb = frame.getImage(i);
                break;
            }
        }
        if(rgb == null) {
            return null;
        }

        /*rgb is now the Image object that weve used in the video*/
        Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(rgb.getPixels());

        frame.close();

        return bm;

        //put the image into a MAT for OpenCV
        /*Mat tmp = new Mat(rgb.getWidth(), rgb.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bm, tmp);*/

        //close the frame, prevents memory leaks and crashing
    }
}
