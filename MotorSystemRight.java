package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Administrator on 2/27/2016.
 */
public class MotorSystemRight extends OpMode {

    DcMotor motorRight;

    @Override
    public void init()
    {
        motorRight = hardwareMap.dcMotor.get("motorRight");
    }

    public void start()
    {
        motorRight.setPower(1.0);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorRight.setPower(0.0);
    }

    @Override
    public void loop() {

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
