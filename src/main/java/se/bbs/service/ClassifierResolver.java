package se.bbs.service;

import org.bytedeco.javacpp.Loader;
import org.springframework.stereotype.Service;
import se.bbs.commons.Classifier;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClassifierResolver {

    public String get(String classifier){
        return fromDisk(classifier).getAbsolutePath();
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
        return this.getClass().getResource(name).getFile();
    }
}
