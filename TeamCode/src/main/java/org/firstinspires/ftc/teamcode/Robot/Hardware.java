package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Modules.Localizer;
import org.firstinspires.ftc.teamcode.Modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.Wrappers.CoolIMU;
import org.firstinspires.ftc.teamcode.Wrappers.Encoder;
public class Hardware {
    public HardwareMap hardwareMap;

    public DcMotorEx mch0, mch1, mch2, mch3;
    public DcMotorEx meh0, meh1, meh2, meh3;

    public Encoder ech0, ech1, ech2, ech3;
    public Encoder eeh0, eeh1, eeh2, eeh3;

    public Servo sch0, sch1, sch2, sch3, sch4, sch5;
    public Servo seh0, seh1, seh2, seh3, seh4, seh5;

    public CoolIMU imu;

    public Localizer localizer;

    public VoltageSensor voltageSensor;
    public static double voltage;

    public static boolean AUTO = false;



    public enum Color{
        Red, Blue, Universal
    }

    public final Color color;

    public Hardware(HardwareMap hm, Color color){
        this(hm, color, false);
    }

    public Hardware(HardwareMap hm, Color color, boolean reset){
        this.hardwareMap = hm;
        this.color = color;

        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

//        chub = hardwareMap.getAll(LynxModule.class).get(0).isParent() ?
//                hardwareMap.getAll(LynxModule.class).get(0) : hardwareMap.getAll(LynxModule.class).get(1);
//
//        ehub = hardwareMap.getAll(LynxModule.class).get(1).isParent() ?
//                hardwareMap.getAll(LynxModule.class).get(0) : hardwareMap.getAll(LynxModule.class).get(1);

        mch0 = hm.get(DcMotorEx.class, "m0");
        mch1 = hm.get(DcMotorEx.class, "m1");
        mch2 = hm.get(DcMotorEx.class, "m2");
        mch3 = hm.get(DcMotorEx.class, "m3");

        meh0 = hm.get(DcMotorEx.class, "m0e");
        meh1 = hm.get(DcMotorEx.class, "m1e");
        meh2 = hm.get(DcMotorEx.class, "m2e");
        meh3 = hm.get(DcMotorEx.class, "m3e");

        ech0 = new Encoder(mch0);
        ech1 = new Encoder(mch1);
        ech2 = new Encoder(mch2);
        ech3 = new Encoder(mch3);

        eeh0 = new Encoder(meh0);
        eeh1 = new Encoder(meh1);
        eeh2 = new Encoder(meh2);
        eeh3 = new Encoder(meh3);

        if(reset){
            mch0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mch1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mch2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mch3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            meh0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            meh1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            meh2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            meh3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            mch0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mch1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mch2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mch3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            meh0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            meh1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            meh2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            meh3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        sch0 = hm.get(Servo.class, "s0");
        sch1 = hm.get(Servo.class, "s1");
        sch2 = hm.get(Servo.class, "s2");
        sch3 = hm.get(Servo.class, "s3");
        sch4 = hm.get(Servo.class, "s4");
        sch5 = hm.get(Servo.class, "s5");

        seh0 = hm.get(Servo.class, "s0e");
        seh1 = hm.get(Servo.class, "s1e");
        seh2 = hm.get(Servo.class, "s2e");
        seh3 = hm.get(Servo.class, "s3e");
        seh4 = hm.get(Servo.class, "s4e");
        seh5 = hm.get(Servo.class, "s5e");

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        imu = new CoolIMU(hm);

        localizer = new Localizer(this);

        MecanumDrive.headingMultiplier = 1;
    }

    public void startThreads(LinearOpMode opMode){
//        imu.startIMUThread(opMode, localizer);
    }

    public void update(){
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.clearBulkCache();
        }
        voltage = voltageSensor.getVoltage();
        imu.update();
        localizer.update();
    }
}
