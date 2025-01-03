package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp(name = "Field Oriented Driving")
public class FieldOrientedDriving extends Hardware {
    public static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 8.0; // In slow speed move with 1/8 of the max power <- to make it slower, make denominator bigger/xfwri
        else
            return power / 3.0; // regular power
    }

//    public void initArmMotorSimple(DcMotorEx motor) {
//        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor.setPower(0.0);
//        motor.setTargetPosition(0);
//        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }

    @Override
    public void runOpMode() {
        initHardware();

        //initArmMotorSimple(armRight); // try directly controlling the arm motors with simple power
        //initArmMotorSimple(armLeft); // try directly controlling the arm motors with simple power

        boolean previousX = false;
        boolean antigravity = false;
        boolean emergencyDrive = false; // activates arm motor control & gravity compensation
        while (opModeIsActive()) {
            boolean slowSpeed = gamepad1.left_bumper; // when pressed, slow down robot
            // X presses
            boolean x = gamepad1.x;
            if (!x & previousX) {
                emergencyDrive = !emergencyDrive; // toggle emergency drive
            }
            previousX = x;

            //EMERGENCY DRIVE AND FIELD ORIENTED
            if (emergencyDrive){
                // Y stick is reversed

                float y = -gamepad1.right_stick_y;
                telemetry.addData("y: ", y);
                float X = gamepad1.right_stick_x;
                telemetry.addData("x: ", X);
                float rx = gamepad1.left_stick_x;
                telemetry.addData("rx: ", rx);
                RobotLog.v(String.valueOf(y));
                RobotLog.v(String.valueOf(X));
                RobotLog.v(String.valueOf(rx));

                // there's a divide by two in these in order to make the power smaller
                // if you want to make the power smaller, then make the '2' higher
                // if you want it stronger make it lower or remove the division by two
                double dividedBy = 2;
                if (gamepad1.right_bumper){
                    dividedBy = 0.5;
                } else if (gamepad1.left_bumper){
                    dividedBy = 4;
                }

                frontLeft.setPower((y + X + rx) / dividedBy);
                telemetry.addData("front left: ", (y + X + rx) / dividedBy);

                backLeft.setPower(((y - X) + rx) / dividedBy);
                telemetry.addData("back left: ", ((y - X) + rx) / dividedBy);

                frontRight.setPower(((y - X) - rx) / dividedBy);
                telemetry.addData("front right: ", ((y - X) - rx) / dividedBy);

                backRight.setPower(((y + X) - rx) / dividedBy);
                telemetry.addData("back right: ", (y - X - rx) / dividedBy);

            } else {

                double rightStickY = powerCurve(-gamepad1.right_stick_y, slowSpeed); // input range is -1..+1
                double rightStickX = powerCurve(gamepad1.right_stick_x, slowSpeed); // ditto
                double leftStickX = powerCurve(gamepad1.left_stick_x, slowSpeed); // ditto
                telemetry.addData("left stick x: ", gamepad1.left_stick_x);
                telemetry.addData("thought left stick x: ", leftStickX);

                // Read sensor data
                YawPitchRollAngles robotOrientation = imu.getRobotYawPitchRollAngles();
                double yaw = robotOrientation.getYaw(AngleUnit.RADIANS);

                double rotX = rightStickX * Math.cos(-yaw) - rightStickY * Math.sin(-yaw);
                double rotY = rightStickX * Math.sin(-yaw) + rightStickY * Math.cos(-yaw);

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio,
                // but only if at least one is out of the range [-1, 1]

                double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(leftStickX), 1);
                if (gamepad1.right_bumper) {
                    denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(leftStickX), 1) / 2;
                }


                double frontLeftPower = (rotY + rotX + leftStickX) / denominator;
                double backLeftPower = (rotY - rotX + leftStickX) / denominator;
                double frontRightPower = (rotY - rotX - leftStickX) / denominator;
                double backRightPower = (rotY + rotX - leftStickX) / denominator;

                frontLeft.setPower(frontLeftPower);
                telemetry.addData("frontLeftPower: ", frontLeftPower);

                backLeft.setPower(backLeftPower);
                telemetry.addData("backLeftPower: ", backLeftPower);

                frontRight.setPower(frontRightPower);
                telemetry.addData("frontRightPower: ", frontRightPower);

                backRight.setPower(backRightPower);
                telemetry.addData("backRightPower: ", backRightPower);

                telemetry.addData("yaw", yaw);
                //addMotorTelemetry();
            }

            // adds to the telemetry which mode the robot is in
            if (emergencyDrive) {
                telemetry.addData("mode", "Emergency Driving");
            } else {
                telemetry.addData("mode", "Field Oriented Driving");
            }


            //Wrist
            if (gamepad2.dpad_up){
                wrist.setPosition(0.9); //Switched from 1, up position
                telemetry.addData("wrist to position", wrist.getPosition());
            } else if(gamepad2.dpad_down){
                wrist.setPosition(0);
                telemetry.addData("wrist to position", wrist.getPosition());
            }




            //TODO: make slow speed for arm with right bumper
            double position = (gamepad2.right_stick_y + 1) / 2;  // maps -1 to 1 to 0 to 1
            spintake.setPosition(position);
            telemetry.addData("spintake to position", spintake.getPosition());



            // Set motor power based on conditions
            if (gamepad2.left_stick_y < -0.1) {
                horizontalArm.setPower(ARM_FORWARD_POWER);
                telemetry.addData("horizontalArm ", ARM_FORWARD_POWER);
            } else if (gamepad2.left_stick_y > 0.1) {
                horizontalArm.setPower(ARM_BACKWARD_POWER);
                telemetry.addData("horizontalArm ", ARM_BACKWARD_POWER);
            } else {
                horizontalArm.setPower(0); //Stop the arm if the joystick is in the neutral position
                telemetry.addData("horizontalArm ", "0");
            }

            // Hang mechanism
            if (gamepad1.y){
                leftHang.setPower(HANG_POWER);
                rightHang.setPower(HANG_POWER);
                telemetry.addData("Hang:", " deploying");
            } else if (gamepad1.a){
                leftHang.setPower(-HANG_POWER);
                rightHang.setPower(-HANG_POWER);
                telemetry.addData("Hang:", " retracting");
            } else {
                leftHang.setPower(0);
                rightHang.setPower(0);
                telemetry.addData("Hang:", " null");
            }

            telemetry.update();
        }
    }
}