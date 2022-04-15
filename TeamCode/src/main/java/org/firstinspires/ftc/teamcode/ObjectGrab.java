package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ObjectGrab {
    Telemetry telemetry;

    Servo turnServo;
    CRServo fingerServo;
    CRServo duck;
    CRServo leftGrab;
    CRServo rightGrab;

    DcMotor extend;
    DcMotor upDownMotor;
    DcMotor rotate;
    DcMotor inOutMotor;
    boolean fingerOut;
    boolean firstPressA;
    TouchSensor touchSensor;

    public ObjectGrab(Telemetry t, HardwareMap hardwareMap){
        telemetry   = t;

        leftGrab   = hardwareMap.get(CRServo.class, "leftGrab");
        rightGrab  = hardwareMap.get(CRServo.class, "rightGrab");
        rotate     = hardwareMap.get(DcMotor.class, "rotate");
        upDownMotor= hardwareMap.get(DcMotor.class, "upDownMotor");
        touchSensor= hardwareMap.get(TouchSensor.class, "touchSensor");
        upDownMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
