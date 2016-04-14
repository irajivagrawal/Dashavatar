package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Administrator on 2/27/2016.
 */
public class ServoJoystick extends OpMode
{

    Servo servoTrigger;

    final static double servoTrigger_MIN_RANGE  = 0.20;
    final static double servoTrigger_MAX_RANGE  = 0.90;
    private double currentPos;

    @Override
    public void init()
    {
        servoTrigger = hardwareMap.servo.get("Trigger");
        servoTrigger.setPosition(0.5);
        currentPos = 0.5;
    }


    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    private void SetServoPosition(Servo thingToMove, double angle, double minRange, double maxRange)
    {
        angle = Range.clip(angle, minRange, maxRange);
        thingToMove.setPosition(angle);
        sleep();
    }

    @Override
    public void loop()
    {
        if (gamepad2.x)
        {
            currentPos += 0.1;
            SetServoPosition(servoTrigger, currentPos, servoTrigger_MIN_RANGE, servoTrigger_MAX_RANGE);

        }

        if (gamepad2.b)
        {
            currentPos -= 0.1;
            SetServoPosition(servoTrigger, currentPos, servoTrigger_MIN_RANGE, servoTrigger_MAX_RANGE);
        }

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("servo", currentPos);
    }
}
