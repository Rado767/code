package uk.ac.ed.inf.sdp2012.group7.testing.vision;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


public class VisionTesting {

    public static void main(String[] args){
        String videoDevice = "/dev/video0";
        int width = 640;
        int height = 480;
        int channel = 0;
        int videoStandard = V4L4JConstants.STANDARD_PAL;
        int compressionQuality = 80;

        try {
            new VisionFeed(videoDevice, width, height, channel, videoStandard, compressionQuality);
        } catch (V4L4JException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}