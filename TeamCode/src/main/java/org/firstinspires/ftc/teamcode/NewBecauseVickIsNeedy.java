//TELEOP RED
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "NewBecauseVickIsNeedy", group = "TeleOp")
public class NewBecauseVickIsNeedy extends LinearOpMode {

    @Override
    public void runOpMode() {

        DriveTrain driveTrain = new DriveTrain(telemetry, hardwareMap);

        waitForStart();
        // Run Until Match is Over ---------------------------------------------------------------
        while (opModeIsActive()) {

            // SET ORIGINAL POWERS TO WHEELS
            driveTrain.lwPower = (gamepad1.left_stick_y - gamepad1.left_stick_x) * -.75;
            driveTrain.rwPower = (gamepad1.left_stick_y + gamepad1.left_stick_x) * -.75;
            driveTrain.blwPower = (gamepad1.left_stick_y + gamepad1.left_stick_x) * -.75;
            driveTrain.brwPower = (gamepad1.left_stick_y - gamepad1.left_stick_x) * -.75;

            // RESET TO ORIGINAL DEGREE
            if (gamepad1.x)
                driveTrain.targetDegree = driveTrain.resetTargetDegree;

            // TURNING THE ROBOT WITH GYRO
            driveTrain.targetDegree += (gamepad1.right_stick_x * driveTrain.turnSpeed)
                    * ( ( driveTrain.moveSpeed - (( (Math.abs(driveTrain.lwPower) > Math.abs(driveTrain.rwPower))
                    ? Math.abs(driveTrain.lwPower) * driveTrain.moveTurnRatio
                    : Math.abs(driveTrain.rwPower) * driveTrain.moveTurnRatio))) / driveTrain.moveSpeed );

            // GYRO STRAIGHT
            driveTrain.gyroStraight();

            // SETTING FINAL POWERS TO WHEELS
            driveTrain.lw.setPower(driveTrain.lwPower);
            driveTrain.rw.setPower(driveTrain.rwPower);
            driveTrain.blw.setPower(driveTrain.blwPower);
            driveTrain.brw.setPower(driveTrain.brwPower);
        }
    }
}
