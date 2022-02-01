//TELEOP
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TelOp", group = "TeleOp")
public class TelOp extends LinearOpMode {

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
        driveTrain.backTargetPosition = 390;
        driveTrain.frontTargetPosition = 390;
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {

            // Drive Train -----------------------------------------------------------------------
            //HOLD AXEL POSITIONS
            driveTrain.holdPosition();

            //ROCKCRAWLER
            if (gamepad1.left_bumper || gamepad2.left_bumper)
                driveTrain.rockCrawler(gamepad1.left_trigger);
            if (gamepad1.dpad_up)
                driveTrain.backTargetPosition += 5;
            if (gamepad1.dpad_down)
                driveTrain.backTargetPosition -= 5;
            if(gamepad1.dpad_left)
                driveTrain.frontTargetPosition -= 5;
            if(gamepad1.dpad_right)
                driveTrain.frontTargetPosition += 5;

            //Set Wheel Powers Before Gyro Adjust
            driveTrain.lwPower  = (gamepad1.left_stick_y) * (-4.0 / 10);
            driveTrain.rwPower  = (gamepad1.left_stick_y) * (-4.0 / 10);
            driveTrain.blwPower = (gamepad1.left_stick_y) * (-4.0 / 10);
            driveTrain.brwPower = (gamepad1.left_stick_y) * (-4.0 / 10);

            //GYRO RESET
            if (gamepad1.x)
                driveTrain.targetDegree = driveTrain.resetTargetDegree;
            if (gamepad1.y)
                driveTrain.targetDegree = driveTrain.resetTargetDegree - 90;

            //GYRO STRAIGHT
            driveTrain.targetDegree += gamepad1.right_stick_x * driveTrain.turnSpeed;
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
            if (gamepad2.right_stick_y < 0 && objectGrab.extend.getCurrentPosition() >= 3300)
                objectGrab.extend.setPower(0);
            else if (gamepad2.right_stick_y > 0 && objectGrab.extend.getCurrentPosition() <= 100)
                objectGrab.extend.setPower(0);
            else
                objectGrab.extend.setPower(-gamepad2.right_stick_y);

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
            else if (!gamepad2.a)
                objectGrab.firstPressA = true;

            if (gamepad2.a && !objectGrab.fingerOut && objectGrab.firstPressA) {
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
            objectGrab.turnServo.setPosition(objectGrab.turnServo.getPosition() + gamepad2.left_stick_x * .02);
            if (gamepad2.y)
                objectGrab.turnServo.setPosition(.5);
            if (gamepad2.x)
                objectGrab.turnServo.setPosition(0);

            //UP AND DOWN ARM
            objectGrab.upDownServo.setPower(-gamepad2.left_stick_y);

            //DUCK SPINNER
            if(gamepad2.dpad_up)
                objectGrab.duck.setPower(.5);
            if(gamepad2.dpad_down)
                objectGrab.duck.setPower(0);
        }


    }
}