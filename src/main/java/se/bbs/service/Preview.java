package se.bbs.service;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.swing.*;

import static java.lang.Double.valueOf;

@Service
public class Preview {

    @Value("app.preview.title")
    private String title;
    private CanvasFrame canvas;

    public Preview() {
        canvas = new CanvasFrame(title);
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show(Frame image) {
        canvas.showImage(image);
    }

    public void show(Frame image, double frameRate) {
        long pause = (long) (1000 / (frameRate == 0 ? 10 : frameRate));
        System.out.println(pause);
        try {
            Thread.sleep(pause);
            show(image);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setup() {

    }
}
