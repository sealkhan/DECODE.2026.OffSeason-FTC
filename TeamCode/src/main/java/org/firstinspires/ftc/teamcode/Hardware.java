package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.RobotLog;

public abstract class Hardware extends LinearOpMode {
    BHI260IMU imu;
    DcMotorEx backLeft;
    DcMotorEx frontLeft;
    DcMotorEx frontRight;
    DcMotorEx backRight;
    DcMotorEx horizontalArm;

    //DcMotorEx armLeft;
//    DcMotorEx armRight;
//    DcMotorEx wrist;
//    Servo claw;
//    Servo launcher;
    final static double ARM_ENCODER_COUNT_PER_ROTATION = 288.0;
    final static double ARM_NORMAL_SPEED = 10.0; // degrees per second
    final static double ARM_SLOW_SPEED = 3.0; // degrees per second


    public void initHardware() {
        // Initialize motors
        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get("frontLeft");
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get("frontRight");
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get("backLeft");
        backRight = (DcMotorEx) hardwareMap.dcMotor.get("backRight");
        horizontalArm = (DcMotorEx) hardwareMap.dcMotor.get("horizontalArm");
//        armRight = (DcMotorEx) hardwareMap.dcMotor.get("ArmRight");
//        armLeft = (DcMotorEx) hardwareMap.dcMotor.get("ArmLeft");
//        wrist = (DcMotorEx) hardwareMap.dcMotor.get("wrist");
//        claw = hardwareMap.servo.get("claw");
//        launcher = hardwareMap.servo.get("launcher");


        // Initialize BHI260AP sensor
        imu = hardwareMap.get(BHI260IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        imu.initialize(parameters);

        backLeft.setDirection(DcMotor.Direction.REVERSE);


//        armLeft.setDirection(DcMotor.Direction.REVERSE);
////        initArmMotor(armRight);
////        initArmFollowerMotor(armLeft);
//
//        wrist.setMode(RunMode.STOP_AND_RESET_ENCODER);
//        wrist.setPower(0.0);
//        wrist.setTargetPosition(0);
//        wrist.setMode(RunMode.RUN_TO_POSITION);
//
//        claw.setDirection(Servo.Direction.FORWARD);
//        launcher.setDirection(Servo.Direction.FORWARD);

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
}
