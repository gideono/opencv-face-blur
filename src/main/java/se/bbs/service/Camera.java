package se.bbs.service;

import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.stereotype.Service;

@Service
public class Camera {
    private static int CAME = 0;

    public OpenCVFrameGrabber getWebCameSource() {
        return new OpenCVFrameGrabber(Camera.CAME);
    }
}
