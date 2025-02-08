package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Auto Far (Moves Forward)", group = "LinearOpMode")
//Declares as autonomous file, SDK thing
public class AutoFar extends Hardware {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        stopMoving();
        moveY(.5);
        sleep(7000);

        //
    }
}