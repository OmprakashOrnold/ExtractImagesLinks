package extract.images.service;

import extract.images.dtos.ImageUrlReqest;
import extract.images.entities.Image;
import extract.images.keys.JsonKeys;
import extract.images.service.ImageService;
import extract.images.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    public static String folderId;
    public static String hmac;
    public static String timestamp;

    private JSONObject fetchJsonData(ImageUrlReqest imageUrlReqest) {
        log.info( "Requested url is {} ", imageUrlReqest.getUrl().trim().toLowerCase());
        String htmlString = JSONUtils.readJSONFromUrlWithOkhttp(imageUrlReqest.getUrl().trim().toLowerCase());
        String jsonString = JSONUtils.extractJsonFromScriptTag(htmlString, JsonKeys.IDENTIFY_JSON_STRING.getValue());
        return JSONUtils.parseJsonStringToJsonObject(jsonString);
    }

    @Override
    public List<Image> getImageInformation(ImageUrlReqest imageUrlReqest) {
        JSONObject jsonObject = fetchJsonData(imageUrlReqest);
        JSONObject levelThreeObject = getLevelThreeObject(jsonObject);
        folderId = levelThreeObject.getString("id");
        hmac = extractHmac(levelThreeObject.getString("photoUrlTemplate"));
        timestamp = extractTimestamp(hmac);
        JSONObject levelFourObject = getLevelFourObject(levelThreeObject);
        List<JSONObject> levelOneArray = getLevelOneArray(levelFourObject);

        List<Image> imageList = levelOneArray.stream()
                .flatMap(this::getInnerJSONArrayJSONObjectList)
                .filter(Objects::nonNull)
                .map(this::buildImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return imageList;
    }

    @Override
    public List<String> getImageUrls(ImageUrlReqest imageUrlReqest) {
        JSONObject jsonObject = fetchJsonData(imageUrlReqest);
        JSONObject levelThreeObject = getLevelThreeObject(jsonObject);
        folderId = levelThreeObject.getString("id");
        hmac = extractHmac(levelThreeObject.getString("photoUrlTemplate"));
        timestamp = extractTimestamp(levelThreeObject.getString("photoUrlTemplate"));
        JSONObject levelFourObject = getLevelFourObject(levelThreeObject);
        List<JSONObject> levelOneArray = getLevelOneArray(levelFourObject);

        List<String> imageUrls = levelOneArray.stream()
                .flatMap(this::getInnerJSONArrayJSONObjectList)
                .filter(Objects::nonNull)
                .map(this::buildImageUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return imageUrls;
    }

    private JSONObject getLevelThreeObject(JSONObject jsonObject) {
        JSONObject levelOneObject = JSONUtils.getSpecificJSONObject(jsonObject, JsonKeys.LEVEL_ONE_OBJECT.getValue());
        JSONObject levelTwoObject = JSONUtils.getSpecificJSONObject(levelOneObject, JsonKeys.LEVEL_TWO_OBJECT.getValue());
        return JSONUtils.getSpecificJSONObject(levelTwoObject, JsonKeys.LEVEL_THREE_OBJECT.getValue());
    }

    private JSONObject getLevelFourObject(JSONObject levelThreeObject) {
        return JSONUtils.getSpecificJSONObject(levelThreeObject, JsonKeys.LEVEL_FOUR_OBJECT.getValue());
    }

    private List<JSONObject> getLevelOneArray(JSONObject levelFourObject) {
        return JSONUtils.getInnerJSONArrayJSONObjectList(levelFourObject, JsonKeys.LEVEL_ONE_ARRAY.getValue());
    }

    private Stream<JSONObject> getInnerJSONArrayJSONObjectList(JSONObject jsonObj) {
        try {
            return JSONUtils.getInnerJSONArrayJSONObjectList(jsonObj, JsonKeys.LEVEL_TWO_ARRAY.getValue()).stream();
        } catch (Exception e) {
            log.error("Error processing jsonObj", e);
            return null;
        }
    }

    private Image buildImage(JSONObject jsonInnerObj) {
        try {
            return Image.builder()
                    .id(jsonInnerObj.getString(JsonKeys.FIELD_VALUE_1.getValue()))
                    .size(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_2.getValue()))
                    .width(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_3.getValue()))
                    .height(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_4.getValue()))
                    .fileName(jsonInnerObj.getString(JsonKeys.FIELD_VALUE_5.getValue()))
                    .imageUrl(buildImageUrl(jsonInnerObj))
                    .build();
        } catch (Exception e) {
            log.error("Error retrieving jsonInnerObj", e);
            return null;
        }
    }

    private String buildImageUrl(JSONObject jsonInnerObj) {
        try {
            return ("https://zenfolio.creatorcdn.com/" + folderId + "/0/1/0/X2XL/0-0-0/" + jsonInnerObj.getString(JsonKeys.FIELD_VALUE_1.getValue().trim()) + "/1/1/" + jsonInnerObj.getString(JsonKeys.FIELD_VALUE_5.getValue().trim()) + "?fjkss=exp=" + timestamp + "~hmac=" + hmac).replace( " ","%" );
        } catch (Exception e) {
            log.error("Error retrieving jsonInnerObj", e);
            return null;
        }
    }

    private String extractHmac(String photoUrlTemplate) {
        String[] hmacArray = photoUrlTemplate.split("hmac=");
        return hmacArray[1].trim();
    }

    private String extractTimestamp(String photoUrlTemplate) {
        String[] hmacArray = photoUrlTemplate.split("hmac=");
        Pattern pattern = Pattern.compile("exp=(\\d+)");
        Matcher matcher = pattern.matcher(hmacArray[0].trim());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
