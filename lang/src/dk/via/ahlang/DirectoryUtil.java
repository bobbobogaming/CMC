package dk.via.ahlang;

import java.io.File;

public class DirectoryUtil {

    private DirectoryUtil() {}

    public static String getExamplesDir() {
        File workingDirectory = new File(System.getProperty("user.dir"));
        if (workingDirectory.isDirectory() && workingDirectory.getName().equals("CMC")) {
            return System.getProperty("user.dir") + "\\lang\\src\\dk\\via\\ahlang";
        } else if (workingDirectory.isDirectory() && workingDirectory.getName().equals("lang")) {
            return System.getProperty("user.dir") + "\\src\\dk\\via\\ahlang";
        } else {
            return System.getProperty("user.dir");
        }
    }
}
