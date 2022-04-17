package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import java.io.File;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.io.File; // File test
import java.io.IOException;
import java.util.Locale;

@Autonomous(name = "RedDuck", group =  "Auto")
public class RedDuck extends LinearOpMode {

    // Creating Instances of Each Subsystem of the Robot

    public ObjectGrab objectGrab;

    @Override
    public void runOpMode() throws InterruptedException{

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        objectGrab = new ObjectGrab(telemetry, hardwareMap);






        //ObjectDetector detector = new ObjectDetector(telemetry);
        //detector.logiCam = hardwareMap.get(WebcamName.class, "logiCam");
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        //detector.camera = OpenCvCameraFactory.getInstance().createWebcam(detector.logiCam);
        //detector.camera.setPipeline(detector);
        //detector.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
        //camera.pauseViewport() and webcam.resumeViewport()

        /*
            @Override
            public void onOpened() {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                detector.camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {

            }
        });
*/
        //driveTrain.start();
        waitForStart();

        //objectGrab.armMovement(0, 300);

        driveTrain.setEveryMove(1, 1, 1, 1000, driveTrain.rw, this, 5250, 5100, objectGrab);

        driveTrain.targetDegree += 180;
        while(driveTrain.getHeading() != driveTrain.targetDegree && opModeIsActive()) {
            objectGrab.armMovementCheck();
            driveTrain.resetPowers();
            driveTrain.gyroStraight();
            driveTrain.lw.setPower(driveTrain.lwPower);
            driveTrain.rw.setPower(driveTrain.rwPower);
            driveTrain.blw.setPower(driveTrain.blwPower);
            driveTrain.brw.setPower(driveTrain.brwPower);
            telemetry.addData("ticks", driveTrain.rw.getCurrentPosition());
            telemetry.addData("TargetDegree", driveTrain.targetDegree);
            telemetry.update();
        }

        while (opModeIsActive()) {
            objectGrab.armMovementCheck();
            telemetry.addData("Rotation", objectGrab.rotateTargetPosition);
            telemetry.addData("Istrying", objectGrab.useSafeRotate);
            telemetry.update();
        }


        //driveTrain.moveAmount(1, -1, .15, 700, driveTrain.rw, this);

    }
}