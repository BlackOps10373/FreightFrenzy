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

    @Override
    public void runOpMode() throws InterruptedException{

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);





/*
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

 */
        //driveTrain.start();
        waitForStart();
        driveTrain.frontTargetPosition = 390;
        driveTrain.backTargetPosition = 390;
        //driveTrain.holdPosition();

            //detector.camera.stopStreaming();
        // run until the end of the match (driver presses STOP)

        //driveTrain.move("side", 1240);
        /*
        switch (detector.getLocation()) {
            case LEFT:
                //objectGrab.BlockPlace(1500, -1, -1);
                break;
            case RIGHT:
                //objectGrab.BlockPlace(0, 0, -1);
                break;
            case MIDDLE:
                //objectGrab.BlockPlace(2000, 1, -1);
                break;
        }
        */

        /*
        driveTrain.move("side", -1260);
        driveTrain.move("straight", 5000);
        driveTrain.move("side", 1500);
        driveTrain.move("rotate", 30);
         */

        /*
        driveTrain.moveAmount(0, 1, 0, 1000);
        driveTrain.moveAmount(1, 0, 0, 1000);
*/

        File recordFile = AppUtil.getInstance().getSettingsFile("RedDuck.txt");

        int totalDist = 0;

        int i = 0;
        char[] array = ReadWriteFile.readFile(recordFile).toCharArray();
        while(array[i] != 0 && opModeIsActive())
        {
            int neg;
            if(((int)(array[i]) & 0b1000000000000000) > 0)
            {
                neg = -1;
                array[i] &= 0b0111111111111111;
            }else
                neg = 1;
            int temp = ((int)((((int)(array[i])) << 16) | (((int)(array[i+1])))));
            float tLSX = Float.intBitsToFloat(temp) * neg;



            if(((int)(array[i+2]) & 0b1000000000000000) > 0)
            {
                neg = -1;
                array[i+2] &= 0b0111111111111111;
            }else
                neg = 1;

            temp = ((int)((((int)(array[i+2])) << 16) | (((int)(array[i+3])))));

            float tLSY = Float.intBitsToFloat(temp) * neg;


            if(((int)(array[i+4]) & 0b1000000000000000) > 0)
            {
                neg = -1;
                array[i+4] &= 0b0111111111111111;
            }else
                neg = 1;

            temp = ((int)((((int)(array[i+4])) << 16) | (((int)(array[i+5])))));

            float tRSX = Float.intBitsToFloat(temp) * neg;



            if(((int)(array[i+6]) & 0b1000000000000000) > 0)
            {
                neg = -1;
                array[i+6] &= 0b0111111111111111;
            }else
                neg = 1;

            temp = ((int)((((int)(array[i+6])) << 16) | (((int)(array[i+7])))));

            float tRSY = Float.intBitsToFloat(temp) * neg;
/*
                float tLSX = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+1])))));

                float tLSY = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+2])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+3])))));

                float tRSX = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+4])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+5])))));

                float tRSY = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+6])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+7])))));
*/
            int dist = (((int)array[i+8]) << 16) | ((int)array[i+9]);

            char trackedWheel = array[i+10];
            telemetry.addData("LeftX", tLSX);
            telemetry.addData("LeftY", tLSY);
            telemetry.addData("RightX", tRSX);
            telemetry.addData("RightY", tRSY);
            telemetry.addData("TrackedWheel", trackedWheel);
            telemetry.addData("dist", dist);
            telemetry.update();
            totalDist += dist;

            i += 11;

            switch (trackedWheel)
            {
                case 0:
                    while(totalDist < driveTrain.lw.getCurrentPosition())
                    {
                        driveTrain.move(tLSY, tLSX, 0);
                    }
                    break;
                case 1:
                    while(totalDist < driveTrain.rw.getCurrentPosition())
                    {
                        driveTrain.move(tLSY, tLSX, 0);
                    }
                    break;
                case 2:
                    while(totalDist < driveTrain.blw.getCurrentPosition())
                    {
                        driveTrain.move(tLSY, tLSX, 0);
                    }
                    break;
                case 3:
                    while(totalDist < driveTrain.brw.getCurrentPosition())
                    {
                        driveTrain.move(tLSY, tLSX, 0);
                    }
            }
        }


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