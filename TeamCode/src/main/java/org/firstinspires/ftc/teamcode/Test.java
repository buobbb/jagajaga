package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.Robot.Hardware;
import org.firstinspires.ftc.teamcode.Utils.Pose;

@Autonomous
public class Test extends LinearOpMode {

    MecanumDrive drive;
    Hardware hardware;
    Pose targetPose1, targetPose2;

    public enum STATES{
        IDLE,
        MOVING,
        TARGET1,
        TARGET2,
        PARK,
        PARKED
    }
    STATES CS = STATES.IDLE, NS = STATES.IDLE;

    public static double target1X = 20, target1Y = 0, target1Heading = 90;
    public static double target2X = 10, target2Y = -20, target2Heading = 270;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(hardwareMap, Hardware.Color.Universal, true);
        drive = new MecanumDrive(hardware, MecanumDrive.RunMode.PID, true);

        targetPose1 = new Pose(target1X, target1Y, Math.toRadians(target1Heading));
        targetPose2 = new Pose(target2X, target2Y, Math.toRadians(target2Heading));

        CS = STATES.TARGET1;

        waitForStart();

        while(opModeIsActive()){
            switch (CS){
                case TARGET1:
                    drive.setTargetPose(targetPose1);
                    CS = STATES.MOVING;
                    NS = STATES.TARGET2;
                    break;

                case MOVING:
                    if(drive.reachedTarget(0.2) && drive.reachedHeading(0.2)){
                        CS = NS;
                    }
                    break;

                case TARGET2:
                    drive.setTargetPose(targetPose2);
                    CS = STATES.MOVING;
                    NS = STATES.PARK;
                    break;

                case PARK:
                    drive.setTargetPose(new Pose(0,0,0));
                    CS = STATES.MOVING;
                    NS = STATES.PARKED;
                    break;
                case PARKED:
                    break;
            }


            hardware.update();
            drive.update();
        }
    }
}
