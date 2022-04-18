
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
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
        ElapsedTime elapsedTime = new ElapsedTime();
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

            //TOUCH SENSOR BACKUP
            if(gamepad1.dpad_left)
                simulateTouchSensor = true;
            if(gamepad1.dpad_down)
                simulateTouchSensor = false;

            //MEDIUM
            if(gamepad1.b)
                armPositionSteps = 1;
            //LOW
            if(gamepad1.y)
                armPositionSteps = 2;
            //HIGH
            if(gamepad1.right_bumper)
                armPositionSteps = 0;

            //SPIT OUT
            if(gamepad2.x || gamepad1.x){
                objectGrab.leftGrab.setPower(-1);
                objectGrab.rightGrab.setPower(1);
                elapsedTime.startTime();
            }

            //STOP SERVOS
            if(gamepad2.y){
                objectGrab.rightGrab.setPower(0);
                objectGrab.leftGrab.setPower(-0);
            }

            //PICK UP
            if(gamepad2.a || gamepad1.a){
                objectGrab.rightGrab.setPower(1);
                objectGrab.leftGrab.setPower(-1);
                objectGrab.armTargetPosition = 50;
            }

            //IF NO BLOCK TURN OFF SERVO
            if(!objectGrab.touchSensor.isPressed()){
                objectGrab.leftGrab.setPower(0);
                objectGrab.rightGrab.setPower(0);
            }

            //PICK UP AS WELL??
            if(gamepad2.b) {
                objectGrab.leftGrab.setPower(1);
                objectGrab.rightGrab.setPower(-1);
            }

            if(objectGrab.touchSensor.isPressed() || simulateTouchSensor) {
                objectGrab.leftGrab.setPower(-0.5);
                objectGrab.rightGrab.setPower(0.01);
                switch(armPositionSteps) {
                    case 0:
                        objectGrab.armTargetPosition = 5400;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            objectGrab.rotateTargetPosition = 5000;
                        break;
                    case 1:
                        objectGrab.armTargetPosition = 3300;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            objectGrab.rotateTargetPosition = 5000;
                        break;
                    case 2:
                        objectGrab.armTargetPosition = 2500;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            objectGrab.rotateTargetPosition = 5000;
                        break;
                }
            } else if (elapsedTime.milliseconds() > 2000) {
                objectGrab.armTargetPosition = 2800;
                objectGrab.rotateTargetPosition = 0;
                if(objectGrab.rotate.getCurrentPosition() == 0){
                    elapsedTime.reset();
                    objectGrab.armTargetPosition = 800;
                }
            }

            telemetry.addData("Rotation", objectGrab.rotateTargetPosition);
            telemetry.addData("touch", objectGrab.touchSensor.isPressed());
            telemetry.addData("rotate pos", objectGrab.rotate.getCurrentPosition());
            /*if(objectGrab.upDownMotor.getCurrentPosition() < 2800 && (objectGrab.rotate.getCurrentPosition() > -2200 && objectGrab.rotate.getCurrentPosition() < 2200))
                objectGrab.armMovement((int)(gamepad2.left_stick_y * 75), 0);
            else if(objectGrab.upDownMotor.getCurrentPosition() < 3000 && ((objectGrab.rotate.getCurrentPosition() > -2000 && objectGrab.rotate.getCurrentPosition() < -1400) || (objectGrab.rotate.getCurrentPosition() < 2000 && objectGrab.rotate.getCurrentPosition() > 1400)))
                objectGrab.armMovement(0, (int)(gamepad2.right_stick_x * 50));
            else*/
                objectGrab.armMovement((int)(gamepad2.left_stick_y * 42), (int)(gamepad2.right_stick_x * 50));

            //Setting the Arms to the right encoder Marks
        }
    }
}
