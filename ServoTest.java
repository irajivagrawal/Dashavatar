package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * Created by Administrator on 2/27/2016.
 */
public class ServoTest extends OpMode {

    //Servo servoTriggerLeft;
    Servo servoTrigger;

    private double currentPos;
    //private double currentPosRight;

    @Override
    public void init()
    {
        //servoTriggerRight = hardwareMap.servo.get("servo1");
        servoTrigger = hardwareMap.servo.get("servo1");

    }

    public void start()
    {

        currentPos = servoTrigger.getPosition();
        //currentPosRight = servoTriggerRight.getPosition();

        //telemetry.addData("Text", "*** Robot Data***");

        //telemetry.addData("servo", "servoleft: " + String.format("%.2f", currentPosLeft));
        //telemetry.addData("servo", "servoRight: " + String.format("%.2f", currentPosRight));


        servoTrigger.setPosition(0.5);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        servoTrigger.setPosition(0.75);

        currentPos = servoTrigger.getPosition();
    }


    @Override
    public void loop() {
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("servo", "servo: " + String.format("%.2f", currentPos));
        //telemetry.addData("servo", "servoRight: " + String.format("%.2f", currentPosRight));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }
}
