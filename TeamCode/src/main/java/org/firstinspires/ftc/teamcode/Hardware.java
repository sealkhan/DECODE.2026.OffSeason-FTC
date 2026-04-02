package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.robotcore.util.ElapsedTime;


public abstract class Hardware extends LinearOpMode {
    BHI260IMU imu;
    DcMotorEx backLeft;
    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;
    DcMotorEx shooterWheel;
    DcMotorEx leftAscend;
    DcMotorEx rightAscend;
    DcMotorEx intake;
    Servo shooterDoor;
    Servo kickUp;
    Servo thirdBallKick;
    public DistanceSensor distanceSensor;

    public void initHardware() {
        // Initialize motors
        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get("frontLeft");
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get("frontRight");
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get("backLeft");
        backRight = (DcMotorEx) hardwareMap.dcMotor.get("backRight");
        shooterWheel = (DcMotorEx) hardwareMap.dcMotor.get("shooterWheel");
        intake = (DcMotorEx) hardwareMap.dcMotor.get("intake");
        shooterDoor = hardwareMap.get(Servo.class, "shooterDoor");
        kickUp = hardwareMap.get(Servo.class, "kickUp");
        thirdBallKick = hardwareMap.get(Servo.class, "thirdBallKick");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "rangeFront");

        leftAscend = (DcMotorEx) hardwareMap.dcMotor.get("leftAscend");
        rightAscend = (DcMotorEx) hardwareMap.dcMotor.get("rightAscend");

        // Initialize BHI260AP sensor
        imu = hardwareMap.get(BHI260IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP)); //Should be UP

        imu.initialize(parameters);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        shooterWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
    }

    public static void initArmFollowerMotor(@NonNull DcMotorEx motor) {
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.0);
        motor.setTargetPosition(0);
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }

    public static void initArmMotor(@NonNull DcMotorEx motor) {
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.0);
        motor.setTargetPosition(0);
        motor.setTargetPositionTolerance(10);

        PIDFCoefficients pid = new PIDFCoefficients(35.0, 0.005, 0.002, 0.0, MotorControlAlgorithm.LegacyPID);
//        pid.p = 15.0;
//        pid.i = 0.005;/
//        pid.d = 0.002;

        // somewhat working...
        //        pid.p = 15.0;
        //        pid.i = 0.005;
        //        pid.d = 0.002;

        // original/default PID setting
        //        pid.p = 10.0;
        //        pid.i = 0.005;
        //        pid.d = 0.0;
        motor.setPIDFCoefficients(RunMode.RUN_TO_POSITION, pid);
        RobotLog.v("Arm PID set to " + pid);
    }

//    public void moveWrist(double degree) {
//        int position = (int) Math.round(degree / 360.0 * Hardware.ARM_ENCODER_COUNT_PER_ROTATION);
//        wrist.setPower(0.1);
//        wrist.setTargetPosition(position);
//        wrist.setMode(RunMode.RUN_TO_POSITION);
//    }
//

//    public void moveClaw(double position) {
//        claw.setPosition(position);
//    }
//
//    public void moveLauncher(double position){
//        launcher.setPosition(position);
//        telemetry.addData("launcher to position", position);
//    }

    protected void moveArmMotor(DcMotorEx motor, int position, boolean slowSpeed) {
        motor.setPower(1.0);
        motor.setTargetPosition(position);
        motor.setMode(RunMode.RUN_TO_POSITION);
    }

//    public void moveArm(double degree, boolean slowSpeed) {
//        RobotLog.v("Arm moving to position " + degree);
//        int position = (int) Math.round(degree / 360.0 * Hardware.ARM_ENCODER_COUNT_PER_ROTATION);
//        moveArmMotor(armRight, position, slowSpeed);
//    }



    // stop moving
    public void stopMoving() {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
    }

    // We can have the go backwards be negative power
    // forward -> use positive power
    // backward -> use negative power
    public void moveY(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);
    }

    // its the same thing as moveY. Positive power is going to be right and negative is left
    // right -> positive power
    // left -> negative power
    public void moveX(double power) {
        frontRight.setPower(-power);
        frontLeft.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(-power);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static double powerCurve(double power, boolean slowSpeed) {
        if (slowSpeed)
            return power / 8.0; // In slow speed move with 1/8 of the max power <- to make it slower, make denominator bigger/xfwri
        else
            return power / 3.0; // regular power
    }
    public static double armPowerCurve(double power) {
        final double clam = 2.8;
        double value = Math.pow(Math.abs(power), clam) * Math.signum(power);
        value = Math.min(value, 0.95); // max lifting power 0.95
        value = Math.max(value, -0.35); // limit down power to not more then 0.35 to avoid slamming the arm
        RobotLog.v("value: " + value);
        return value;
    }

//    public void addMotorTelemetry() {
////        telemetry.addData(name + ".isBusy", busy);
////        telemetry.addData(name + ".position", currentPosition);
////        telemetry.addData(name + ".getTargetPosition", targetPosition);
//        String text =
//                " position:" + armLeft.getCurrentPosition()
////                + " target:" + armRight.getTargetPosition()
//                        + " amp:" + armRight.getCurrent(CurrentUnit.AMPS) + ":" + armLeft.getCurrent(CurrentUnit.AMPS);
//        //RobotLog.v(text);
//        telemetry.addData("arm", text);
//    }
//

    public void initArmMotorSimple(DcMotorEx motor) {
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0.0);
        motor.setTargetPosition(0);
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }

    public void goPark(){


        //move back
        moveY(.2);
        sleep(500);

        //move to side of field with parking
        moveX(-0.5);
        sleep(4500);

        //park
        moveY(.3);
        sleep(2000);

//        moveY(-0.2);
//        sleep(300);
    }

    public void rotateBot(int sleep, boolean clockwise){
        int clockwiseNumber;
        if (clockwise == true){
            clockwiseNumber = 1;
        } else {
            clockwiseNumber = -1;
        }
        frontLeft.setPower(0.5 * clockwiseNumber);
        backLeft.setPower(0.5 * clockwiseNumber);
        frontRight.setPower(-0.5 * clockwiseNumber);
        backRight.setPower(-0.5 * clockwiseNumber);
        sleep(sleep);

        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);

    }

    public double getHeading() {
        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        double headingRadians = AngleUnit.RADIANS.normalize(angles.getYaw(AngleUnit.RADIANS));
        double headingDegrees = Math.toDegrees(headingRadians);
        telemetry.addData("Heading (Degrees)", headingDegrees);
        telemetry.update();
        return headingRadians;
    }

    public void rotateToAngle(double targetDegrees) {
        double targetHeading = Math.toRadians(targetDegrees);  // Convert target angle to radians
        double currentHeading = getHeading();  // Get current heading in radians

        // Calculate the new target heading relative to the current heading
        double newTargetHeading = currentHeading + targetHeading;

        // Ensure the target heading is within the -π to π range
        newTargetHeading = (newTargetHeading + Math.PI) % (2 * Math.PI) - Math.PI;

        // Calculate the error to be corrected
        double error = angleDifference(newTargetHeading, currentHeading);

        // Rotate until the robot reaches the target heading within a tolerance
        while (opModeIsActive() && Math.abs(error) > 0.017) { // ~1 degree in radians
            double power = 0.3 * Math.signum(error);  // Set motor power based on error direction
            frontLeft.setPower(power);
            backLeft.setPower(power);
            frontRight.setPower(-power);
            backRight.setPower(-power);

            currentHeading = getHeading();  // Get the updated heading
            error = angleDifference(newTargetHeading, currentHeading);  // Update the error
        }

        stopMotors();  // Stop the motors once the target is reached
    }

    public double angleDifference(double target, double current) {
        double diff = target - current;
        while (diff > Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;
        return diff;
    }

    public void stopMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public boolean shooting = false;
    public boolean shootingLast = false;
    public ElapsedTime shootTimer = new ElapsedTime();

    public void shootOneBall() {
        shooting = true;
        shootTimer.reset();
    }

    public void shootOneBallLast() {
        shootingLast = true;
        shootTimer.reset();
    }

    public void shootBallsWithEncoder(){
        //Shoot one ball
        shooterWheel.setVelocity(-1230);
        kickUp.setPosition(0.32);
        sleep(500);

        //Reset kick-up position and second ball
        kickUp.setPosition(0.22);
        intake.setPower(1.5); //TODO: CHECK DIRECTION
        sleep(2000); //TODO: Include distance sensor?
        kickUp.setPosition(0.32);
        sleep(100);

        //Reset kick-up position and third ball
        kickUp.setPosition(0.22);
        thirdBallKick.setPosition(0.3);
        intake.setPower(1.5); //TODO: CHECK DIRECTION
        sleep(300); //TODO: Include distance sensor?
        thirdBallKick.setPosition(0);
        sleep(400);
        thirdBallKick.setPosition(0.3);
        sleep(400);
        kickUp.setPosition(0.32);
        sleep(500);
    }


    public void shootOneBallWithEncoderBlueInside(){
        shooterWheel.setVelocity(-1350);
        shooterDoor.setPosition(0);
        sleep(3500); //3500
        shooterDoor.setPosition(0.7);
        sleep(130);
        shooterDoor.setPosition(0);
        sleep(500);


        shooterWheel.setVelocity(-1350);
        shooterDoor.setPosition(0);
        sleep(3500); //3500
        shooterDoor.setPosition(0.7);
        sleep(150);
        shooterDoor.setPosition(0);
        sleep(500);
    }

    public void shootOneBallWithEncoderRedOutside(){
        shooterWheel.setVelocity(-1310);
        shooterDoor.setPosition(0);
        sleep(3500); //3500
        shooterDoor.setPosition(0.7);
        sleep(130);
        shooterDoor.setPosition(0);
        sleep(500);

        //Second set of balls
        shooterWheel.setVelocity(-1310);
        shooterDoor.setPosition(0);
        sleep(3500); //3500
        shooterDoor.setPosition(0.7);
        sleep(150);
        shooterDoor.setPosition(0);
        sleep(500);
    }


    public void changeMotifWithEncoder(){
        shooterWheel.setVelocity(-800); //800
        shooterDoor.setPosition(0);
        sleep(3500);
        shooterDoor.setPosition(0.7);
        sleep(250);
        shooterDoor.setPosition(0);
        sleep(1000);
    }

    public void shootOneBallWithEncoderLastBlue(){
        shooterWheel.setVelocity(-1360);
        shooterDoor.setPosition(0);
        sleep(3500);
        shooterDoor.setPosition(0.7);
        sleep(500);
        shooterDoor.setPosition(0);
        sleep(1000);
    }

    public void shootOneBallWithEncoderLastRed(){
        shooterWheel.setVelocity(-1300);
        shooterDoor.setPosition(0);
        sleep(3500);
        shooterDoor.setPosition(0.7);
        sleep(500);
        shooterDoor.setPosition(0);
        sleep(1000);
    }

}
