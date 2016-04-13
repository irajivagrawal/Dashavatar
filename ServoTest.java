package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Administrator on 3/5/2016.
 */
public class ServoTest extends OpMode {

    Servo servoTriggerLeft;
    Servo servoTriggerRight;

    @Override
    public void init()
    {
        servoTriggerLeft = hardwareMap.servo.get("servo1");
        servoTriggerRight = hardwareMap.servo.get("servo2");
    }

    public void start()
    {
        servoTriggerLeft.setPosition(0.5);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        servoTriggerLeft.setPosition(0.75);
    }

    @Override
    public void loop()
    {
        double posServoLeft = servoTriggerLeft.getPosition();
        double posServoRight = servoTriggerRight.getPosition();

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left servo pos", "Left servo pos:  " + Double.toString(posServoLeft));
        telemetry.addData("right servo pos",  "Right servo pos: " + Double.toString(posServoRight));
    }
}

