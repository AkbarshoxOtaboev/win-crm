package uz.script.wincrm.storage;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface StorageService {

    String uploadFile(MultipartFile file);

    Resource downloadFile(String fileName);

    void delete(String fileName);
}
