package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "Auto Specimen", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoSpecimen extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        boolean touchPosition = false;
        boolean hangingPosition = false;

        initHardware();
        stopMoving();

        //close claw
        claw.setPosition(0.63);
        sleep(300);

        //move towards submersible

        //TODO: Change back. I just removed for testing in my room
//       while (distanceSensor.getDistance(DistanceUnit.INCH) != 0) {
//           moveY(-.3);
//        }


        moveY(0);
        sleep(100);


        //prop wrist up to be able to bring up bucket
        wrist.setPosition(0.8);
        sleep(300);

        //move backwards to be able to put claw around poles
        //TODO: ADD BACK
//        while (distanceSensor.getDistance(DistanceUnit.INCH) < 3.3){
//            moveY(0.2);
//        }
        //stop
        moveY(0);
        sleep(100);


        //open vertical arm
        verticalArm.setPower(0.5);
        sleep(3500);

       //move towards submersible
        //TODO: ADD BACK
//        while(distanceSensor.getDistance(DistanceUnit.INCH) > 3.5) {
//            moveY(-0.2);
//        }
//


        wrist.setPosition(1);

        moveY(0);
        sleep(100);


        //TODO: Figure out split?
        //bring vertical arm down
        verticalArm.setPower(-0.6);
        sleep(1200); //used to be 1700


        claw.setPosition(0.5);
        sleep(500);

        moveY(0.3);
        sleep(600);

        verticalArm.setPower(-0.6);
        sleep(50);

        claw.setPosition(0.65);
        sleep(450);

        goPark();

    }
}