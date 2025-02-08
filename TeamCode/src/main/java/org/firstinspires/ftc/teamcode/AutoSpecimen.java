package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Specimen", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoSpecimen extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();



        //close claw
        claw.setPosition(0.7);
        sleep(300);

        //move towards submersible (readjust with submersible)
        //TODO: ADD TOUCH SENSORS
        moveY(-.3);
        sleep(2000);

        //prop wrist up to be able to bring up bucket
        wrist.setPosition(0.8);
        sleep(300);

        //move backwards to be able to put claw around poles
        //TODO: ADD DISTANCE SENSOR HERE
        moveY(0.2);
        sleep(500);

        //stop
        moveY(0);
        sleep(100);

        //open vertical arm
        verticalArm.setPower(-0.5);
        sleep(2800);

        //move forward
        //TODO: BE 1 INCH AWAY FROM SUBMERSIBLE
        //telemetry message?
        //or 2.54cm from submersible
        moveY(-0.2);
        sleep(300);

        //stop moving
        moveY(0);
        sleep(100);

        //bring vertical arm down
        verticalArm.setPower(0.6);
        sleep(1700);

        //bring wrist all the way up (?)
        wrist.setPosition(1);
        sleep(300);

        //move back
        moveY(.2);
        sleep(500);

        //move to side of field with parking
        moveX(-0.5);
        sleep(4000);

        //park
        moveY(.3);
        sleep(2000);

//        moveY(-0.2);
//        sleep(300);




    }
}