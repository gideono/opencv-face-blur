package se.bbs.commons;

import java.net.URI;
import java.net.URISyntaxException;

public class Classifier {
    public static String BASE = "https://raw.github.com/Itseez/opencv/2.4.0/data/haarcascades/";

    public static String HAARCASCADE_FRONTALFACE_ALT = "haarcascade_frontalface_alt.xml";

    public static URI toURI(String fileName) {
        try {
            return new URI(BASE + fileName);
        } catch (URISyntaxException e) {
            //TODO log
            e.printStackTrace();
        }
        return null;
    }
}
