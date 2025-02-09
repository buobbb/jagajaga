package org.firstinspires.ftc.teamcode.Wrappers;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
public class CoolMotor {
    public enum RunMode {
        RUN, PID, RTP
    }

    public final DcMotorFunny motor;

    private final PIDController pidController = new PIDController(0,0,0);
    private final PIDController controlPidController = new PIDController(0,0,0);
    private PIDCoefficients pidCoefficients = new PIDCoefficients(0,0,0);

    private RunMode runMode;

    private final boolean async;

    public CoolMotor(DcMotorFunny motor, RunMode runMode, boolean reversed){
        this.motor = motor;
        this.async = true;
        MotorConfigurationType motorConfigurationType = this.motor.motor.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        this.motor.motor.setMotorType(motorConfigurationType);
        this.motor.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(reversed)this.motor.motor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.runMode = runMode;
    }

    public CoolMotor(DcMotorFunny motor){
        this(motor, RunMode.RUN, false);
    }

    public CoolMotor(DcMotorFunny motor, RunMode runMode){
        this(motor, runMode, false);
    }

    public CoolMotor(DcMotorFunny motor, boolean reversed){
        this(motor, RunMode.RUN, false);
    }

    public CoolMotor(DcMotorEx motor, RunMode runMode, boolean reversed){
        this.motor = new DcMotorFunny(motor);
        this.async = false;
        MotorConfigurationType motorConfigurationType = this.motor.motor.getMotorType().clone();
        motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
        this.motor.motor.setMotorType(motorConfigurationType);
        this.motor.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if(reversed)this.motor.motor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.runMode = runMode;
    }

    public CoolMotor(DcMotorEx motor){
        this(motor, RunMode.RUN, false);
    }

    public CoolMotor(DcMotorEx motor, RunMode runMode){
        this(motor, runMode, false);
    }

    public CoolMotor(DcMotorEx motor, boolean reversed){
        this(motor, RunMode.RUN, reversed);
    }

    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior zeroPowerBehaviour){
        this.motor.motor.setZeroPowerBehavior(zeroPowerBehaviour);
    }

    public void setMode(RunMode runMode){
        this.runMode = runMode;
    }
    public RunMode getMode(){
        return runMode;
    }

    public double power;
    public int target;

    public void setPower(double power){
        if(runMode == RunMode.PID) return;
        this.power = power;
    }

    public void setTarget(int target){
        this.target = target;
    }

    public void setPID(PIDCoefficients pidCoefficients){
        this.pidCoefficients = pidCoefficients;
    }

    private double feedforward;

    public void setPIDF(PIDCoefficients pidCoefficients, double feedforward){
        this.feedforward = feedforward;
        this.pidCoefficients.p = pidCoefficients.p;
        this.pidCoefficients.i = pidCoefficients.i;
        this.pidCoefficients.d = pidCoefficients.d;
    }

    public void setPIDF(PIDFCoefficients pidfCoefficients, double feedforward){
        this.feedforward = feedforward;
        pidCoefficients.p = pidfCoefficients.p;
        pidCoefficients.i = pidfCoefficients.i;
        pidCoefficients.d = pidfCoefficients.d;
    }

    public void setPIDF(PIDFCoefficients pidfCoefficients){
        if(runMode==RunMode.RTP) motor.motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pidfCoefficients);
    }

    public void calculatePIDPower(double current, double target){
        if(runMode == RunMode.RUN) return;
        pidController.setPID(pidCoefficients.p, pidCoefficients.i, pidCoefficients.d);
        power = feedforward + pidController.calculate(current,target);
    }

    public void calculatePIDPower(double current, double target, double voltage){
        if(runMode == RunMode.RUN) return;
        pidController.setPID(pidCoefficients.p, pidCoefficients.i, pidCoefficients.d);
        power = (feedforward + pidController.calculate(current,target)) * (12.0/voltage);
    }

    public double getPIDPower(double current, double target){
        if(runMode == RunMode.RUN) return 0;
        controlPidController.setPID(pidCoefficients.p, pidCoefficients.i, pidCoefficients.d);
        return controlPidController.calculate(current,target);
    }

    public int getCurrentPosition(){
        return motor.motor.getCurrentPosition();
    }

    public void update(){
        if(runMode == RunMode.RTP) {
            if(motor.motor.getMode()!= DcMotor.RunMode.RUN_TO_POSITION) {
                motor.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor.motor.setTargetPosition(target);
                motor.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor.motor.setPower(power);
            }
            motor.setPowerAsync(power);
            motor.setTargetPositionAsync(target);
            if(!async){
                motor.updatePowerAsync();
                motor.updateTargetPositionAsync();
            }
        }
        else {
            if(motor.motor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER)
                motor.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPowerAsync(power);
            if (!async) motor.updatePowerAsync();
        }
    }
}
