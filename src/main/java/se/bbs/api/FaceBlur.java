package se.bbs.api;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.stereotype.Component;
import se.bbs.service.Camera;
import se.bbs.service.Preview;
import se.bbs.support.Rectangle;

import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static se.bbs.support.Image.changeColorSpace;
import static se.bbs.support.Image.gray;

@Component
public class FaceBlur {

    private Preview preview;
    private Rectangle rectangle;
    private FaceDetection faceDetection;
    private OpenCVFrameConverter.ToIplImage converter;

    public FaceBlur(Preview preview, Rectangle rectangle, FaceDetection faceDetection) {
        this.preview = preview;
        this.rectangle = rectangle;
        this.faceDetection = faceDetection;
        this.converter = new OpenCVFrameConverter.ToIplImage();
    }

    public void preview(Camera source) throws FrameGrabber.Exception {
        OpenCVFrameGrabber camera = source.getWebCameSource();
        camera.start();
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();

        while (true) {
            cvClearMemStorage(storage);
            opencv_core.IplImage image = converter.convert(camera.grab());
            opencv_core.IplImage org = cvCloneImage(image);
            opencv_core.IplImage gray = gray(image);
            changeColorSpace(image, gray, CV_BGR2GRAY);
            opencv_core.CvSeq faces = faceDetection.detect(gray, storage, 1.1, 3);
            int total = faces.total();
            if (total > 0) {
                System.out.println(total);
                //todo us ellipse
                List<opencv_core.CvRect> rects = rectangle.getRect(faces);
                rects.forEach(r -> {
                    opencv_core.IplImage face = cvCreateImage(new opencv_core.CvSize(r.width(), r.height()), org.depth(), org.nChannels());
                    cvSetImageROI(org, r);
                    cvCopy(org, face, null);
                    cvSmooth(face, face, CV_GAUSSIAN, 51, 51, 0, 0);
                    cvAdd(org, face, org);
//                    preview.show(converter.convert(org));
                });
            }
            preview.show(converter.convert(org));
        }
    }
}
