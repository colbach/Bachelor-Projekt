package utils.files;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;
import main.MainClass;
import utils.math.RandomHelper;

public class TemporarilyFiles {

    private final static String RANDOM_STATIC_STRING = RandomHelper.randomReadableString(5);

    private static AtomicLong counter = new AtomicLong(0);

    private static String createUniqueFolderName() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("_");
        sb.append(RANDOM_STATIC_STRING);
        long counted = counter.getAndIncrement();
        if (counted == 0) {
            sb.append("_");
            sb.append(counted);
        }
        return sb.toString();
    }

    public static File[] createTemporarilyFiles(String[] names) {

        File directory = new File(MainClass.PATH_FOR_TEMP_FILES + "/" + createUniqueFolderName());
        directory.mkdirs();
        directory.deleteOnExit();

        File[] temporarilyFiles = new File[names.length];

        for (int i = 0; i < names.length; i++) {
            temporarilyFiles[i] = new File(directory, names[i]);
            temporarilyFiles[i].deleteOnExit();
        }

        return temporarilyFiles;
    }

    public static File createTemporarilyFile(String name) {
        return createTemporarilyFiles(new String[]{name})[0];
    }

}
