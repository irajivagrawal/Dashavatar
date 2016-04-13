package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Administrator on 3/19/2016.
 */
public class MoveMotorSystemTest extends OpMode{
    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void init() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
    }

    @Override
    public void start()
    {
        if (motorRight != null) {
            motorRight.setPower(1.0);
            try {
                Thread.sleep(1000);
                motorRight.setPower(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            motorRight.setPower(-1.0);
            try {
                Thread.sleep(1000);
                motorRight.setPower(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            telemetry.addData("motorRight", "Right motor not found");
        }
        //motorRight.setPower(0);

        while( ! gamepad1.a ) {
            ;
        }
        if (motorLeft != null) {
            motorLeft.setPower(1.0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            motorLeft.setPower(-1.0);
        } else {
            telemetry.addData("motorLeft", "Left motor not found");
        }
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop()
    {
    }
}
