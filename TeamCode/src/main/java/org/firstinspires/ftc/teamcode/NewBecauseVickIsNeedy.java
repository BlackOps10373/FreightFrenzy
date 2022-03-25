
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "NewBecauseVickIsNeedy", group = "TeleOp")
public class NewBecauseVickIsNeedy extends LinearOpMode {

    @Override
    public void runOpMode() {

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);
        ObjectGrab objectGrab = new ObjectGrab(telemetry, hardwareMap);

        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {

            // SET ORIGINAL POWERS TO WHEELS
            driveTrain.lwPower = (gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) * -.75;
            driveTrain.rwPower = (gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) * -.75;
            driveTrain.blwPower = (gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) * -.75;
            driveTrain.brwPower = (gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) * -.75;

            // RESET TO ORIGINAL DEGREE
            //if (gamepad1.x)
                driveTrain.targetDegree = driveTrain.resetTargetDegree;

            // TURNING THE ROBOT WITH GYRO
           // driveTrain.targetDegree += gamepad1.right_stick_x * 8;

            // GYRO STRAIGHT
           // driveTrain.gyroStraight();

            // SETTING FINAL POWERS TO WHEELS
            driveTrain.lw.setPower(driveTrain.lwPower);
            driveTrain.rw.setPower(driveTrain.rwPower);
            driveTrain.blw.setPower(driveTrain.blwPower);
            driveTrain.brw.setPower(driveTrain.brwPower);

            if(gamepad1.dpad_down){
                objectGrab.leftGrab.setPower(.25);
                objectGrab.rightGrab.setPower(-.25);
            }
            if(gamepad1.dpad_up){
                objectGrab.leftGrab.setPower(-.25);
                objectGrab.rightGrab.setPower(.25);
            }
            if(gamepad1.dpad_right || gamepad1.dpad_left){
                objectGrab.rightGrab.setPower(0);
                objectGrab.leftGrab.setPower(0);
            }
        }
    }
}
