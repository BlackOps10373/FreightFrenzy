package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


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
        ElapsedTime elapsedTime = new ElapsedTime();

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

        // Initialize arm off ground
        objectGrab.safeArmMovement(200, 0);
        objectGrab.armMovementCheck();
        waitForStart();

        // Move diagonal towards wobblegoal while lifting arm behind and turning to have the back facing the goal
        driveTrain.setEveryMove(-Math.cos(45), Math.sin(45), 0.5, (750 * 2) + 50, driveTrain.rw, this, 5400, 5100, objectGrab);
        driveTrain.turnMotorsToZero();

        driveTrain.targetDegree += (180 - 30);
        while(((driveTrain.getHeading() - driveTrain.targetDegree) > driveTrain.windowSize || driveTrain.degreeCalc(driveTrain.getHeading() - driveTrain.targetDegree) < 360 - driveTrain.windowSize) && opModeIsActive()) {
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
        driveTrain.resetPowers();
        driveTrain.turnMotorsToZero();
        // finish moving arm
        while (objectGrab.upDownMotor.isBusy() || objectGrab.rotate.isBusy()) {
            objectGrab.armMovementCheck();
        }

        // Drop block
        objectGrab.rightGrab.setPower(-1);
        objectGrab.leftGrab.setPower(1);

        // wait 2 seconds
        double time = elapsedTime.time() + 2;
        while (elapsedTime.time() < time) {
            objectGrab.armMovementCheck();
        }

        // get ready to grab block
        objectGrab.rightGrab.setPower(1);
        objectGrab.leftGrab.setPower(-1);

        // move back at same time as block drop
        driveTrain.setEveryMove(Math.cos(45) * 0.8, -Math.sin(45) * 0.8, 0, 100, driveTrain.rw, this, 5500, 5100, objectGrab);
        driveTrain.turnMotorsToZero();

        // lower arm to front position to pick up blocks while rotating to be against the wall and moving to the wall
        driveTrain.setEveryMove(Math.cos(45), -Math.sin(45), -0.5, 200, driveTrain.rw, this, 200, 0, objectGrab);
        driveTrain.turnMotorsToZero();

        driveTrain.targetDegree = 90;
        while(((driveTrain.getHeading() - driveTrain.targetDegree) > driveTrain.windowSize || driveTrain.degreeCalc(driveTrain.getHeading() - driveTrain.targetDegree) < 360 - driveTrain.windowSize) && opModeIsActive()) {
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
        driveTrain.resetPowers();
        driveTrain.turnMotorsToZero();

        // finish going to wall
        driveTrain.setEveryMove(0, -1, 0, 300, driveTrain.rw, this, 200, 0, objectGrab);
        driveTrain.turnMotorsToZero();
        // go into warehouse
        driveTrain.setEveryMove(0.6, 0, 0, 550, driveTrain.rw, this, 200, 0, objectGrab);
        driveTrain.turnMotorsToZero();
        // wait for block to be grabbed
        while(!(objectGrab.touchSensor.isPressed()) && opModeIsActive()) {
            objectGrab.armMovementCheck();
            driveTrain.setEveryMove(0.6, 0, 0, 550, driveTrain.rw, this, 200, 0, objectGrab);
        }
        driveTrain.turnMotorsToZero();
        objectGrab.leftGrab.setPower(-0.5);
        objectGrab.rightGrab.setPower(0.01);
        // when block is grabbed, back up
        driveTrain.setEveryMove(-1, -0.4, 0, 550, driveTrain.rw, this, 10, 0, objectGrab);
        driveTrain.turnMotorsToZero();



        // end and reset everything
        objectGrab.rightGrab.setPower(0);
        objectGrab.leftGrab.setPower(0);

        objectGrab.safeArmMovement(200, 0);

        while (opModeIsActive()) {
            objectGrab.armMovementCheck();
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