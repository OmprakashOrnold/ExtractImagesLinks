package extract.images;

import extract.images.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class ExtractImagesLinksApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run( ExtractImagesLinksApplication.class, args );
    }
}
