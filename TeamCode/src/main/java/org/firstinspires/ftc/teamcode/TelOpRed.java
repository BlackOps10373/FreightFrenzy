//TELEOP RED
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TelOpRed", group = "TeleOp")
public class TelOpRed extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status:", "Building");
        telemetry.update();

        //CREATE INSTANCES OF CLASSES
        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);

        telemetry.addData("Status:", "Initialized");
        telemetry.update();

        //Wait til hit play
        waitForStart();

        // Full Rotation = 26880
        driveTrain.backTargetPosition = 0;
        driveTrain.frontTargetPosition = 0;

        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {
            // Drive Train -----------------------------------------------------------------------

            //HOLD POSITION
            //driveTrain.holdPosition();

            //ROCKCRAWLER
            if (gamepad1.left_trigger > 0.2)
                //driveTrain.rockCrawler(gamepad1.a);

            //Adjustments to Axles if Needed
            if (gamepad1.dpad_up)
                driveTrain.backTargetPosition += 50;
            if (gamepad1.dpad_down)
                driveTrain.backTargetPosition -= 50;
            if(gamepad1.dpad_left)
                driveTrain.frontTargetPosition -= 50;
            if(gamepad1.dpad_right)
                driveTrain.frontTargetPosition += 50;


            //Set Wheel Powers Before Gyro Adjust
            driveTrain.lwPower  = (gamepad1.left_stick_y - gamepad1.left_stick_x) * -driveTrain.moveSpeed;
            driveTrain.rwPower  = (gamepad1.left_stick_y + gamepad1.left_stick_x) * -driveTrain.moveSpeed;
            driveTrain.blwPower = (gamepad1.left_stick_y + gamepad1.left_stick_x) * -driveTrain.moveSpeed;
            driveTrain.brwPower = (gamepad1.left_stick_y - gamepad1.left_stick_x) * -driveTrain.moveSpeed;

            //Gyro Reset
            if (gamepad1.x)
                driveTrain.targetDegree = driveTrain.resetTargetDegree;

            //Left 90 Degree
            if (gamepad1.y)
                driveTrain.targetDegree = driveTrain.resetTargetDegree - 90;

            //GYRO STRAIGHT
            driveTrain.targetDegree += (gamepad1.right_stick_x * driveTrain.turnSpeed)
                    * ( ( driveTrain.moveSpeed - (( (Math.abs(driveTrain.lwPower) > Math.abs(driveTrain.rwPower))
                            ? Math.abs(driveTrain.lwPower) * driveTrain.moveTurnRatio
                                    : Math.abs(driveTrain.rwPower) * driveTrain.moveTurnRatio))) / driveTrain.moveSpeed);
            driveTrain.gyroStraight();
            telemetry.addData("heading", driveTrain.getHeading());
            telemetry.addData("targetDegree", driveTrain.targetDegree);
            telemetry.update();

            //Set Power To Wheels After Gyro Adjust
            driveTrain.lw.setPower(driveTrain.lwPower);
            driveTrain.rw.setPower(driveTrain.rwPower);
            driveTrain.blw.setPower(driveTrain.blwPower);
            driveTrain.brw.setPower(driveTrain.brwPower);

            // Object Grab -----------------------------------------------------------------------
            //EXTEND ARM
            if (gamepad2.left_stick_y < 0 && objectGrab.extend.getCurrentPosition() >= 3300)
                objectGrab.extend.setPower(0);
            else if (gamepad2.left_stick_y > 0 && objectGrab.extend.getCurrentPosition() <= 100)
                objectGrab.extend.setPower(0);
            else
                objectGrab.extend.setPower(-gamepad2.left_stick_y * .75);

            //MAGNET DROPPER
            if (gamepad2.a && objectGrab.fingerOut && objectGrab.firstPressA) {
                ElapsedTime fingerOutTime = new ElapsedTime();
                fingerOutTime.startTime();
                objectGrab.fingerServo.setPower(.5);
                while (fingerOutTime.milliseconds() < 1500) {
                    objectGrab.fingerServo.setPower(.5);
                }
                objectGrab.fingerServo.setPower(0);
                objectGrab.fingerOut = false;
                objectGrab.firstPressA = false;
            }
            else if (gamepad2.a && !objectGrab.fingerOut && objectGrab.firstPressA) {
                ElapsedTime fingerIn = new ElapsedTime();
                fingerIn.startTime();
                objectGrab.fingerServo.setPower(-1);
                while (fingerIn.milliseconds() < 1800) {
                    objectGrab.fingerServo.setPower(-1);
                }
                objectGrab.fingerServo.setPower(0);
                objectGrab.fingerOut = true;
                objectGrab.firstPressA = false;
            }
            else if (!gamepad2.a)
                objectGrab.firstPressA = true;

            //TURN ARM
            objectGrab.turnServo.setPosition(objectGrab.turnServo.getPosition() + gamepad2.left_stick_x * .015);
            if (gamepad2.y)
                objectGrab.turnServo.setPosition(.5);
            if (gamepad2.x)
                objectGrab.turnServo.setPosition(0);

            //UP AND DOWN ARM
            objectGrab.upDownMotor.setPower(-gamepad2.right_stick_y * .5);

            //DUCK SPINNER
           if(gamepad2.dpad_up)
                objectGrab.duck.setPower(.5);
            if(gamepad2.dpad_down)
                objectGrab.duck.setPower(-.5);
            if(gamepad2.dpad_right)
                objectGrab.duck.setPower(0);
        }


    }
}