package se.bbs.support;

import org.bytedeco.javacpp.opencv_core;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;

@Component
public class Rectangle {
    public void draw(opencv_core.CvSeq image){
        IntStream.of(0, image.total())
                .mapToObj(i -> cvGetSeqElem(image, i))
                .map(opencv_core.CvRect::new)
                .forEach(rect -> draw(rect, image));
    }

    private void draw(opencv_core.CvRect r, opencv_core.CvArr image) {
        int x = r.x();
        int y = r.y();
        int w = r.width();
        int h = r.height();
        cvRectangle(image, cvPoint(x, y), cvPoint(x + w, y + h), opencv_core.CvScalar.RED, 1, CV_AA, 0);
    }
}
