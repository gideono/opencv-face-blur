package se.bbs.support;

import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;

public class Image {
    public static opencv_core.IplImage gray(opencv_core.IplImage image) {
        return opencv_core.IplImage.create(image.width(), image.height(), IPL_DEPTH_8U, 1);
    }

    public static void changeColorSpace(opencv_core.IplImage source, opencv_core.IplImage target, int encoding){
        cvCvtColor(source, target, encoding);
    }
}
