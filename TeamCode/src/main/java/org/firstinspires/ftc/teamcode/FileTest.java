package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ReadWriteFile;


import org.firstinspires.ftc.onbotjava.handlers.file.DeleteFile;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File; // File test
import java.io.IOException;
import java.io.Writer;

@Autonomous(name = "FileTest", group =  "Auto")
public class FileTest extends LinearOpMode {

    // Creating Instances of Each Subsystem of the Robot



    public char first;
    public char second;

    native float twoCharToFloat(char first, char second);
    native void floatToTwoChar(char first, char second);
    static {
        System.loadLibrary("ftcrobotcontroller");
    }

    @Override
    public void runOpMode() throws InterruptedException{
        waitForStart();

/*
        char[] fileExport = new char[4];

        fileExport[3] = 0;
        */

        /*
        float[] t = new float[4];

        t[0] = 1.0f;
        t[1] = 4.0f;
        t[2] = -2.1f;
        t[3] = 0;


        File fileTest = AppUtil.getInstance().getSettingsFile("test.txt");
        ReadWriteFile.writeFile(fileTest, String.copyValueOf(FloatArrayToChar(t)));

        float[] t2 = CharArrayToFloat(ReadWriteFile.readFile(fileTest).toCharArray());
        telemetry.addData("0", t2[0]);
        telemetry.addData("0", t2[1]);
        telemetry.addData("0", t2[2]);
        telemetry.addData("0", t2[3]);

         */

        telemetry.update();

        while(opModeIsActive()) {}

    }
}