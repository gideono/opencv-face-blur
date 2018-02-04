package se.bbs;

import org.bytedeco.javacv.FrameGrabber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import se.bbs.api.FaceBlur;
import se.bbs.api.FaceDetection;
import se.bbs.service.Camera;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws FrameGrabber.Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        FaceDetection face = (FaceDetection) context.getBean("faceDetection");
//        face.preview(new Camera());

        FaceBlur faceBlur = (FaceBlur) context.getBean("faceBlur");
        faceBlur.preview(new Camera());
    }
}
