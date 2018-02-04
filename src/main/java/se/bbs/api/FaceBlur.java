package se.bbs.api;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.stereotype.Component;
import se.bbs.service.Camera;
import se.bbs.service.Preview;
import se.bbs.support.Rectangle;

import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
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
            opencv_core.IplImage gray = gray(image);
            changeColorSpace(image, gray, CV_BGR2GRAY);
            opencv_core.CvSeq faces = faceDetection.detect(gray, storage, 1.1, 3);
            rectangle.draw(faces, image);
            preview.show(converter.convert(image));
            cvClearMemStorage(storage);
            preview.show(converter.convert(image));

        }
    }
}
