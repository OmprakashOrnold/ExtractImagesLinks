package extract.images.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    private String id;
    private Integer size;
    private Integer width;
    private Integer height;
    private String fileName;
    private String imageUrl;
}
