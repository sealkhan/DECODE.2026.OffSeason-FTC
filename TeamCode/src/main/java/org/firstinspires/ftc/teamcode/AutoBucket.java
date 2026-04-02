//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//
//@Autonomous(name = "Auto Bucket", group = "LinearOpMode")
////Declares as autonomous file, SDK thing
//public class AutoBucket extends Hardware {
//    @Override
//    public void runOpMode() throws InterruptedException {
//        initHardware();
//        stopMoving();
//
////        wrist.setPosition(0.8);
////        sleep(300);
////
////        moveY(0.5);
////        sleep(600);
////
////        moveY(0);
//        moveX(-0.3);
//        sleep(700);
//
//        moveY(-0.5);
//        sleep(1500); //used to be 1500
//
//        moveX(0);
//
//        rotateBot(450, false);
//        wrist.setPosition(0.2);
//        sleep(300);
//        moveY(-0.2);
//        sleep(600);
////        moveX(0.2);
////        sleep(200);
//        verticalArm.setPower(0.7);
//        sleep(3500);
//
//
//        bucketWrist.setPosition(FLIP_POSITION);
//        sleep(1500);
//
//        verticalArm.setPower(-0.7);
//        sleep(3500);
//
//        wrist.setPosition(1);
//        sleep(300);
//
//        rotateBot(450, true);
//
//        moveY(0.5);
//        sleep(600);
//
//        moveX(0.3);
//        sleep(1500);
//        moveX(-0.5);
//        sleep(3020);
//        verticalArm.setPower(0);
//
//        wrist.setPosition(0.2);
//        sleep(300);
//
//        verticalArm.setPower(0.7);
//        sleep(2500);
//
//        rotateBot(1700, true);
//        moveY(-.5);
//        sleep(650);
//        moveY(- .3);
//        sleep(300);
//
//
//
//        moveY(0.3);
//        sleep(400);
//
//        verticalArm.setPower(0.5);
//        sleep(2300);
//
//        verticalArm.setPower(0);
//
//
//
//    }
//}