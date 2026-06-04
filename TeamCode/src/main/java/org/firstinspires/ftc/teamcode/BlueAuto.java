package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//TODO: TEST THIS!


@Autonomous(name = "BlueAuto", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class BlueAuto extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();
        stopMoving();

        waitForStart();
        //Redo with 46 inches

        telemetry.addData("distance", distanceSensor.getDistance(DistanceUnit.INCH));

        //Start powering the wheel while backing up
        shooterWheel.setVelocity(-1230);
        sleep(1500);
        //Move away from goal
        while (distanceSensor.getDistance(DistanceUnit.INCH)<45){
            moveX(0.5); //used to be moveY(-0.3);
        }



        stopMoving();

        //Shoot BALL ONE
        kickUp.setPosition(0.32);
        sleep(700);

        //Reset Position
        kickUp.setPosition(0.22);
        sleep(2000); //buffer wait time

        //Shoot BALL TWO
        thirdBallKick.setPosition(0.45);
        sleep(400);
        kickUp.setPosition(0.32);
        sleep(700);

        //Reset Position
        kickUp.setPosition(0.22);
        thirdBallKick.setPosition(0);
        sleep(600);
        intake.setPower(-0.1);
        sleep(600);

        //Shoot ball THREE
        thirdBallKick.setPosition(0.45);
        sleep(400);
        intake.setPower(0);
        sleep(2000); //buffer weight time
        kickUp.setPosition(0.32);
        sleep(700);

        //Reset shooter position
        kickUp.setPosition(0.22);
        thirdBallKick.setPosition(0);
        sleep(500);

        /// ////////////////////////old

//        //Shoot the first ball
//        kickUp.setPosition(0.32);
//        sleep(700);
//
//        //Reset kick-up position and second ball
//        kickUp.setPosition(0.22);
//        intake.setPower(-0.7); //TODO: CHECK DIRECTION
//        sleep(2000); //TODO: Include distance sensor?
//        kickUp.setPosition(0.32);
//        sleep(700);
//
//        //Reset kick-up position and third ball
//        kickUp.setPosition(0.22);
//        intake.setPower(-0.7); //TODO: CHECK DIRECTION
//        sleep(700); //TODO: Include distance sensor?
//        thirdBallKick.setPosition(0);
//        kickUp.setPosition(0.32);
//        sleep(600);
//        sleep(700);
//        thirdBallKick.setPosition(0.45);
//        sleep(400);

        //TODO: add back
        //shootBallsWithEncoder();
        /// ///////////////////////////////END////////////////////


        moveX(-0.3);
        sleep(2000);

        moveY(-0.5);
        sleep(500);


    }
}