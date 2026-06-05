package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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

        boolean previousX = false;
        boolean emergencyDrive = false; // activates arm motor control
        boolean previousY = false;
        boolean previousA = false;
        boolean doorOpen = false;
        boolean toggle1500 = false;
        boolean lastLB = false;
        boolean toggle1220 = false;
        boolean lastRB = false;


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
                    dividedBy = 6;
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
            }

            // adds to the telemetry which mode the robot is in
            if (emergencyDrive) {
                telemetry.addData("mode", "Emergency Driving");
            } else {
                telemetry.addData("mode", "Field Oriented Driving");
            }


            //Gamepad Controllers defined
            // Set motor power based on conditions
            //Define speed modifiers
            double speedModifier = 1.0; //Default Speed

            // Check bumper conditions
//            if (gamepad2.left_bumper) {
//                speedModifier = 0.5; //Slow down to 50% speed
//            } else if (gamepad2.right_bumper) {
//                speedModifier = 1.5; // Speed up to 150% speed
//            } else {
//                speedModifier = 1.0; //Default speed
//            }

            // Gamepad2 left stick y turns on SHOOTER


            //TODO: FINISH THIS CODE! --> SHOOTER
            ///////////////////////////////////////////////////////////////////////////////////////
            // Toggle for 1500 (Left Bumper)
            if (gamepad2.left_bumper && !lastLB) {
                toggle1500 = !toggle1500;
                if (toggle1500) toggle1220 = false; // Turn off the other speed
            }
            lastLB = gamepad2.left_bumper;

            // Toggle for 1220 (Right Bumper)
            if (gamepad2.right_bumper && !lastRB) {
                toggle1220 = !toggle1220;
                if (toggle1220) toggle1500 = false; // Turn off the other speed
            }
            lastRB = gamepad2.right_bumper;

            // Velocity Logic
            if (gamepad2.right_stick_y < -0.5 || toggle1500) {
                shooterWheel.setVelocity(-1500);
            } else if (gamepad2.right_stick_y > 0.5 || toggle1220) {
                shooterWheel.setVelocity(-1300);
            } else {
                shooterWheel.setVelocity(0);
            }

            //////////////////////////////////////////////////////////////////////////////////////
//            if(gamepad2.right_stick_y < -0.5){
//                shooterWheel.setVelocity(-1200);
//            } else {
//                shooterWheel.setVelocity(0);
//            }


            // Gamepad2 right stick y runs intake
            intake.setPower(-gamepad2.left_stick_y); //-gamepad2.left_stick_y/3

            // Gamepad2 y moves the kick-up
            if(gamepad2.y){
                kickUp.setPosition(0.32);
            } else {
                kickUp.setPosition(0.22);
            }

            // Gamepad x kicks in the third ball
            if(gamepad2.x){
                thirdBallKick.setPosition(0.5);
            } else {
                thirdBallKick.setPosition(0.9);
            }

            // Gamepad a opens the door
            if (gamepad2.a && !previousA) {
                doorOpen = !doorOpen; // Switch the state
            }
            previousA = gamepad2.a; // Update previous state
            // Set position based on the toggle
            if (doorOpen) {
                shooterDoor.setPosition(0.58);
            } else {
                shooterDoor.setPosition(0.7);
            }

            //Ascension logic
            if(gamepad1.y){
                leftAscend.setPower(-1.0);
                rightAscend.setPower(-1.0);
                telemetry.addLine("Ascending!");
            } else if (gamepad1.b){
                leftAscend.setPower(1.0);
                rightAscend.setPower(1.0);
                telemetry.addLine("Descending! WATCH OUT!");
            } else if (gamepad1.a || gamepad1.y && gamepad1.left_bumper){
                leftAscend.setPower(0.3);
                rightAscend.setPower(0.3);
                telemetry.addLine("Descending SLOWLY! WATCH OUT!");
            } else {
                leftAscend.setPower(0);
                rightAscend.setPower(0);
                telemetry.addLine("Not ascending or descending...");
            }

            telemetry.addData("wheel ticks", shooterWheel.getVelocity());

        }
    }
}
