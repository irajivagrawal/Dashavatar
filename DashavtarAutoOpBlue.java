package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Administrator on 3/13/2016.
 */
public class DashavtarAutoOpBlue extends LinearOpMode {
    DcMotor motorRight;
    DcMotor motorLeft;
    Servo Swivel;
    Servo Drop;

    double SwivelPos = 0.9;
    double DropPos = 0.9;

    double DROP_UP = 0.5;
    double DROP_DOWN = 0.2;
    double SWIVEL_OUT = 0.2;
    double SWIVEL_IN = 0.9;

    public void runOpMode() throws InterruptedException
    {
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        Swivel = hardwareMap.servo.get("Swivel");
        Drop = hardwareMap.servo.get("Drop");


        Swivel.setPosition(SWIVEL_IN);
        Drop.setPosition(DROP_UP);

        waitForStart();

        MoveChassis(110, 1.0f);//Move the chassis approx. 60 inches

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        MoveRight();

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        MoveChassis(35, 1.0f);

        Swivel.setPosition(0.7);
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        waitOneFullHardwareCycle();

    }

    private  void stopAllMotors()
    {
        //Stop all motors
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

    private void MoveChassis (float distance, float power)//Distance in inches
    {
        double WheelRadius = 5;
        double noOfRotations = distance/(3.14159265 * WheelRadius);

        //assume RPM to be 80
        int RPM = 80;
        long noOfmiliSeconds = (long)((noOfRotations / RPM)* 60 * 1000);  //in miliseconds

        motorRight.setPower(power);
        motorLeft.setPower(power);

        try {
            sleep(noOfmiliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopAllMotors();
    }

    private void MoveRight ()
    {
        motorRight.setPower(-1.0);
        motorLeft.setPower(1.0);

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
             e.printStackTrace();
        }

        stopAllMotors();
    }

    private void MoveLeft ()
    {
        motorRight.setPower(1.0);
        motorLeft.setPower(-1.0);

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        stopAllMotors();
    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }


        // return scaled value.
        return dScale;
    }
}
