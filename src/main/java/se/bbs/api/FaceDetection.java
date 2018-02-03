package se.bbs.api;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.bbs.service.Camera;
import se.bbs.service.ClassifierResolver;
import se.bbs.service.Preview;
import se.bbs.support.Rectangle;

import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static org.bytedeco.javacpp.opencv_objdetect.cvHaarDetectObjects;
import static se.bbs.commons.Classifier.HAARCASCADE_FRONTALFACE_ALT;
import static se.bbs.support.Image.changeColorSpace;
import static se.bbs.support.Image.gray;

@Component
public class FaceDetection {
    private ClassifierResolver classifierResolver;
    private Preview preview;
    private Rectangle rectangle;
    private OpenCVFrameConverter.ToIplImage converter;

    @Autowired
    public FaceDetection(ClassifierResolver classifierResolver, Preview preview, Rectangle rectangle) {
        this.classifierResolver = classifierResolver;
        this.preview = preview;
        this.rectangle = rectangle;
        this.converter = new OpenCVFrameConverter.ToIplImage();
    }

    public opencv_core.CvSeq detect(opencv_core.CvArr image, opencv_core.CvMemStorage storage, double scaleFactor, int minNeighbors) {
        opencv_objdetect.CvHaarClassifierCascade cascade =
                new opencv_objdetect.CvHaarClassifierCascade(classifierResolver.get(HAARCASCADE_FRONTALFACE_ALT));
        return cvHaarDetectObjects(image, cascade, storage, scaleFactor, minNeighbors, CV_HAAR_DO_CANNY_PRUNING);
    }

    public void preview(Camera source) throws FrameGrabber.Exception {
        OpenCVFrameGrabber camera = source.getWebCameSource();
        camera.start();
        while (true) {
            opencv_core.IplImage image = converter.convert(camera.grab());
            opencv_core.IplImage gray = gray(image);
            changeColorSpace(image, gray, CV_BGR2GRAY);
            opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
            opencv_core.CvSeq faces = detect(gray, storage, 1.1, 3);
            System.out.println(faces.total());
            rectangle.draw(faces, image);
            preview.show(converter.convert(image));
            cvClearMemStorage(storage);
        }
    }
}
