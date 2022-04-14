
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
        int armTargetPosition = 0;
        boolean useSafeArmTarget = false;
        int safeArmTargetPosition = 0;
        int rotateTargetPosition = 0;
        boolean useSafeRotate = false;
        int safeRotateTargetPosition = 0;
        int inOutTargetDegree = 0;

        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {
            //DRIVE
            driveTrain.move(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);


            if(objectGrab.touchSensor.isPressed()) {
                objectGrab.leftGrab.setPower(-0.5);
                objectGrab.rightGrab.setPower(0.01);

                armTargetPosition = 5400;

                useSafeRotate = true;
                safeRotateTargetPosition = 3000;
            }


            if(gamepad2.a || gamepad1.a) {
                useSafeArmTarget = true;
                safeArmTargetPosition = 0;
                objectGrab.leftGrab.setPower(-1);
                objectGrab.rightGrab.setPower(1);
            } else {
                if(!objectGrab.touchSensor.isPressed())
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



            if(objectGrab.upDownMotor.getCurrentPosition() <1500 )

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
            objectGrab.rotate.setTargetPosition(rotateTargetPosition);
            objectGrab.rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.rotate.setPower(1);
        }
    }
}
