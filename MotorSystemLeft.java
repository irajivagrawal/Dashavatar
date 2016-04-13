package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Administrator on 3/5/2016.
 */
public class MotorSystemLeft extends OpMode {

    DcMotor motorLeft;

    @Override
    public void init()
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
    }

    public void start()
    {
        motorLeft.setPower(1.0);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorLeft.setPower(0.0);
    }

    @Override
    public void loop() {

    }
}
