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
public class DATeleOpV2 extends OpMode {

	final static double TRIGGERLEFT_MIN_RANGE  = 0.20;
	final static double TRIGGERLEFT_MAX_RANGE  = 0.90;
	final static double TRIGGERRIGHT_MIN_RANGE  = 0.20;
	final static double TRIGGERRIGHT_MAX_RANGE  = 0.90;

	final static double SWIVEL_MIN_RANGE  = 0.20;
	final static double SWIVEL_MAX_RANGE  = 0.90;
	final static double DROP_MIN_RANGE  = 0.20;
	final static double DROP_MAX_RANGE  = 0.90;

    double SwivelPos;

    double DropPos;

    double TriggerLeftPos;

    double TriggerRightPos;

    double servoDelta = 0.1;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorLift;
    Servo TriggerLeft;
    Servo TriggerRight;
    Servo Swivel;
    Servo Drop;

	/**
	 * Constructor
	 */
	public DATeleOpV2() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {


		motorRight = hardwareMap.dcMotor.get("motorRight");
		motorLeft = hardwareMap.dcMotor.get("motorLeft");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLift = hardwareMap.dcMotor.get("motorLift");

        TriggerLeft = hardwareMap.servo.get("TriggerLeft");
        TriggerRight = hardwareMap.servo.get("TriggerRight");
        Swivel = hardwareMap.servo.get("Swivel");
        Drop = hardwareMap.servo.get("Drop");

		// assign the starting position of all the servos
        TriggerLeftPos = 0.2;
        TriggerRightPos = 0.2;

        TriggerLeft.setPosition(TriggerLeftPos);
        TriggerRight.setPosition(TriggerRightPos);

		SwivelPos = 0.2;
        DropPos = 0.2;
		
        Swivel.setPosition(SwivelPos);
        Drop.setPosition(DropPos);
	}

	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		float throttle = -gamepad1.left_stick_y;
		float direction = gamepad1.left_stick_x;
		float right = throttle + direction;
		float left = throttle - direction;

		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		motorRight.setPower(right);
		motorLeft.setPower(left);

        float liftPower = -gamepad2.left_stick_y;

        liftPower = Range.clip(liftPower, -1, 1);

        liftPower = (float)scaleInput(liftPower);

        motorLift.setPower(liftPower);

        if(gamepad1.dpad_up)
        {
            motorRight.setPower(right * 0.5);
            motorLeft.setPower(left * 0.5);
        }

        if (gamepad1.dpad_down)
        {
            motorRight.setPower(-right * 0.5);
            motorLeft.setPower(-left * 0.5);
        }

        if (gamepad1.dpad_right)
        {
            motorLeft.setPower(left * 0.5);
            motorRight.setPower(-right * 0.5);
        }

        if (gamepad1.dpad_left)
        {
            motorLeft.setPower(-left * 0.5);
            motorRight.setPower(right * 0.5);
        }

        if (gamepad2.left_bumper) {
            TriggerLeftPos += servoDelta;
        }

        if (gamepad2.left_trigger > 0.0) {
            TriggerLeftPos -= servoDelta;
        }

        if (gamepad2.right_bumper) {
            TriggerRightPos -= servoDelta;
        }

        if (gamepad1.right_trigger > 0.0) {
            TriggerRightPos += servoDelta;
        }

        TriggerLeftPos = Range.clip(TriggerLeftPos, TRIGGERLEFT_MIN_RANGE, TRIGGERLEFT_MAX_RANGE);
        TriggerRightPos = Range.clip(TriggerRightPos, TRIGGERRIGHT_MIN_RANGE, TRIGGERRIGHT_MAX_RANGE);

        TriggerLeft.setPosition(TriggerLeftPos);
        TriggerRight.setPosition(TriggerRightPos);

        if (gamepad2.a) {
            SwivelPos += servoDelta;
			SwivelPos = Range.clip(SwivelPos, SWIVEL_MIN_RANGE, SWIVEL_MAX_RANGE);
			Swivel.setPosition(SwivelPos);
        }

        if (gamepad2.y) {
            SwivelPos -= servoDelta;
			SwivelPos = Range.clip(SwivelPos, SWIVEL_MIN_RANGE, SWIVEL_MAX_RANGE);
		    Swivel.setPosition(SwivelPos);
		}

        if (gamepad2.x) {
            DropPos += servoDelta;
			DropPos = Range.clip(DropPos, DROP_MIN_RANGE, DROP_MAX_RANGE);
			Drop.setPosition(DropPos);
        }

        if (gamepad2.b) {
            DropPos -= servoDelta;
			DropPos = Range.clip(DropPos, DROP_MIN_RANGE, DROP_MAX_RANGE);
		    Drop.setPosition(DropPos);
        }

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("TriggerLeft", "TriggerLeft:  " + String.format("%.2f", TriggerLeftPos));
        telemetry.addData("TriggerRight", "TriggerRight:  " + String.format("%.2f", TriggerRightPos));
        telemetry.addData("Swivel", "Swivel:  " + String.format("%.2f", SwivelPos));
        telemetry.addData("Drop", "Drop:  " + String.format("%.2f", DropPos));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
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
