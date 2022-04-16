
package org.firstinspires.ftc.teamcode;

import android.app.ApplicationErrorReport;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;


@TeleOp(name = "RecordforAuto", group = "TeleOp")
public class RecordforAuto extends LinearOpMode {

    @Override
    public void runOpMode() {

        File recordFile = AppUtil.getInstance().getSettingsFile("RedDuck.txt");
        char[] fileExport = new char[200000];
        fileExport[2000 - 1] = 0;
        int i = 0;

        int x = 0;

        int step = 0;

        int stepsStored = 0;

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);
        double powerchange = -0.5;

        boolean turnGripped = false;
        double rAngleDiff = 0;
        int armTargetPosition = 0;
        boolean useSafeArmTarget = false;
        int safeArmTargetPosition = 0;
        int rotateTargetPosition = 0;
        boolean useSafeRotate = false;
        int safeRotateTargetPosition = 0;
        int inOutTargetDegree = 0;

        boolean simulateTouchSensor = false;

        boolean pressedup = false;
        boolean pressedright = false;

        float prevLSX = 0;
        float prevLSY = 0;
        float prevRSX = 0;
        float prevRSY = 0;

        int prevDist = 0;

        boolean firstLoop = true;
        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {
            if (!firstLoop)
            {
                if(prevLSX != gamepad1.left_stick_x || prevLSY != gamepad1.left_stick_y || prevRSX != gamepad1.right_stick_x || prevRSY != gamepad1.right_stick_y)
                {
                    int temp = Float.floatToIntBits(Math.abs(gamepad1.left_stick_x));
                    int firstTemp = temp;
                    if(gamepad1.left_stick_x < 0)
                    {
                        firstTemp = ~temp;
                    }

                    fileExport[i] = (char)(firstTemp >> 16);
                    i++;
                    fileExport[i] = (char)(Math.abs(temp) & 0x0000ffff);
                    i++;

                    temp = Float.floatToIntBits(Math.abs(gamepad1.left_stick_y));
                    firstTemp = temp;
                    if(gamepad1.left_stick_y < 0)
                    {
                        firstTemp = ~temp;
                    }

                    fileExport[i] = (char)(firstTemp >> 16);
                    i++;
                    fileExport[i] = (char)(Math.abs(temp) & 0x0000ffff);
                    i++;
                    temp = Float.floatToIntBits(Math.abs(gamepad1.right_stick_x));
                    firstTemp = temp;
                    if(gamepad1.right_stick_x < 0)
                    {
                        firstTemp = ~temp;
                    }

                    fileExport[i] = (char)(firstTemp >> 16);
                    i++;
                    fileExport[i] = (char)(Math.abs(temp) & 0x0000ffff);
                    i++;
                    temp = Float.floatToIntBits(Math.abs(gamepad1.right_stick_y));
                    firstTemp = temp;
                    if(gamepad1.right_stick_y < 0)
                    {
                        firstTemp = ~temp;
                    }

                    fileExport[i] = (char)(firstTemp >> 16);
                    i++;
                    fileExport[i] = (char)(Math.abs(temp) & 0x0000ffff);
                    i++;


                    char wheelId = 0;
                    int comp = Math.max(Math.max(driveTrain.lw.getCurrentPosition(), driveTrain.rw.getCurrentPosition()), Math.max(driveTrain.blw.getCurrentPosition(), driveTrain.brw.getCurrentPosition()));

                    if(comp == driveTrain.lw.getCurrentPosition())
                        wheelId = 0;
                    if(comp == driveTrain.rw.getCurrentPosition())
                        wheelId = 1;
                    if(comp == driveTrain.blw.getCurrentPosition())
                        wheelId = 2;
                    if(comp == driveTrain.brw.getCurrentPosition())
                        wheelId = 3;

                    int dist = 0;

                    switch (wheelId)
                    {
                        case 0:
                            dist = Math.abs(prevDist - driveTrain.lw.getCurrentPosition());
                            break;
                        case 1:
                            dist = Math.abs(prevDist - driveTrain.rw.getCurrentPosition());
                            break;
                        case 2:
                            dist = Math.abs(prevDist - driveTrain.blw.getCurrentPosition());
                            break;
                        case 3:
                            dist = Math.abs(prevDist - driveTrain.brw.getCurrentPosition());
                    }

                    fileExport[i] = wheelId;
                    i++;
                    fileExport[i] = (char)(dist >> 16);
                    i++;
                    fileExport[i] = (char)(dist & 0x0000ffff);
                    i++;

                    ReadWriteFile.writeFile(recordFile, String.copyValueOf(fileExport));
                    stepsStored++;
                }
            }
            prevLSX = gamepad1.left_stick_x;
            prevLSY = gamepad1.left_stick_y;
            prevRSX = gamepad1.right_stick_x;
            prevRSY = gamepad1.right_stick_y;
            firstLoop = false;

            if(stepsStored > 0)
            {
                if(gamepad1.dpad_right )
                {
                    if(!pressedright)
                        x += 11;
                    pressedright = true;
                }
                else pressedright = false;
                if(gamepad1.dpad_up)
                {
                    if(!pressedup)
                        x -= 11;
                    pressedup = true;
                }
                else pressedup = false;
                telemetry.addData("FileStep", x / 8);

                char[] array = ReadWriteFile.readFile(recordFile).toCharArray();

                int neg;
                if(((int)(array[x]) & 0b1000000000000000) > 0)
                {
                    neg = -1;
                    array[x] &= 0b0111111111111111;
                }else
                    neg = 1;

                int temp = ((int)((((int)(array[x])) << 16) | (((int)(array[x+1])))));

                float tLSX = Float.intBitsToFloat(temp) * neg;




                if(((int)(array[x+2]) & 0b1000000000000000) > 0)
                {
                    neg = -1;
                    array[x+2] &= 0b0111111111111111;
                }else
                    neg = 1;

                temp = ((int)((((int)(array[x+2])) << 16) | (((int)(array[x+3])))));

                float tLSY = Float.intBitsToFloat(temp) * neg;


                if(((int)(array[x+4]) & 0b1000000000000000) > 0)
                {
                    neg = -1;
                    array[x+4] &= 0b0111111111111111;
                }else
                neg = 1;

                temp = ((int)((((int)(array[x+4])) << 16) | (((int)(array[x+5])))));

                float tRSX = Float.intBitsToFloat(temp) * neg;



                if(((int)(array[x+6]) & 0b1000000000000000) > 0)
                {
                    neg = -1;
                    array[x+6] &= 0b0111111111111111;
                }else
                    neg = 1;

                temp = ((int)((((int)(array[x+6])) << 16) | (((int)(array[x+7])))));

                float tRSY = Float.intBitsToFloat(temp) * neg;
/*
                float tLSX = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+1])))));

                float tLSY = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+2])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+3])))));

                float tRSX = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+4])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+5])))));

                float tRSY = Float.intBitsToFloat((int)(Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+6])) << 16) + Integer.toUnsignedLong(((int)(ReadWriteFile.readFile(recordFile).toCharArray()[x+7])))));
*/
                int dist = (((int)array[x+8]) << 16) | ((int)array[x+9]);

                telemetry.addData("LeftX", tLSX);
                telemetry.addData("LeftY", tLSY);
                telemetry.addData("RightX", tRSX);
                telemetry.addData("RightY", tRSY);
                telemetry.addData("TrackedWheel", array[x+10]);
                telemetry.addData("dist", dist);
                telemetry.update();
            }


            //DRIVE
            driveTrain.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad1.dpad_left)
                simulateTouchSensor = true;
            if(gamepad1.dpad_down)
                simulateTouchSensor = false;

            if(objectGrab.touchSensor.isPressed() || simulateTouchSensor) {
                objectGrab.leftGrab.setPower(-0.5);
                objectGrab.rightGrab.setPower(0.01);

                armTargetPosition = 5400;

                useSafeRotate = true;
                safeRotateTargetPosition = 5100;
            }


            if(gamepad2.a || gamepad1.a) {
                useSafeArmTarget = true;
                safeArmTargetPosition = 0;
                objectGrab.leftGrab.setPower(-1);
                objectGrab.rightGrab.setPower(1);
            } else {
                if(!(objectGrab.touchSensor.isPressed() || simulateTouchSensor))
                {
                    useSafeArmTarget = true;
                    safeArmTargetPosition = 1000;
                    objectGrab.rightGrab.setPower(0);
                    objectGrab.leftGrab.setPower(-0);
                }
            }
            //SPIT OUT
            if(gamepad2.x || gamepad1.x){
                objectGrab.leftGrab.setPower(1);
                objectGrab.rightGrab.setPower(-1);
            }

            if(gamepad2.y || gamepad1.y){
                objectGrab.rightGrab.setPower(0);
                objectGrab.leftGrab.setPower(-0);

                useSafeRotate = true;
                safeRotateTargetPosition = 0;
            }


            telemetry.addData("Rotation", rotateTargetPosition);
            telemetry.addData("touch", objectGrab.touchSensor.isPressed());


            // Safe versions of variables logic (use the safe ones to ensure the arm does not snap)
            if(useSafeRotate)
            {
                if(objectGrab.upDownMotor.getCurrentPosition() >= 2500) {
                    rotateTargetPosition = safeRotateTargetPosition;
                    useSafeRotate = false;
                }
                else
                {
                    armTargetPosition = 5400;
                }
            }

            if(useSafeArmTarget)
            {
                if ((objectGrab.rotate.getCurrentPosition() > -300 && objectGrab.rotate.getCurrentPosition() < 300)) {
                    armTargetPosition = safeArmTargetPosition;
                    useSafeArmTarget = false;
                }
                else
                {
                    useSafeRotate = true;
                    safeRotateTargetPosition = 0;
                }
            }

            armTargetPosition -= (int)(gamepad2.left_stick_y * 15 * 5);
            telemetry.addData("position", armTargetPosition);
            telemetry.update();
            if(armTargetPosition < 0)
                armTargetPosition = 0;
            if(armTargetPosition > 5500)
                armTargetPosition = 5500;
            objectGrab.upDownMotor.setTargetPosition(armTargetPosition);
            objectGrab.upDownMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.upDownMotor.setPower(.75);

            rotateTargetPosition += (int)(gamepad2.right_stick_x * 50);
            if(rotateTargetPosition < - 5000)
                rotateTargetPosition = -5000;
            if(rotateTargetPosition > 5000)
                rotateTargetPosition = 5000;
            objectGrab.rotate.setTargetPosition(rotateTargetPosition);
            objectGrab.rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.rotate.setPower(1);


        }
    }
}
