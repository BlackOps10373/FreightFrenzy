
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
        int armPositionSteps = 0;

        boolean useSafeArmTarget = false;
        boolean useSafeRotate = false;
        int armTargetPosition = 1200;
        int rotateTargetPosition = 0;

        boolean isNotSafe = false;

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

            //STOP SERVOS
            if(gamepad2.y){
                objectGrab.rightGrab.setPower(0);
                objectGrab.leftGrab.setPower(-0);
            }

            //PICK UP
            if(gamepad2.a || gamepad1.a){
                objectGrab.rightGrab.setPower(1);
                objectGrab.leftGrab.setPower(-1);
                armTargetPosition = 50;
            }

            //IF NO BLOCK TURN OFF SERVO
            if(!(objectGrab.touchSensor.isPressed()) && !(gamepad1.a)){
                objectGrab.leftGrab.setPower(0);
                objectGrab.rightGrab.setPower(0);
            }

            //PICK UP AS WELL??
            if(gamepad2.b) {
                objectGrab.leftGrab.setPower(1);
                objectGrab.rightGrab.setPower(-1);
            }

            if(objectGrab.touchSensor.isPressed() || simulateTouchSensor) {
                if(!(gamepad1.x || gamepad2.x)){
                    objectGrab.leftGrab.setPower(-0.5);
                    objectGrab.rightGrab.setPower(0.01);
                }
                else if(gamepad1.x || gamepad2.x){
                    objectGrab.leftGrab.setPower(1);
                    objectGrab.rightGrab.setPower(-1);
                    elapsedTime.startTime();
                }
                switch(armPositionSteps) {
                    case 0:
                        armTargetPosition = 5400;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            rotateTargetPosition = 5000;
                        break;
                    case 1:
                        armTargetPosition = 3300;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            rotateTargetPosition = 5000;
                        break;
                    case 2:
                        armTargetPosition = 2500;
                        if(objectGrab.upDownMotor.getCurrentPosition() > 2800)
                            rotateTargetPosition = 5000;
                        break;
                }
            }

            if(!(objectGrab.touchSensor.isPressed()) && elapsedTime.milliseconds() > 3000) {
                armTargetPosition = 2800;
                rotateTargetPosition = 0;
                if(objectGrab.rotate.getCurrentPosition() == 0){
                    elapsedTime.reset();
                    armTargetPosition = 1200;
                }
            }

            //SETTING THE SAFE BOUNDARIES
            //ARM
            if(armTargetPosition >= 5500){
                armTargetPosition = 5500;
            }

            if(!(objectGrab.rotate.getCurrentPosition() > -480 && objectGrab.rotate.getCurrentPosition() < 480) || !(objectGrab.rotate.getCurrentPosition() > 5000-480 && objectGrab.rotate.getCurrentPosition() < 5000+480) || !(rotateTargetPosition > -480 && rotateTargetPosition < 480) && !(rotateTargetPosition > 5000-480 && rotateTargetPosition < 5000+480)){
                useSafeArmTarget = false;
            }else{
                useSafeArmTarget = true;
            }

            //SETTING THE POSITION TO BE HIGHER THAN ALL OF THE STUFF IF IT IS NOT SAFE TO ROTATE
            if(useSafeArmTarget = false && armTargetPosition < 3200){
                armTargetPosition = 3200;
            }

            //ROTATE
            if(armTargetPosition < 3200 && (rotateTargetPosition <-400 && !(rotateTargetPosition < -3000))){
                rotateTargetPosition = -400;
            }

            if(armTargetPosition < 3200 && (rotateTargetPosition > 400 && !(rotateTargetPosition > 3000))){
                rotateTargetPosition = 400;
            }

            if(armTargetPosition < 3200 && (rotateTargetPosition < 5000-400 && (!(rotateTargetPosition < 400 && rotateTargetPosition > -400) && !(rotateTargetPosition < -3000)))){
                rotateTargetPosition = 5000-480;
            }

            if(armTargetPosition < 3200 && (rotateTargetPosition > -5000+400 && (!(rotateTargetPosition < 400 && rotateTargetPosition > -400) && !(rotateTargetPosition > 3000)))){
                rotateTargetPosition = 5000-480;
            }

            //LIMIT ROTATING IN GENERAL
            if(rotateTargetPosition > 5400){
                rotateTargetPosition = 5400;
            }

            if(rotateTargetPosition < -5400){
                rotateTargetPosition = -5400;
            }

            //SOME TELEMETRY
            telemetry.addData("ROTATE", objectGrab.rotate.getCurrentPosition());
            telemetry.addData("UPDOWN", objectGrab.upDownMotor.getCurrentPosition());
            telemetry.addData("NOT SAFE ROTATE", true);
            telemetry.update();

            objectGrab.upDownMotor.setTargetPosition(armTargetPosition);
            objectGrab.upDownMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.upDownMotor.setPower(.75);

            objectGrab.rotate.setTargetPosition(rotateTargetPosition);
            objectGrab.rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            objectGrab.rotate.setPower(1);
        }
    }
}
