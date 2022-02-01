package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.sql.Time;


@Autonomous(name = "RedWarehouse", group =  "Auto")
public class RedWarehouse extends LinearOpMode {

    // Creating Instances of Each Subsystem of the Robot

    @Override
    public void runOpMode() throws InterruptedException{
        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);


       /* ObjectDetector detector = new ObjectDetector(telemetry);
        detector.logiCam = hardwareMap.get(WebcamName.class, "logiCam");
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        detector.camera = OpenCvCameraFactory.getInstance().createWebcam(detector.logiCam);
        detector.camera.setPipeline(detector);
        detector.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            //camera.pauseViewport() and webcam.resumeViewport()

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
        driveTrain.frontTargetPosition = 390;
        driveTrain.backTargetPosition = 390;

        //detector.camera.stopStreaming();
        // run until the end of the match (driver presses STOP)

       // driveTrain.move("side", 1240);
        /*switch (detector.getLocation()) {
            case LEFT:
                objectGrab.BlockPlace(1500, -1, .15);
                objectGrab.Retract(1500, 1, .5);
                break;
            case RIGHT:
                objectGrab.BlockPlace(0, 0, .15);
                objectGrab.Retract(0, 0, .5);
                break;
            case MIDDLE:
                objectGrab.BlockPlace(2000, 1, .15);
                objectGrab.Retract(2000, -1, .5);
                break;
        }

         */
        //driveTrain.move("side", -1260);
        driveTrain.move("straight", 3000);
        driveTrain.frontTargetPosition = 0;
        driveTrain.backTargetPosition = 0;
        driveTrain.frontTwist.setTargetPosition(driveTrain.frontTargetPosition);
        driveTrain.backTwist.setTargetPosition(driveTrain.backTargetPosition);
        driveTrain.frontTwist.setMode(RUN_TO_POSITION);
        driveTrain.backTwist.setMode(RUN_TO_POSITION);
        driveTrain.holdPosition();
        while(driveTrain.backTwist.isBusy() || driveTrain.frontTwist.isBusy()){};
        //driveTrain.move("side", 1500);
       // driveTrain.move("rotate", 30);

        //driveTrain.threadRun = false;
        //ARM CODE HERE
        /*objectGrab.turnServo.setPower(.1);
        sleep(3000);
        objectGrab.turnServo.setPower(0.01);
        objectGrab.duck.setPower(1);
        sleep(5000);
        objectGrab.turnServo.setPower(-.1);
        sleep(1800);
        objectGrab.turnServo.setPower(0);
        driveTrain.move("side", -1000);*/

    }
}