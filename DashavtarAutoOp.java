package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Administrator on 3/13/2016.
 */
public class DashavtarAutoOp extends OpMode {
    DcMotor motorRight;
    DcMotor motorLeft;
    Servo servoSwivel;
    Servo servoDrop;

    long timeToMoveForward = 2000; // to be computed based on the distance to travel

    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        servoSwivel.setPosition(0.5);
    }

    private  void stopAllMotors()
    {
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }

    @Override
    public void start()
    {
        // Move the robot forward

        motorRight.setPower(1.0);
        motorLeft.setPower(1.0);

        try {
            Thread.sleep(timeToMoveForward);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopAllMotors();

        // Move the auto drop

        servoSwivel.setPosition(0.9);
        telemetry.addData("swivel pos", "Servo swivel pos: " + String.format("%.2f", 0.9));

        double servoDropPos = servoDrop.getPosition();
        telemetry.addData("drop pos", "Servo drop pos: " + Double.toString(servoDropPos));

        servoDropPos = servoDropPos - 0.25;
        servoDrop.setPosition(servoDropPos);
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        double servoSwivelPosition = servoSwivel.getPosition();
        double servoDropPos = servoDrop.getPosition();

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("swivel pos", "Servo swivel pos: " + String.format("%.2f", servoSwivelPosition));
        telemetry.addData("drop pos", "Servo drop pos: " + Double.toString(servoDropPos));
   }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

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
