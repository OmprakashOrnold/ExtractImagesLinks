package extract.images.service;

import extract.images.dtos.ImageUrlReqest;
import extract.images.entities.Image;

import java.util.List;

public interface ImageService {

    List<Image> getImageInformation(ImageUrlReqest imageUrlReqest);

    List<String> getImageUrls(ImageUrlReqest imageUrlReqest) ;


}
