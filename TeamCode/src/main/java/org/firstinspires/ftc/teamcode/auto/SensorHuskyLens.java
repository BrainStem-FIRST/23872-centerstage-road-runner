package org.firstinspires.ftc.teamcode.auto;

import android.util.Size;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "Sensor: HuskyLens", group = "Sensor")
//@Disabled
//@Config
public class SensorHuskyLens extends LinearOpMode {

    private final int READ_PERIOD = 1;

//    AprilTagDetection myAprilTagDetection;
//    double myTagPoseX = myAprilTagDetection.ftcPose.x;
//    double myTagPoseY = myAprilTagDetection.ftcPose.y;
//    double myTagPoseZ = myAprilTagDetection.ftcPose.z;
//    double myTagPosePitch = myAprilTagDetection.ftcPose.pitch;
//    double myTagPoseRoll = myAprilTagDetection.ftcPose.roll;
//    double myTagPoseYaw = myAprilTagDetection.ftcPose.yaw;


    private HuskyLens huskyLens;

    private HuskyLens.Block[] blocks;

    // Determine the prop position
    int targetTagPos = -1;
    int targetBlockPos = -1; // The block of interest within the blocks array.


    enum alliance {
        RED, BLUE
    }

    int getTargetTag(HuskyLens.Block[] blocks, alliance a) {
//        telemetry.addData("ID 1", "True");
//        telemetry.addData("ID 2", "True");
//        updateTelemetry();




        int propPos;
        // for test purposes, return a known value
        // delete this segment when team prop is available
        //        return 1;
        telemetry.addData("Block List: ", blocks);
        if (blocks.length == 1) {
            if (blocks[0].x < 50) {
                // Prop is on left
                propPos = (a == alliance.BLUE) ? 1 : 4;
            } else if (blocks[0].x > 280) {
                // prop is on right
                propPos = (a == alliance.BLUE) ? 3 : 6;
            } else {
                // prop is on center\
                propPos = (a == alliance.BLUE) ? 2 : 5;
            }
        } else {
            telemetry.addData("Did not see prop", "True");
            telemetry.update();
            // could not recognize; return center
            propPos = (a == alliance.BLUE) ? 2 : 5;
        }

        telemetry.addData("Thing location  :", blocks[0].x);
telemetry.update();

        return propPos;




    }




    @Override
    public void runOpMode()
    {
        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");

        //trying to get april tags to see xyz positions
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .build();



//        while (!isStopRequested()&& opModeIsActive()){
//
//            if (tagProcessor.getDetections().size() > 0) {
//                AprilTagDetection tag = tagProcessor.getDetections().get(0);
//                telemetry.addData("x",tag.ftcPose.x);
//                telemetry.addData("y",tag.ftcPose.y);
//                telemetry.addData("z",tag.ftcPose.z);
//                telemetry.addData("pitch",tag.ftcPose.pitch);
//                telemetry.addData("roll",tag.ftcPose.roll);
//                telemetry.addData("yaw",tag.ftcPose.yaw);
//                telemetry.addData("id", tag.id);
//
//            }
//        }

        /*
         * This sample rate limits the reads solely to allow a user time to observe
         * what is happening on the Driver Station telemetry.  Typical applicatiosns
         * would not likely rate limit.
         */
        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
        /*
         * Immediately expire so that the first time through we'll do the read.
         */

        rateLimit.expire();

        /*
         * Basic check to see if the device is alive and communicating.  This is not
         * technically necessary here as the HuskyLens class does this in its
         * doInitialization() method which is called when the device is pulled out of
         * the hardware map.  However, sometimes it's unclear why a device reports as
         * failing on initialization.  In the case of this device, it's because the
         * call to knock() failed.
         */
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        /*
         * The device uses the concept of an algorithm to determine what types of
         * objects it will look for and/or what mode it is in.  The algorithm may be
         * selected using the scroll wheel on the device, or via software as shown in
         * the call to selectAlgorithm().
         *
         * The SDK itself does not assume that the user wants a particular algorithm on
         * startup, and hence does not set an algorithm.
         *
         * Users, should, in general, explicitly choose the algorithm they want to use
         * within the OpMode by calling selectAlgorithm() and passing it one of the values
         * found in the enumeration HuskyLens.Algorithm.
         */
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

        telemetry.update();
        waitForStart();

        /*
         * Looking for AprilTags per the call to selectAlgorithm() above.  A handy grid
         * for testing may be found at https://wiki.dfrobot.com/HUSKYLENS_V1.0_SKU_SEN0305_SEN0336#target_20.
         *
         * Note again that the device only recognizes the 36h11 family of tags out of the box.
         */
        while(opModeIsActive()) {
            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();

            /*
             * All algorithms, except for LINE_TRACKING, return a list of Blocks where a
             * Block represents the outline of a recognized object along with its ID number.
             * ID numbers allow you to identify what the device saw.  See the HuskyLens documentation
             * referenced in the header comment above for more information on IDs and how to
             * assign them to objects.
             *
             * Returns an empty array if no objects are seen.
             */

//            HuskyLens.Block[] blocks = huskyLens.blocks();
//            telemetry.addData("Block count", blocks.length);
//            for (int i = 0; i < blocks.length; i++) {
//                telemetry.addData("Block", blocks[i].toString());
//            }
//
//            telemetry.update();

            blocks = huskyLens.blocks();
            telemetry.addData("amount of blocks", blocks.length);

            if (blocks.length != 0) {
                targetTagPos = getTargetTag(blocks, alliance.RED); //TODO: this is just an example, change alliance later
                telemetry.addData("Found target prop: ", targetTagPos);
                telemetry.addData("Target prop position: ", blocks[0].x);
            } else {
                telemetry.addLine("Don't see the prop :(");

                if (targetTagPos == -1) {
                    telemetry.addLine("(The prop has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the prop before");
                    telemetry.addData("which was: ", targetTagPos);
                }

                sleep(20);
            }

            telemetry.update();
        }

    }
}
