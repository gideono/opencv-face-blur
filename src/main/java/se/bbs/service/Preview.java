package se.bbs.service;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.util.Optional;

public class Preview {

    private String title;
    private int width;
    private int height;

    private Camera camera;
    private CanvasFrame canvas;

    public Preview(int width, int height, Camera camera) {
        this.width = width;
        this.height = height;
        this.camera = camera;
    }

    public Preview(String title, int width, int height, Camera camera) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.camera = camera;
    }

    public void show() {
        canvas = new CanvasFrame(Optional.of(title).orElse("Preview"));
        try {
            OpenCVFrameGrabber cameraSource = camera.getWebCameSource();
            cameraSource.setImageWidth(width);
            cameraSource.setImageHeight(height);
            cameraSource.start();

            Frame image = cameraSource.grab();
            canvas.setCanvasSize(width, height);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }
}
