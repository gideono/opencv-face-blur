package se.bbs.service;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import se.bbs.commons.Classifier;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import static org.bytedeco.javacpp.opencv_core.cvLoad;

@Service
public class ClassifierResolver {

    @Autowired
    private ResourceLoader resourceLoader;

    public Pointer get(String classifier){
        return cvLoad(fromDisk(classifier).getAbsolutePath());
    }

    private File fromDisk(String name) {
        String path = getFilePath(name);
        return  Optional.of(new File(path))
                .orElse(download(name));
    }

    private File download(String name) {
        try {
            URL url = Objects.requireNonNull(Classifier.toURI(name)).toURL();
            return Loader.extractResource(url, null, name, ".xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFilePath(String name) {
        try {
            return resourceLoader.getResource("classpath:haar_classifier/" + name)
                    .getFile()
                    .getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
