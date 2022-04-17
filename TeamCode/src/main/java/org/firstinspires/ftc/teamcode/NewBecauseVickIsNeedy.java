
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
        int armPositionSteps = 0;

        boolean turnGripped = false;
        double rAngleDiff = 0;
        boolean useSafeArmTarget = false;
        int safeArmTargetPosition = 0;
        boolean useSafeRotate = false;
        int safeRotateTargetPosition = 0;
        int inOutTargetDegree = 0;

        boolean simulateTouchSensor = false;

        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {

            //DRIVE ------------------------------------------------------------------------------
            driveTrain.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            if(gamepad1.dpad_left)
                simulateTouchSensor = true;
            if(gamepad1.dpad_down)
                simulateTouchSensor = false;
            if(gamepad1.b)
                armPositionSteps = 1;
            if(gamepad1.y)
                armPositionSteps = 2;
            if(gamepad1.right_bumper)
                armPositionSteps = 0;

            if(objectGrab.touchSensor.isPressed() || simulateTouchSensor) {
                objectGrab.leftGrab.setPower(-0.5);
                objectGrab.rightGrab.setPower(0.01);
                switch(armPositionSteps) {
                    case 0:
                        objectGrab.armTargetPosition = 5400;
                        break;
                    case 1:
                        objectGrab.armTargetPosition = 3300;
                        break;
                    case 2:
                        objectGrab.armTargetPosition = 2500;
                        break;
                }
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
                    objectGrab.armTargetPosition = 3000;
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


            telemetry.addData("Rotation", objectGrab.rotateTargetPosition);
            telemetry.addData("touch", objectGrab.touchSensor.isPressed());


            // Safe versions of variables logic (use the safe ones to ensure the arm does not snap)
            if(useSafeRotate)
            {
                if(objectGrab.upDownMotor.getCurrentPosition() >= 2500) {
                    objectGrab.rotateTargetPosition = safeRotateTargetPosition;
                    useSafeRotate = false;
                }
                else
                {
                    objectGrab.armTargetPosition = 5400;
                }
            }
            if(useSafeArmTarget)
            {
                if ((objectGrab.rotate.getCurrentPosition() > -300 && objectGrab.rotate.getCurrentPosition() < 300)) {
                    objectGrab.armTargetPosition = safeArmTargetPosition;
                    useSafeArmTarget = false;
                }
                else
                {
                    useSafeRotate = true;
                    safeRotateTargetPosition = 0;
                }
            }

            //player controlled movement
            objectGrab.armMovement((int)(gamepad2.left_stick_y * 75), (int)(gamepad2.right_stick_x * 50));

            //Setting the Arms to the right encoder Marks
        }
    }
}
