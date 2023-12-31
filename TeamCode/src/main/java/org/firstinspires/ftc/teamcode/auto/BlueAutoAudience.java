package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotAuto.CollectorAuto;
import org.firstinspires.ftc.teamcode.robotAuto.DepositorAuto;

@Autonomous(name="Blue Auto Audience", group="Robot")
public final class BlueAutoAudience extends LinearOpMode {

    private final int READ_PERIOD = 1;
    private HuskyLens huskyLens;
    private HuskyLens.Block[] blocks;

    private Pose2d startPose = new Pose2d(-35.25, 62.50, Math.toRadians(90));

    // Determine the prop position
    int targetTagPos = -1;
    int targetBlockPos = -1; // The block of interest within the blocks array.

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));
        //Set starting position
        drive.pose = startPose;
        CollectorAuto collector = new CollectorAuto(hardwareMap, telemetry);
        DepositorAuto depositor = new DepositorAuto(hardwareMap, telemetry);

        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");

        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

        telemetry.update();


        blocks = huskyLens.blocks();

        int line = 3;
        int counter = 0;



        telemetry.addData("started", isStarted());
        while (!isStarted() && !isStopRequested()) {
            blocks = huskyLens.blocks();
            counter++;
            telemetry.addData("amount of blocks", blocks.length);
            telemetry.addData("counter", counter);
            telemetry.addData("started", isStarted());
            telemetry.addData("Current Position", drive.pose.position);
            telemetry.addData("Current Heading", drive.pose.heading.toString());
            drive.updatePoseEstimate();

            telemetry.update();
            if (blocks.length != 0) {
                if (blocks[0].x < 50) {
                    // Prop is on left
                    line = 1;
                } else if (blocks[0].x > 260) {
                    // prop is on right 3
                  line = 1;
                } else {
                    // prop is on center 2
                    line = 1;
                }


                telemetry.addData("Line", line);
                telemetry.addData("Thing location  :", blocks[0].x);

                telemetry.update();
            }
//2
            if (blocks.length == 0){
                line = 1;
            }

        }
        telemetry.addData("started after while", isStarted());

        waitForStart();
        telemetry.addData("Going for", line);
        telemetry.update();
        if (line == 1) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .splineToLinearHeading(new Pose2d(-44, 36, Math.toRadians(0)), Math.toRadians(180))
                                    .build()));
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(false)
                                    .setTangent(Math.toRadians(0))
                                    .splineToConstantHeading(new Vector2d(-36, 34), Math.toRadians(0))
                                    .build()));
                            //collector.collectorInAction(),
                            //new SleepAction(0.85),
                            //collector.collectorOffAction()
//                            drive.actionBuilder(drive.pose)
//                                    .setReversed(true)
//                                    .setTangent(Math.toRadians(90))
//                                    .splineToLinearHeading(new Pose2d(-35, 30, Math.toRadians(0)), Math.toRadians(180))
//                                    .build()
//                            drive.actionBuilder(drive.pose)
//                                    .setTangent(Math.toRadians(-90))
//                                    .strafeTo(new Vector2d(-25,0))
//                                    .build(),
//                            drive.actionBuilder(drive.pose)
//                                    .setTangent(Math.toRadians(-90))
//                                    .splineToLinearHeading(new Pose2d(50, 45, Math.toRadians(180)), Math.toRadians(180))
//                                    .build()
//                            depositor.depositorScoringAction(),
//                            new SleepAction(2.0),
//                            depositor.pixelDropAction(),
//                            new SleepAction(2.0),
//                            depositor.depositorRestingAction(),
//                            new SleepAction(2.5),
//                            drive.actionBuilder(drive.pose)
//                                    .setTangent(Math.toRadians(90))
//                                    .strafeTo(new Vector2d(62, 65))
//                                    .build()
//                    )

            while (opModeIsActive()) {
                telemetry.addData("Heading", drive.pose.heading);
                telemetry.update();
            }
        }
        if (line == 2) {
            Actions.runBlocking(
                    new SequentialAction(
                            collector.drawbridgeUpAction(),
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(15, 34, Math.toRadians(-90)), Math.toRadians(-90))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.65),
                            collector.collectorOffAction(),
                            // Move robot to backdrop
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(180))
//                                    .lineToY(new Vector2d(14, 60))
                                    .splineToLinearHeading(new Pose2d(50, 37, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(2.0),
                            depositor.pixelDropAction(),
                            new SleepAction(2.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(2.5),
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .strafeTo(new Vector2d(62, 65))
                                    .build()
                    )
            );
        }
        if (line == 3) {
            Actions.runBlocking(
                    new SequentialAction(
                            collector.drawbridgeUpAction(),
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(7, 27, Math.toRadians(180)), Math.toRadians(180))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.65),
                            collector.collectorOffAction(),
                            // Move robot to backdrop
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(90))
                                    .splineToLinearHeading(new Pose2d(50, 30, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(2.0),
                            depositor.pixelDropAction(),
                            new SleepAction(2.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(2.5),
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .strafeTo(new Vector2d(62, 65))
                                    .build()
                    )
            );
        }
    }
}



