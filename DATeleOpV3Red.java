/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Set;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class DATeleOpV3Red extends OpMode {

	final static double TRIGGER_MIN_RANGE  = 0.20;
	final static double TRIGGER_MAX_RANGE  = 0.90;

	final static double SWIVEL_MIN_RANGE  = 0.20;
	final static double SWIVEL_MAX_RANGE  = 0.90;
	final static double DROP_MIN_RANGE  = 0.20;
	final static double DROP_MAX_RANGE  = 0.90;

    final static double HOOK_MIN_RANGE  = 0.20;
    final static double HOOK_MAX_RANGE  = 0.90;

    private double SwivelPos;
    private double DropPos;
    private double TriggerPos;
    private double HookPos;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorLift;
    Servo Trigger;
    Servo Swivel;
    Servo Drop;
    Servo Hook;

    double DROP_UP = 0.9;
    double DROP_DOWN = 0.2;

	/**
	 * Constructor
	 */
	public DATeleOpV3Red() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {

        //Initialise the wheel motors
		motorRight = hardwareMap.dcMotor.get("motorRight");
		motorLeft = hardwareMap.dcMotor.get("motorLeft");
        //Set the direction of the Left motors to be reversed
		motorRight.setDirection(DcMotor.Direction.REVERSE);
        //Initialise the lift motor
        motorLift = hardwareMap.dcMotor.get("motorLift");

        //Initialise all servos
        Trigger = hardwareMap.servo.get("Trigger");
        Swivel = hardwareMap.servo.get("Swivel");
        Drop = hardwareMap.servo.get("Drop");
        Hook = hardwareMap.servo.get("Hook");

        SwivelPos = 0.5;

        //TriggerPos = Trigger.getPosition();
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

    /*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        //Gets the input value of the joystick in the y plane of gamepad1 to move forward and backward
		float throttle = -gamepad1.left_stick_y;
        //Gets the input value of the joystick in the x plane of gamepad1 to move left and right
		float direction = gamepad1.left_stick_x;
        //Initialises the power values of the left and right wheel motors
		float rightPower = throttle - direction;
		float leftPower = throttle + direction;

		rightPower = Range.clip(rightPower, -1, 1);
		leftPower = Range.clip(leftPower, -1, 1);

		rightPower = (float)scaleInput(rightPower);
		leftPower =  (float)scaleInput(leftPower);

		motorRight.setPower(rightPower);
		motorLeft.setPower(leftPower);

        //Gets the input value for the lift motor
        float liftPower = -gamepad2.left_stick_y;

        liftPower = Range.clip(liftPower, -1, 1);

        liftPower = (float)scaleInput(liftPower);

        motorLift.setPower(liftPower);

        // GAMEPAD 2 CONTROLS
        if (gamepad2.right_bumper) {
            TriggerPos += 0.1;//Moves the triggers
        }
        //Moves the hook in
        if (gamepad2.right_trigger > 0.0)
        {
            TriggerPos -= 0.1;

        }

        Trigger.setPosition(TriggerPos);
        sleep();

        //clips the range of the servo so that it does not exceed its range values
        TriggerPos = Range.clip(TriggerPos, TRIGGER_MIN_RANGE, TRIGGER_MAX_RANGE);
        Trigger.setPosition(TriggerPos);
        sleep();

        if (gamepad2.x) {
            SwivelPos += 0.1;//Moves the drop inside the bot
        }

        if (gamepad2.b) {
            //move the swivel slowly
            SwivelPos -= 0.1;//Moves the drop inside the bot
        }

        SwivelPos = Range.clip(SwivelPos, SWIVEL_MIN_RANGE, SWIVEL_MAX_RANGE);
        Swivel.setPosition(SwivelPos);
        sleep();

        if (gamepad2.left_bumper){
            HookPos += 0.1;
        }

        if (gamepad2.left_trigger > 0.0){
            HookPos -= 0.1;
        }

        SetServoPosition(Hook, HookPos, HOOK_MIN_RANGE, HOOK_MAX_RANGE);
        sleep();

        if (gamepad2.y) {
            DropPos = DROP_UP;//Moves the drop upwards
        }

        if (gamepad2.a) {
            DropPos = DROP_DOWN;//Moves the drop downwards
        }

        //clips the range of the servo so that it does not exceed its range values
        DropPos = Range.clip(DropPos, DROP_MIN_RANGE, DROP_MAX_RANGE);

        Drop.setPosition(DropPos);
        sleep();

        //Send telemetry data back to driver station.
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Trigger", TriggerPos);
        telemetry.addData("Swivel", SwivelPos);
        telemetry.addData("Drop", DropPos);
        telemetry.addData("Hook", "Hook:  " + String.format("%.2f", HookPos));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftPower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightPower));
        telemetry.addData("lift tgt pwr", "lift pwr: " + String.format("%.2f", liftPower));

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
