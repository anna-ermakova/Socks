package pro.sky.socks.servise;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface FileService {
    void saveToFile(String json);

    String readFromFile();

    void cleanDataFile(Path path);

    File getDataFile();

    InputStreamResource exportFile() throws FileNotFoundException;

    void importFile(MultipartFile file) throws FileNotFoundException;

    String importOperations(MultipartFile file) throws FileNotFoundException;

    InputStreamResource exportOperations(String json) throws FileNotFoundException;

    Path getPath();

    Path getOperationPath();
}

