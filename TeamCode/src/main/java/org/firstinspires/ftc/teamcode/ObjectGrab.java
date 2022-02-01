package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ObjectGrab {
    Telemetry telemetry;

    Servo turnServo;
    CRServo upDownServo;
    CRServo fingerServo;
    CRServo duck;

    DcMotor extend;

    boolean firstPressA = true;
    boolean fingerOut = true;

    public ObjectGrab(Telemetry t, HardwareMap hardwareMap){
        telemetry   = t;

        turnServo   = hardwareMap.get(Servo.class, "turnServo");
        turnServo.setPosition(.5);
        upDownServo = hardwareMap.get(CRServo.class, "upDownServo");
        fingerServo = hardwareMap.get(CRServo.class, "fingerServo");
        duck        = hardwareMap.get(CRServo.class, "duck");
        extend      = hardwareMap.get(DcMotor.class, "extend");
        extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void BlockPlace(int level, int upDown, double turn) {
        ElapsedTime blockPlaceTime = new ElapsedTime();
        blockPlaceTime.startTime();
        turnServo.setPosition(turn);
        upDownServo.setPower(1 * upDown);
        extend.setPower(.60);
        extend.setTargetPosition(3300);
        extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (extend.isBusy()) {
            if (blockPlaceTime.milliseconds() > level)
                upDownServo.setPower(0);
        }
        blockPlaceTime.reset();
        while (blockPlaceTime.milliseconds() < 500) {
            fingerServo.setPower(-.25);
        }
        fingerServo.setPower(0);
    }
    public void Retract(int level, int upDown, double turn) {
        ElapsedTime retractTime = new ElapsedTime();
        retractTime.startTime();
        turnServo.setPosition(turn);
        upDownServo.setPower(1 * upDown);
        extend.setPower(-.60);
        extend.setTargetPosition(0);
        extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (extend.isBusy()) {
            if (retractTime.milliseconds() > level)
                upDownServo.setPower(0);
        }
    }




}
