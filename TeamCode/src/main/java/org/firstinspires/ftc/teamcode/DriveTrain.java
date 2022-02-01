package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import java.util.Arrays;

public class DriveTrain {
    // Declarations of all hardware
    Telemetry   telemetry;
    DcMotor     lw;
    DcMotor     rw;
    DcMotor     blw;
    DcMotor     brw;
    DcMotor     frontTwist;
    DcMotor     backTwist;
    DcMotor     tread;
    BNO055IMU   imu;
    ColorRangeSensor frontColorSensor;
    ColorRangeSensor backColorSensor;

    // Target Positions of frontTwist and backTwist
    int frontTargetPosition = 0;
    int backTargetPosition = 0;

    // Variables for Rock Crawler
    int frontTwistDelay = 0;
    int backTwistDelay = 0;
    boolean isFlipping = false;
    boolean firstRun = false;
    boolean forwardFacing = true;
    int blackCount;

    // Variables for Driving
    double lwPower;
    double rwPower;
    double blwPower;
    double brwPower;

    // Variables for Gyro
    double adjSpeed = 0.03;
    double minTurn = 0.01;
    int windowSize = 3;
    double targetDegree = 0.0;
    double resetTargetDegree = 0.0;
    int piecewiseWindow = 150;
    double piecewiseSpeed = 0.007517647057771725;
    double piecewiseMinTurn = 0.004;
    double turnSpeed = 8;

    // Drive Train Constructor
    public DriveTrain(Telemetry t, HardwareMap hardwareMap) {
        telemetry        = t;
        lw               = hardwareMap.get(DcMotor.class, "lw");
        rw               = hardwareMap.get(DcMotor.class, "rw");
        rw.setDirection(DcMotor.Direction.REVERSE);
        blw              = hardwareMap.get(DcMotor.class, "blw");
        brw              = hardwareMap.get(DcMotor.class, "brw");
        brw.setDirection(DcMotor.Direction.REVERSE);
        lw.setMode(RUN_WITHOUT_ENCODER);
        blw.setMode(RUN_WITHOUT_ENCODER);
        rw.setMode(RUN_WITHOUT_ENCODER);
        brw.setMode(RUN_WITHOUT_ENCODER);


        frontTwist       = hardwareMap.get(DcMotor.class, "frontTwist");
        frontTwist.setMode(STOP_AND_RESET_ENCODER);
        backTwist        = hardwareMap.get(DcMotor.class, "backTwist");
        backTwist.setDirection(DcMotor.Direction.REVERSE);
        backTwist.setMode(STOP_AND_RESET_ENCODER);
        tread            = hardwareMap.get(DcMotor.class, "tread");
        tread.setMode(RUN_WITHOUT_ENCODER);
        tread.setDirection(DcMotor.Direction.REVERSE);


        imu              = hardwareMap.get(BNO055IMU.class,"imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled = false;
        imu.initialize(parameters);
        while(!imu.isGyroCalibrated()){
            telemetry.addData("isCalibrating", "isCalibrating");
            telemetry.update();
        }
        telemetry.update();
        targetDegree = getHeading();
        resetTargetDegree = targetDegree;

        frontColorSensor = hardwareMap.get(ColorRangeSensor.class, "frontColorSensor");
        backColorSensor  = hardwareMap.get(ColorRangeSensor.class, "backColorSensor");
    }

    //Move Function For Auto
    public void move(String variation, int ticCount){
        int[] ticks = new int[4];
        lw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        blw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        switch (variation) {
            case "rotate":
                lw.setTargetPosition(-ticCount);
                blw.setTargetPosition(-ticCount);
                rw.setTargetPosition(ticCount);
                brw.setTargetPosition(ticCount);
                break;
            case "straight":
                lw.setTargetPosition(ticCount);
                blw.setTargetPosition(ticCount);
                rw.setTargetPosition(ticCount);
                brw.setTargetPosition(ticCount);
                break;
            case "diagonalRight":
                blw.setTargetPosition(ticCount);
                rw.setTargetPosition(ticCount);
                break;
            case "diagonalLeft":
                lw.setTargetPosition(ticCount);
                brw.setTargetPosition(ticCount);
                break;
            case "side":
                lw.setTargetPosition(-ticCount);
                blw.setTargetPosition(ticCount);
                rw.setTargetPosition(ticCount);
                brw.setTargetPosition(-ticCount);
                break;
        }
        lw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        blw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brw.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(lw.isBusy() || blw.isBusy() || rw.isBusy() || brw.isBusy()){
            holdPosition();
            //telemetry.addData("rw", rw.getCurrentPosition());
            //telemetry.addData("lw", lw.getCurrentPosition());
            //telemetry.addData("brw", brw.getCurrentPosition());
            //telemetry.addData("blw", blw.getCurrentPosition());
           // telemetry.update();

            ticks[0] = Math.abs(lw.getTargetPosition() - lw.getCurrentPosition());
            ticks[1] = Math.abs(blw.getTargetPosition() - blw.getCurrentPosition());
            ticks[2] = Math.abs(brw.getTargetPosition() - brw.getCurrentPosition());
            ticks[3] = Math.abs(rw.getTargetPosition() - rw.getCurrentPosition());
            Arrays.sort(ticks);

            lwPower = (lw.getTargetPosition() - lw.getCurrentPosition()) * 1.0 / ticks[3];
            blwPower = (blw.getTargetPosition() - blw.getCurrentPosition()) * 1.0 / ticks[3];
            rwPower = (rw.getTargetPosition() - rw.getCurrentPosition()) * 1.0 / ticks[3];
            brwPower = (brw.getTargetPosition() - brw.getCurrentPosition()) * 1.0 / ticks[3];

            gyroStraight();

            if(ticks[3] > 1000) {
                lw.setPower(lwPower / 3);
                blw.setPower(blwPower / 3);
                rw.setPower(rwPower / 3);
                brw.setPower(brwPower / 3);
            }
            else if(ticks[3] < 500){
                lw.setPower(lwPower / 8);
                blw.setPower(blwPower / 8);
                rw.setPower(rwPower / 8);
                brw.setPower(brwPower / 8);
            }
            else {
                lw.setPower(lwPower / 5);
                blw.setPower(blwPower / 5);
                rw.setPower(rwPower / 5);
                brw.setPower(brwPower / 5);
            }
        }
        lw.setPower(0);
        rw.setPower(0);
        blw.setPower(0);
        brw.setPower(0);
    }

    //Barrier Function
    public void rockCrawler(double trigger) {
        if (forwardFacing) {
            telemetry.addData("frontTwistDelay", frontTwistDelay);
            telemetry.update();
            frontTwistDelay++;
            if (trigger > 0.2 && !firstRun && frontTwistDelay > 30) {
                frontTargetPosition = 3270;
                frontTwist.setTargetPosition(frontTargetPosition);
                frontTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                firstRun = true;
                blackCount++;
                isFlipping = true;
                tread.setPower(0.5);
                brw.setPower(0.33);
                lw.setPower(0);
                rw.setPower(0);
                blw.setPower(0.33);
            } else if ((frontTargetPosition - 50 <= frontTwist.getCurrentPosition()) &&
                    (frontTwist.getCurrentPosition() <= frontTargetPosition + 50) && blackCount >= 1) {
                backTargetPosition = 3270;
                backTwist.setTargetPosition(backTargetPosition);
                backTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                forwardFacing = false;
                frontTwistDelay = 0;
                firstRun = false;
                blackCount = 0;
                isFlipping = false;
                tread.setPower(0.5);
                blw.setPower(0);
                brw.setPower(0);
                lw.setPower(0.33);
                rw.setPower(0.33);
            }
        } else if (!forwardFacing) {
            telemetry.addData("backTwistDelay", backTwistDelay);
            telemetry.update();
            backTwistDelay++;
            if (trigger > 0.2 && !firstRun && backTwistDelay > 30) {
                backTargetPosition = 390;
                backTwist.setTargetPosition(backTargetPosition);
                backTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                firstRun = true;
                blackCount++;
                isFlipping = true;
                tread.setPower(-0.5);
                blw.setPower(0);
                brw.setPower(0);
                lw.setPower(-0.33);
                rw.setPower(-0.33);
            } else if ((backTargetPosition - 50 <= backTwist.getCurrentPosition()) && (backTwist.getCurrentPosition() <= backTargetPosition + 50) && blackCount >= 1) {
                frontTargetPosition = 390;
                frontTwist.setTargetPosition(frontTargetPosition);
                frontTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                forwardFacing = true;
                backTwistDelay = 0;
                firstRun = false;
                blackCount = 0;
                isFlipping = false;
                tread.setPower(-0.5);
                brw.setPower(-0.33);
                lw.setPower(0);
                rw.setPower(0);
                blw.setPower(-0.33);
            }
        }
    }

    // Hold Axels in Place
    public void holdPosition() {
            if (!frontTwist.isBusy() && !backTwist.isBusy()) {
                tread.setPower(0);
                frontTwist.setPower(0);
                backTwist.setPower(0);
                frontTwist.setTargetPosition(frontTargetPosition);
                backTwist.setTargetPosition(backTargetPosition);
                frontTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backTwist.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backTwist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            } else {
                frontTwist.setPower(1);
                backTwist.setPower(1);
            }
    }

    public double degreeCalc(double degree){
        double returnDegree = degree;
        if(returnDegree < 0){
            returnDegree = returnDegree + 360;
        }

        if(returnDegree >= 360){
            returnDegree = returnDegree - 360;
        }
        return returnDegree;

    }

    public double degreeCalc180(double degree){
        double returnDegree = degree;
        if(returnDegree < -180){
            returnDegree = returnDegree + 360;
        }
        else if(returnDegree >= 180){
            returnDegree = returnDegree - 360;
        }
        return returnDegree;

    }

    public void turnPower(double amount){
        lwPower += amount;
        rwPower -= amount;
        blwPower += amount;
        brwPower -= amount;
    }

    public void gyroStraight() {
        targetDegree = degreeCalc180(targetDegree);

        if (degreeCalc(getHeading() - targetDegree) > windowSize + piecewiseWindow && degreeCalc(getHeading() - targetDegree) <= 180) {
            if ((Math.pow(degreeCalc(getHeading() - targetDegree) * adjSpeed, 2)) >= minTurn) {
                turnPower(-( Math.pow(degreeCalc(getHeading() - targetDegree) * adjSpeed, 2)));
            }
            else{
                turnPower(-minTurn);
            }
        }

        if (degreeCalc(getHeading() - targetDegree) < 360 - windowSize - piecewiseWindow && degreeCalc(getHeading() - targetDegree) > 180) {
            if ((Math.pow((360 - degreeCalc(getHeading() - targetDegree)) * adjSpeed, 2) >= minTurn)) {
                turnPower(Math.pow((360 - degreeCalc(getHeading() - targetDegree)) * adjSpeed, 2));
            }
            else {
                turnPower(minTurn);
            }
        }
        // Second graph function (piecewise) the one that is closer to 0 degrees
        if (degreeCalc(getHeading() - targetDegree) > windowSize && degreeCalc(getHeading() - targetDegree) <= piecewiseWindow + windowSize) {
            if ((Math.sqrt(degreeCalc(getHeading() - targetDegree) * piecewiseSpeed)) >= minTurn) {
                turnPower(-(Math.sqrt(degreeCalc(getHeading() - targetDegree) * piecewiseSpeed)));
            }
            else {
                turnPower(-minTurn);
            }
        }

        if (degreeCalc(getHeading() - targetDegree) < 360 - windowSize && degreeCalc(getHeading() - targetDegree) > 360 - piecewiseWindow - windowSize) {
            if (Math.sqrt((360 - degreeCalc(getHeading() - targetDegree)) * piecewiseSpeed) >= minTurn) {
                turnPower((Math.sqrt((360 - degreeCalc(getHeading() - targetDegree)) * piecewiseSpeed) + piecewiseMinTurn));
            }
            else {
                turnPower(minTurn);
            }
        }
    }

    public double getHeading() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return -angles.firstAngle;
    }





}
