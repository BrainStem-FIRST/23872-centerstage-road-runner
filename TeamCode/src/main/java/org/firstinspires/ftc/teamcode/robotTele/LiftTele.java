package org.firstinspires.ftc.teamcode.robotTele;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class LiftTele {
    public DcMotorEx liftMotor;
    private HardwareMap hardwareMap;
    private final Telemetry telemetry;
    public LiftState liftState = LiftState.ZERO;
    public int levelCounter = 0;


    private final static double kP = 0.01;
    private final static double kI = 0.0;
    private final static double kD = 0.0;
    private final static double kS = 0.04;
    PIDController pidController = new PIDController(kP, kI, kD);
    private final static int levelZeroHeight = 0;
    private final static int levelOneHeight = 90;
    private final static int levelTwoHeight = 190;
    private final static int levelThreeHeight = 325;
    private final static int levelFourHeight = 430;
    private final static int levelFiveHeight = 540;
    private final static int levelSixHeight = 665;
    private final static int levelSevenHeight = 785;
    private final static int levelEightHeight = 900;
    private final static int levelNineHeight = 1080;

    public LiftTele(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        pidController.setInputBounds(0, 1080);
        pidController.setOutputBounds(-0.25, 1);
        liftMotor = new org.firstinspires.ftc.teamcode.util.CachingMotor(hardwareMap.get(DcMotorEx.class, "Lift"));
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public enum LiftState {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, RESET
    }

    public void liftState() {

        switch (liftState) {
            case RESET: {
                resetEncoders();
            }
            case ZERO: {
                setLiftHeight(levelZeroHeight);
                updateLevel();
                telemetry.addData("lift enum", "Zero");
                break;
            }
            case ONE: {
                setLiftHeight(levelOneHeight);
                updateLevel();
                telemetry.addData("lift enum", "One");
                break;
            }
            case TWO: {
                setLiftHeight(levelTwoHeight);
                updateLevel();
                telemetry.addData("lift enum", "Zero");
                break;
            }
            case THREE: {
                setLiftHeight(levelThreeHeight);
                updateLevel();
                telemetry.addData("lift enum", "Two");
                break;
            }
            case FOUR: {
                setLiftHeight(levelFourHeight);
                updateLevel();
                telemetry.addData("lift enum", "Zero");
                break;
            }
            case FIVE: {
                setLiftHeight(levelFiveHeight);
                updateLevel();
                telemetry.addData("lift enum", "Three");
                break;
            }
            case SIX: {
                setLiftHeight(levelSixHeight);
                updateLevel();
                telemetry.addData("lift enum", "Three");
                break;
            }
            case SEVEN: {
                setLiftHeight(levelSevenHeight);
                updateLevel();
                telemetry.addData("lift enum", "Four");
                break;
            }
            case EIGHT: {
                setLiftHeight(levelEightHeight);
                updateLevel();
                telemetry.addData("lift enum", "Five");
                break;
            }
            case NINE: {
                setLiftHeight(levelNineHeight);
                updateLevel();
                telemetry.addData("lift enum", "Five");
                break;
            }
        }
    }
            public void setLiftHeight(int liftHeight) {
                pidController.setTarget(liftHeight);
                double currentPosition = liftMotor.getCurrentPosition();
                double error = liftHeight - currentPosition;
                if (Math.abs(error) < 5){
                    error = 0;
                }
                double power = pidController.updateWithError(error) + kS;
                if (Math.abs(error) < 25){
                    power = power/4;
                }
                if (liftHeight < 25 && currentPosition < 25) {
                    power = 0;
                }
                liftMotor.setPower(power);
                telemetry.addData("liftError", error);
                telemetry.addData("liftPower", power);
                telemetry.addData("currentPosition", currentPosition);
                telemetry.addData("liftTarget", liftHeight);
                telemetry.addData("lift encoder", liftMotor.getCurrentPosition());

            }

            public void increaseLevel(){

                levelCounter += 1;

                if (levelCounter >= 9){
                    levelCounter = 9;
                }
            }
            public void decreaseLevel() {
                levelCounter -= 1;
                if (levelCounter < 0) {
                    levelCounter = 0;
                }
            }
            public void updateLevel(){
                telemetry.addData("Level Counter", levelCounter);
                switch (levelCounter){
                    case 0:
                        liftState = LiftState.ZERO;
                        break;
                    case 1:
                        liftState = LiftState.ONE;
                        break;
                    case 2:
                        liftState = LiftState.TWO;
                        break;
                    case 3:
                        liftState = LiftState.THREE;
                        break;
                    case 4:
                        liftState = LiftState.FOUR;
                        break;
                    case 5:
                        liftState = LiftState.FIVE;
                        break;
                    case 6:
                        liftState = LiftState.SIX;
                        break;
                    case 7:
                        liftState = LiftState.SEVEN;
                        break;
                    case 8:
                        liftState = LiftState.EIGHT;
                        break;
                    case 9:
                        liftState = LiftState.NINE;
                        break;
                    case 10:
                        liftState = LiftState.RESET;
                        break;
                }
            }
            public void setLiftOff () {
        liftMotor.setPower(0);
    }

    public void setRawPower (double power){
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setPower(power);
    }

    public void setLiftZero () {
        liftState = LiftState.ZERO;
    }

    public void setLiftOne () {
        liftState = LiftState.ONE;
    }
    public void setLiftTwo() {
        liftState = LiftState.TWO;
    }
    public void setLiftThree() {
        liftState = LiftState.THREE;
    }
    public void setLiftFour() {
        liftState = LiftState.FOUR;
    }
    public void setLiftFive() {
        liftState = LiftState.FIVE;
    }
    public void  resetEncoders() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setPower(-0.05);
    }

    public void resetLift(){
        liftState = LiftState.RESET;
    }

}