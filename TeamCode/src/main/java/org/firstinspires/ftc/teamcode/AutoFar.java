package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Far", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoFar extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();

        //Thwip down
        top.setPosition(1);
        sleep(3000);

        //Move to the side to go around the pole
        moveX(.2);//used to be .3
        sleep(350);//used to be 500
        //in milliseconds

        //Moonwalk into parking
        moveY(-0.5);
        sleep(1550);
    }
}