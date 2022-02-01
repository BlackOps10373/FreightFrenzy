package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "BlueDuck", group =  "Auto")
public class BlueDuck extends LinearOpMode {

    // Creating Instances of Each Subsystem of the Robot

    @Override
    public void runOpMode() throws InterruptedException{

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);

        driveTrain.lw = hardwareMap.dcMotor.get("lw");
        driveTrain.blw = hardwareMap.dcMotor.get("blw");
        driveTrain.rw = hardwareMap.dcMotor.get("rw");
        driveTrain.brw = hardwareMap.dcMotor.get("brw");

        objectGrab.turnServo = hardwareMap.servo.get("turnServo");
        objectGrab.upDownServo = hardwareMap.crservo.get("upDownServo");
        objectGrab.fingerServo = hardwareMap.crservo.get("fingerServo");
        objectGrab.duck = hardwareMap.crservo.get("duck");
        objectGrab.extend = hardwareMap.dcMotor.get("extend");
        objectGrab.extend.setMode(RUN_WITHOUT_ENCODER);


        ObjectDetector detector = new ObjectDetector(telemetry);
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
        waitForStart();

       /*switch (detector.getLocation()) {
            case LEFT:
                // do stuff
                break;
            case RIGHT:
                // do other stuff
                break;
            case MIDDLE:
                // also do stuff
                break;
        }*/
        detector.camera.stopStreaming();
        // run until the end of the match (driver presses STOP)

        driveTrain.move("side", 1240);
        driveTrain.move("side", -1260);
        driveTrain.move("straight", 5000);
        driveTrain.move("side", 1500);
        driveTrain.move("rotate", 30);
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