package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "RedWarehouse", group =  "Auto")
public class RedWarehouse extends LinearOpMode {

    // Creating Instances of Each Subsystem of the Robot

    @Override
    public void runOpMode() throws InterruptedException{

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);

        //objectGrab.turnServo = hardwareMap.servo.get("turnServo");


        /*ObjectDetector detector = new ObjectDetector(telemetry);
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
        });*/
        waitForStart();
        while(driveTrain.rw.getCurrentPosition() > -400) {
            driveTrain.move(1, 0, 0);
            telemetry.addData("rw", driveTrain.rw.getCurrentPosition());
            telemetry.update();
        }
        driveTrain.targetDegree += 180;
        while(!(driveTrain.getHeading() < driveTrain.targetDegree + 2 && driveTrain.getHeading() > driveTrain.targetDegree - 2)) {
            objectGrab.armTargetPosition = 5400;
            objectGrab.armMovement(0, 0);
            driveTrain.move(0, 0, .75);
            if(objectGrab.upDownMotor.getCurrentPosition() > 1400)
                objectGrab.rotateTargetPosition = 5000;
        }
        while(objectGrab.rotate.getCurrentPosition() != objectGrab.rotateTargetPosition){
            objectGrab.armMovement(0,0);
            driveTrain.move(0,0,0);
        }
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
        //detector.camera.stopStreaming();

        // run until the end of the match (driver presses STOP)
        //driveTrain.move("side", 1500);
        //driveTrain.move("rotate", 30);
    }
}