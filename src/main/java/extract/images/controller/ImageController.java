package extract.images.controller;

import extract.images.dtos.ImageUrlReqest;
import extract.images.entities.Image;
import extract.images.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/images-info")
    public ResponseEntity<List<Image>> getImages(@RequestBody ImageUrlReqest imageUrlReqest) {
       return ResponseEntity.ok(imageService.getImageInformation(imageUrlReqest));
    }

    @PostMapping("/request-image-urls")
    public ResponseEntity<List<String>> getImageUrls(@RequestBody ImageUrlReqest imageUrlReqest) {
        return ResponseEntity.ok(imageService.getImageUrls(imageUrlReqest));
    }
}
