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

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class DATeleOpV2servoDelta extends OpMode {

	final static double TRIGGER_MIN_RANGE  = 0.20;
	final static double TRIGGER_MAX_RANGE  = 0.90;

	final static double SWIVEL_MIN_RANGE  = 0.20;
	final static double SWIVEL_MAX_RANGE  = 0.90;
	final static double DROP_MIN_RANGE  = 0.20;
	final static double DROP_MAX_RANGE  = 0.90;

    final static double CLAW_MIN_RANGE  = 0.20;
    final static double CLAW_MAX_RANGE  = 0.90;
    final static double ELBOW_MIN_RANGE  = 0.20;
    final static double ELBOW_MAX_RANGE  = 0.90;

    final static double HOOK_MIN_RANGE  = 0.20;
    final static double HOOK_MAX_RANGE  = 0.90;

    double SwivelPos;
    double DropPos;
    double TriggerPos;
    double ClawPos;
    double ElbowPos;
    double HookPos;

    double servoDelta = 0.1;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorLift;
    Servo Trigger;
    Servo Swivel;
    Servo Drop;
    Servo Claw;
    Servo Elbow;
    Servo Hook;

    double DROP_UP = 0.9;
    double DROP_DOWN = 0.2;
    double SWIVEL_OUT = 0.2;
    double SWIVEL_IN = 0.9;

    double TRIGGER_IN = 0.3;
    double TRIGGER_OUT = 0.5;

    double CLAW_CLOSE = 0.2;
    double CLAW_OPEN = 0.9;

    double ELBOW_RIGHT = 0.2;
    double ELBOW_LEFT = 0.9;
    double ELBOW_MID = 0.42;

    double HOOK_IN = 0.2;
    double HOOK_OUT = 0.9;

	/**
	 * Constructor
	 */
	public DATeleOpV2servoDelta() {

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
        Claw = hardwareMap.servo.get("Claw");
        Elbow = hardwareMap.servo.get("Elbow");
        Hook = hardwareMap.servo.get("Hook");

        //TriggerPos = Trigger.getPosition();
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

        float fineThrottle = -gamepad1.right_stick_y * 0.5f;
        float fineDirection = gamepad1.right_stick_x * 0.5f;
        float fineRightPower = fineThrottle + fineDirection;
        float fineLeftPower = fineThrottle - fineDirection;

		rightPower = Range.clip(rightPower, -1, 1);
		leftPower = Range.clip(leftPower, -1, 1);

        fineRightPower = Range.clip(fineRightPower, -1, 1);
        fineLeftPower = Range.clip(fineLeftPower, -1, 1);

        fineRightPower = (float)scaleInput(fineRightPower);
        fineLeftPower = (float)scaleInput(fineLeftPower);

		rightPower = (float)scaleInput(rightPower);
		leftPower =  (float)scaleInput(leftPower);

        motorRight.setPower(fineRightPower);
        motorLeft.setPower(fineLeftPower);

		motorRight.setPower(rightPower);
		motorLeft.setPower(leftPower);

        float liftPower = -gamepad2.left_stick_y;

        liftPower = Range.clip(liftPower, -1, 1);

        liftPower = (float)scaleInput(liftPower);

        motorLift.setPower(liftPower);

        // GAMEPAD 1 CONTROLS
        if (gamepad1.left_bumper) {
            TriggerPos = TRIGGER_OUT;//Moves the triggers
        }

        if (gamepad1.right_bumper) {
            TriggerPos = TRIGGER_IN;//Moves the triggers
        }

        //clips the range of the servo so that it does not exceed its range values
        TriggerPos = Range.clip(TriggerPos, TRIGGER_MIN_RANGE, TRIGGER_MAX_RANGE);

        Trigger.setPosition(TriggerPos);

        if (gamepad1.x) {
            SwivelPos += servoDelta;//Moves the drop inside the bot
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            telemetry.addData("x: ", "x button pressed");
        }

        if (gamepad1.b) {
            SwivelPos -= servoDelta;//Moves the drop outside the bot
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            telemetry.addData("b: ", "b button pressed");
        }

        SwivelPos = Range.clip(SwivelPos, SWIVEL_MIN_RANGE, SWIVEL_MAX_RANGE);
        Swivel.setPosition(SwivelPos);

        if (gamepad1.y) {
            DropPos = DROP_UP;//Moves the drop upwards
            telemetry.addData("y: ", "y button pressed");
        }

        if (gamepad1.a) {
            DropPos = DROP_DOWN;//Moves the drop downwards
            telemetry.addData("a: ", "a button pressed");
        }

        //clips the range of the servo so that it does not exceed its range values
        DropPos = Range.clip(DropPos, DROP_MIN_RANGE, DROP_MAX_RANGE);

        Drop.setPosition(DropPos);

        // GAMEPAD2 CONTROLS
        if (gamepad2.right_bumper)
        {
            ElbowPos = ELBOW_MID;//Puts the elbow in the middle
            telemetry.addData("RB: ", "right bumper pressed");
        }

        if(gamepad2.x)
        {
            ElbowPos = ELBOW_LEFT;//Moves the elbow in()
            telemetry.addData("x: ", "x button pressed");
        }

        if (gamepad2.b)
        {
            ElbowPos = ELBOW_RIGHT;//Moves the elbow out()
            telemetry.addData("b: ", "b button pressed");
        }

        if (gamepad2.y)
        {
            ClawPos = CLAW_OPEN;//Opens the claw of the basket
            telemetry.addData("y: ", "y button pressed");
        }

        if (gamepad2.a)
        {
            ClawPos = CLAW_CLOSE;//Closes the claw of the basket
            telemetry.addData("a: ", "a button pressed");
        }

        ClawPos = Range.clip(ClawPos,CLAW_MIN_RANGE, CLAW_MAX_RANGE);
        ElbowPos = Range.clip(ElbowPos, ELBOW_MIN_RANGE, ELBOW_MAX_RANGE);

        Claw.setPosition(ClawPos);
        Elbow.setPosition(ElbowPos);

        if (gamepad2.right_trigger > 0.0)
        {
            HookPos = HOOK_OUT;
        }

        if (gamepad2.left_trigger > 0.0)
        {
            HookPos = HOOK_IN;
        }

        HookPos = Range.clip(HookPos,HOOK_MIN_RANGE, HOOK_MAX_RANGE);

        Hook.setPosition(HookPos);

        //Send telemetry data back to driver station.
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Trigger", "Trigger:  " + String.format("%.2f", TriggerPos));
        telemetry.addData("Swivel", "Swivel:  " + String.format("%.2f", SwivelPos));
        telemetry.addData("Drop", "Drop:  " + String.format("%.2f", DropPos));
        telemetry.addData("Claw", "Claw:  " + String.format("%.2f", ClawPos));
        telemetry.addData("Elbow", "Elbow:  " + String.format("%.2f", ElbowPos));
        telemetry.addData("Hook", "Hook:  " + String.format("%.2f", HookPos));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftPower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightPower));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", fineLeftPower));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", fineRightPower));
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
