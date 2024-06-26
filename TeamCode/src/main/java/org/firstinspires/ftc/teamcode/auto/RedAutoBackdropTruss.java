package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotAuto.CollectorAuto;
import org.firstinspires.ftc.teamcode.robotAuto.DepositorAuto;
import org.firstinspires.ftc.teamcode.robotAuto.LiftAuto;
import org.firstinspires.ftc.teamcode.robotAuto.TransferAuto;

@Autonomous(name="Red Auto Backdrop Truss", group="Robot")
public final class RedAutoBackdropTruss extends LinearOpMode {

    private final int READ_PERIOD = 1;
    private HuskyLens huskyLens;
    private HuskyLens.Block[] blocks;

    // Determine the prop position
    int targetTagPos = -1;
    int targetBlockPos = -1; // The block of interest within the blocks array.

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(12, -62, Math.toRadians(-85)));
        CollectorAuto collector = new CollectorAuto(hardwareMap, telemetry);
        DepositorAuto depositor = new DepositorAuto(hardwareMap, telemetry);
        TransferAuto transfer = new TransferAuto(hardwareMap, telemetry);
        LiftAuto lift = new LiftAuto(hardwareMap, telemetry);


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
                if (blocks[0].x < 80) {
                    // Prop is on left
                    line = 4;
                } else if (blocks[0].x > 240) {
                    // prop is on right
                    line = 6;
                } else {
                    // prop is on center
                    line = 5;
                }

//            if (blocks.length != 0)
//            {
//                if (blocks[0].x < 170) {
//                    // Prop is on left
//                    line = 4;
//                } else if (blocks[0].x > 170) {
//                    // prop is on right
//                    line = 5;
//                }


                telemetry.addData("Line", line);
                telemetry.addData("Thing location  :", blocks[0].x);

                telemetry.update();
            }
            //4
            if (blocks.length == 0){
                line = 4
                ;
            }

        }
        telemetry.addData("started after while", isStarted());

        waitForStart();
        telemetry.addData("Going for", line);
        telemetry.addData("x", drive.pose.position.x);
        telemetry.addData("y", drive.pose.position.y);
        telemetry.update();





        if (line == 4) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .splineToLinearHeading(new Pose2d(26, -25, Math.toRadians(180)), Math.toRadians(30), new TranslationalVelConstraint(45))
                                    .lineToXConstantHeading(5)
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.55),
                            collector.collectorOffAction()
//                             Move robot to backdrop
                    )
            );

                            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(0))
                                    .splineToLinearHeading(new Pose2d(57, -22, Math.toRadians(-175)), Math.toRadians(0), new TranslationalVelConstraint(45))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(1.0),
                            depositor.pixelDropAction(),
                            new SleepAction(1.0),
                            depositor.depositorRestingAction(),
                            new SleepAction(1.0)
                            )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(-22, -67, Math.toRadians(180)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                   .splineToLinearHeading(new Pose2d(-65, -49, Math.toRadians(165)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build(),
                            collector.collectorStackInAction(),
                            transfer.transferInAction()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .strafeTo(new Vector2d(-65, -29))
                                    .build()
                    )
            );

            Actions.runBlocking(
                    new SequentialAction(
                            new SleepAction(1.75),
                            depositor.pixelHoldAction(),
                            new SleepAction(1.35)
                    )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            collector.collectorStackOutAction(),
                            transfer.transferOutAction(),
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(-22,-64, Math.toRadians(180)), Math.toRadians(0),new TranslationalVelConstraint(45))
                                    .setTangent(Math.toRadians(0))
                                    .splineToConstantHeading(new Vector2d(40,-30), Math.toRadians(0))
                                    .build(),
                            collector.collectorStackOffAction(),
                            transfer.transferOffAction(),
                            depositor.depositorScoringAction(),
                            new SleepAction(0.35)
                    )
                    );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .splineToConstantHeading(new Vector2d(53.75,-32), Math.toRadians(0))
                                    .build(),
                            depositor.pixelDropAction(),
                            new SleepAction(0.75),
                            depositor.depositorRestingAction(),
                            drive.actionBuilder(drive.pose)
                                    .splineToConstantHeading(new Vector2d(50, -60), Math.toRadians(180))
                                    .build()
                    )
            );



        }

        if (line == 5) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .splineToLinearHeading(new Pose2d(13, -28, Math.toRadians(90)), Math.toRadians(90))
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(0.55),
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
                                    .splineToLinearHeading(new Pose2d(58, -27.5, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(1.0),
                            depositor.pixelDropAction(),
                            new SleepAction(1.0),
                            depositor.depositorRestingAction()
                    )
            );



            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(-22, -67, Math.toRadians(180)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .splineToLinearHeading(new Pose2d(-65, -49, Math.toRadians(165)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build(),
                            collector.collectorStackInAction(),
                            transfer.transferInAction()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .strafeTo(new Vector2d(-65, -35))
                                    .build()
                    )
            );

            Actions.runBlocking(
                    new SequentialAction(
                            new SleepAction(1.0),
                            depositor.pixelHoldAction(),
                            new SleepAction(1.0)
                    )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            collector.collectorStackOutAction(),
                            transfer.transferOutAction(),
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(-22,-65, Math.toRadians(180)), Math.toRadians(0),new TranslationalVelConstraint(45))
                                    .splineToConstantHeading(new Vector2d(40,-30), Math.toRadians(-40))
                                    .build(),
                                    depositor.depositorScoringAction(),
                                    collector.collectorStackOffAction(),
                                    transfer.transferOffAction(),
                            new SleepAction(0.35)
                            )
                    );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                    .splineToConstantHeading(new Vector2d(53.75,-38), Math.toRadians(0))
                                    .build(),
                            depositor.pixelDropAction(),
                            new SleepAction(0.75),
                            depositor.depositorRestingAction(),
                            drive.actionBuilder(drive.pose)
                                    .splineToConstantHeading(new Vector2d(50, -60), Math.toRadians(180))
                                    .build()
                    )
            );

        }

        if (line == 6) {
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(0))
                                    .splineToLinearHeading(new Pose2d(27, -25, Math.toRadians(-180)),Math.PI/2)
                                    .build(),
                            collector.collectorOutAction(),
                            new SleepAction(.55),
                            collector.collectorOffAction()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(57, -43, Math.toRadians(180)), Math.toRadians(0))
                                    .build(),
                            depositor.depositorScoringAction(),
                            new SleepAction(1.0),
                            depositor.pixelDropAction(),
                            new SleepAction(1.0),
                            depositor.depositorRestingAction()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(-90))
                                    .splineToLinearHeading(new Pose2d(-22, -67, Math.toRadians(180)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .splineToLinearHeading(new Pose2d(-65, -49, Math.toRadians(165)), Math.toRadians(180), new TranslationalVelConstraint(35))
                                    .build(),
                            collector.collectorStackInAction(),
                            transfer.transferInAction()
                    )
            );

            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .setTangent(Math.toRadians(90))
                                    .strafeTo(new Vector2d(-65, -28))
                                    .build()
                    )
            );

            Actions.runBlocking(
                    new SequentialAction(
                            new SleepAction(1.5),
                            depositor.pixelHoldAction(),
                            new SleepAction(1.35)
                    )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            collector.collectorStackOutAction(),
                            transfer.transferOutAction(),
                            drive.actionBuilder(drive.pose)
                                    .setReversed(true)
//                                    .setTangent(Math.toRadians(-90))
                                    .splineToConstantHeading(new Vector2d(-26, -64), Math.toRadians(-90))
                                    .splineToConstantHeading(new Vector2d(40,-30), Math.toRadians(0))
                                    .build(),
                            collector.collectorStackOffAction(),
                            transfer.transferOffAction(),
                            depositor.depositorScoringAction(),
                            new SleepAction(0.35)
                    )
            );
            drive.updatePoseEstimate();
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    .splineToConstantHeading(new Vector2d(53.75,-25), Math.toRadians(0))
                                    .build(),
                            depositor.pixelDropAction(),
                            new SleepAction(0.75),
                            depositor.depositorRestingAction(),
                            drive.actionBuilder(drive.pose)
                                    .splineToConstantHeading(new Vector2d(50, -60), Math.toRadians(180))
                                    .build()
                    )
            );

        }

        while (opModeIsActive()){
            telemetry.addData("Robot Position", drive.actionBuilder(drive.pose));
            telemetry.addData("x", drive.pose.position.x);
            telemetry.addData("y", drive.pose.position.y);
            telemetry.update();
        }

    }
}



