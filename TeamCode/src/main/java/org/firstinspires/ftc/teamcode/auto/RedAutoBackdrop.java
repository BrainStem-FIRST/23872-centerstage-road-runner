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

@Autonomous(name="Red Auto Backdrop", group="Robot")
public final class RedAutoBackdrop extends LinearOpMode {

    private final int READ_PERIOD = 1;
    private HuskyLens huskyLens;
    private HuskyLens.Block[] blocks;

    // Determine the prop position
    int targetTagPos = -1;
    int targetBlockPos = -1; // The block of interest within the blocks array.

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(12, -62, Math.toRadians(-90)));
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

        int line = 6;
        int counter = 0;


        telemetry.addData("started", isStarted());
        while (!isStarted() && !isStopRequested()) {
            blocks = huskyLens.blocks();
            counter++;
            telemetry.addData("amount of blocks", blocks.length);
            telemetry.addData("counter", counter);
            telemetry.addData("started", isStarted());

            telemetry.update();
            if (blocks.length != 0)
            {
                if (blocks[0].x < 50) {
                    // Prop is on left
                    line = 4;
                } else if (blocks[0].x > 260) {
                    // prop is on right
                    line = 6;
                } else {
                    // prop is on center
                    line = 5;
                }


                telemetry.addData("Line", line);
                telemetry.addData("Thing location  :", blocks[0].x);

                telemetry.update();
            }
            if (blocks.length == 0){
                line = 5;
            }

        }
        telemetry.addData("started after while", isStarted());

        waitForStart();
        telemetry.addData("Going for", line);
        telemetry.update();



        if (line == 4) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .splineToLinearHeading(new Pose2d(6, -27, Math.toRadians(180)), Math.toRadians(180))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.85),
                            collector.collectorOffAction()
                    )
            );
                            // Move robot to backdrop
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                              drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(10))
                                    .splineToLinearHeading(new Pose2d(55,-31, Math.toRadians(-180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(2.0),
                            depositor.pixelDropAction(),
                            new SleepAction(2.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(2.5)
                    )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                              drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading( new Pose2d(48,-65, Math.toRadians(180)), Math.toRadians(-90))
                                    .build()
                    )
            );
        }
        if (line == 5) {
            Actions.runBlocking(
                    new SequentialAction(
                            collector.drawbridgeUpAction(),
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .splineToLinearHeading(new Pose2d(15, -28, Math.toRadians(90)), Math.toRadians(90))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.65),
                            collector.collectorOffAction()
                            )
            );
                            // Move robot to backdrop

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(55, -31, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(2.0),
                            depositor.pixelDropAction(),
                            new SleepAction(2.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(2.5)
                    )
                            );


            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading( new Pose2d(48,-65, Math.toRadians(180)), Math.toRadians(-90))
                                    .build()
                    )
            );
        }
        if (line == 6) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(45))
                                    .splineToLinearHeading(new Pose2d(22, -27, Math.toRadians(0)), Math.toRadians(0))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.85),
                            collector.collectorOffAction()
                            // Move robot to backdrop
                    )
            );
                            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(-115))
                                    .splineToLinearHeading(new Pose2d(55, -45, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(2.0),
                            depositor.pixelDropAction(),
                            new SleepAction(2.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(2.5)
                            )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading( new Pose2d(48,-65, Math.toRadians(180)), Math.toRadians(-90))
                                    .build()
                    )
            );
        }
    }
}



