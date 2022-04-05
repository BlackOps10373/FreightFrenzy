
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "NewBecauseVickIsNeedy", group = "TeleOp")
public class NewBecauseVickIsNeedy extends LinearOpMode {

    @Override
    public void runOpMode() {

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);
        double powerchange = -0.5;

        boolean turnGripped = false;
        double rAngleDiff = 0;

        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {
            int armTargetPosition = 0;
            double driveTurn = gamepad1.right_stick_x;
            double XCoordinate = gamepad1.left_stick_x;
            double YCoordinate = -gamepad1.left_stick_y;

            double gamepadHypot = Range.clip(Math.hypot(XCoordinate, YCoordinate), 0, 1);
            double gamepadDegree = -(Math.toDegrees(Math.atan2(YCoordinate, XCoordinate)) - 90);
            gamepadDegree = driveTrain.degreeCalc180(gamepadDegree);

            telemetry.addData("gamepadDegree", gamepadDegree);
            //the inverse tangent of opposite/adjacent gives us our gamepad degree
            double robotDegree = driveTrain.getHeading();
            telemetry.addData("robotDegree", robotDegree);
            //gives us the angle our robot is at
            double movementDegree = gamepadDegree - robotDegree;
            telemetry.addData("movementDegree", movementDegree);

            //adjust the angle we need to move at by finding needed movement degree based on gamepad and robot angles
            double gamepadXControl = Math.sin(Math.toRadians(movementDegree)) * gamepadHypot;
            telemetry.addData("X", gamepadXControl);
            //by finding the adjacent side, we can get our needed x value to power our motors
            double gamepadYControl = Math.cos(Math.toRadians(movementDegree)) * gamepadHypot;
            telemetry.addData("Y", gamepadYControl);
            //by finding the opposite side, we can get our needed y value to power our motors
            telemetry.update();

            // The rotation controls that are similar to flick-stick style controls
            // It changes the targetDegree for the gyro straight
            /*
            double rXCoordinate = gamepad1.right_stick_x;
            double rYCoordinate = -gamepad1.right_stick_y;

            double rgamepadHypot = Range.clip(Math.hypot(rXCoordinate, rYCoordinate), 0, 1);
            if(rgamepadHypot > 0.99) {
                double rgamepadDegree = -(Math.toDegrees(Math.atan2(rYCoordinate, rXCoordinate)) - 90);
                rgamepadDegree = driveTrain.degreeCalc180(rgamepadDegree);
                if(!turnGripped) {
                    // Activates each time you move the stick to the outer ring (outer ring gripped)
                    turnGripped = true;


                    rAngleDiff = rgamepadDegree - driveTrain.targetDegree;
                }

                driveTrain.targetDegree = driveTrain.degreeCalc180(rgamepadDegree - rAngleDiff);

                telemetry.addData("rgamepadDegree", rgamepadDegree);
            }
            else turnGripped = false;


            //GYRO STRAIGHT

            driveTrain.rwPower = gamepadYControl * Math.abs(gamepadYControl) - gamepadXControl * Math.abs(gamepadXControl);
            driveTrain.brwPower = gamepadYControl * Math.abs(gamepadYControl) + gamepadXControl * Math.abs(gamepadXControl);
            driveTrain.lwPower = gamepadYControl * Math.abs(gamepadYControl) + gamepadXControl * Math.abs(gamepadXControl);
            driveTrain.blwPower = gamepadYControl * Math.abs(gamepadYControl) - gamepadXControl * Math.abs(gamepadXControl);

            driveTrain.gyroStraight();

            driveTrain.rw.setPower(driveTrain.rwPower);
            driveTrain.brw.setPower(driveTrain.brwPower);
            driveTrain.lw.setPower(driveTrain.lwPower);
            driveTrain.blw.setPower(driveTrain.blwPower);
            */

            driveTrain.rw.setPower(gamepadYControl * Math.abs(gamepadYControl) - gamepadXControl * Math.abs(gamepadXControl) + driveTurn);
            driveTrain.brw.setPower(gamepadYControl * Math.abs(gamepadYControl) + gamepadXControl * Math.abs(gamepadXControl) + driveTurn);
            driveTrain.lw.setPower(gamepadYControl * Math.abs(gamepadYControl) + gamepadXControl * Math.abs(gamepadXControl) - driveTurn);
            driveTrain.blw.setPower(gamepadYControl * Math.abs(gamepadYControl) - gamepadXControl * Math.abs(gamepadXControl) - driveTurn);

            /*frontRight.setPower(driveVertical - driveHorizontal + driveTurn);
            backRight.setPower(driveVertical + driveHorizontal + driveTurn);
            frontLeft.setPower(driveVertical + driveHorizontal - driveTurn);
            backLeft.setPower(driveVertical - driveHorizontal - driveTurn);*/

            /*armTargetPosition += (int)(gamepad2.left_stick_y * 10);
            armUpDown(armTargetPosition);
            objectGrab.upDownMotor.setTargetPosition(armTargetPosition);
            objectGrab.upDownMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.upDownMotor.setPower(.5);*/

        }
    }
}
