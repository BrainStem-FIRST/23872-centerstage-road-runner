package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;



public class MeepMeepTesting {

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();


        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-32.25, -62.50, Math.toRadians(-90)))
                .setReversed(false)
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-30.75, -29), Math.toRadians(-90))
                .setReversed(true)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(-35, -10, Math.toRadians(180)), Math.toRadians(0))
                .setTangent(Math.toRadians(0))
                .lineToXConstantHeading(30)
                .setReversed(true)
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(57, -48, Math.toRadians(180)), Math.toRadians(-90))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading( new Pose2d(54,-10, Math.toRadians(180)), Math.toRadians(90))
                .build());



                meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)

                .addEntity(myBot)
                .start();
    }
}