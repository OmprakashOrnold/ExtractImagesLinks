package extract.images.controller;

import extract.images.entities.Image;
import extract.images.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images-info")
    public ResponseEntity<List<Image>> getImages() {
       return ResponseEntity.ok(imageService.getImageInformation());
    }

    @GetMapping("/image-urls")
    public ResponseEntity<List<String>> getImageUrls() {
        return ResponseEntity.ok(imageService.getImageUrls());
    }
}
