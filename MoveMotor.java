package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Administrator on 2/27/2016.
 */
public class MoveMotor extends OpMode {

    DcMotor motorRight;

    @Override
    public void init()
    {
        motorRight = hardwareMap.dcMotor.get("motor_2");
    }

    public void loop()
    {
        motorRight.setPower(-1.0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorRight.setPower(0.0);
    }

}
