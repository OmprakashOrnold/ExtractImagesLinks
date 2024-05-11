package extract.images.service;

import extract.images.entities.Image;
import extract.images.keys.JsonKeys;
import extract.images.service.ImageService;
import extract.images.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private JSONObject fetchJsonData() {
        String htmlString = JSONUtils.readJSONFromUrlWithOkhttp(JsonKeys.TARGET_URL.getValue());
        String jsonString = JSONUtils.extractJsonFromScriptTag(htmlString, JsonKeys.IDENTIFY_JSON_STRING.getValue());
        return JSONUtils.parseJsonStringToJsonObject(jsonString);
    }

    @Override
    public List<Image> getImageInformation() {
        JSONObject jsonObject = fetchJsonData();
        JSONObject levelOneObject = JSONUtils.getSpecificJSONObject(jsonObject, JsonKeys.LEVEL_ONE_OBJECT.getValue());
        JSONObject levelTwoObject = JSONUtils.getSpecificJSONObject(levelOneObject, JsonKeys.LEVEL_TWO_OBJECT.getValue());
        JSONObject levelThreeObject = JSONUtils.getSpecificJSONObject(levelTwoObject, JsonKeys.LEVEL_THREE_OBJECT.getValue());
        JSONObject levelFourObject = JSONUtils.getSpecificJSONObject(levelThreeObject, JsonKeys.LEVEL_FOUR_OBJECT.getValue());
        List<JSONObject> levelOneArray = JSONUtils.getInnerJSONArrayJSONObjectList(levelFourObject, JsonKeys.LEVEL_ONE_ARRAY.getValue());

        // Using Streams to process the JSONObjects and collect Image objects
        List<Image> imageList = levelOneArray.stream()
                .flatMap(jsonObj -> {
                    try {
                        return JSONUtils.getInnerJSONArrayJSONObjectList(jsonObj, JsonKeys.LEVEL_TWO_ARRAY.getValue()).stream();
                    } catch (Exception e) {
                        log.error("Error processing jsonObj", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out nulls
                .map(jsonInnerObj -> {
                    try {
                        return Image.builder()
                                .id(jsonInnerObj.getString(JsonKeys.FIELD_VALUE_1.getValue()))
                                .size(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_2.getValue()))
                                .width(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_3.getValue()))
                                .height(jsonInnerObj.getInt(JsonKeys.FIELD_VALUE_4.getValue()))
                                .fileName(jsonInnerObj.getString(JsonKeys.FIELD_VALUE_5.getValue()))
                                .imageUrl("https://zenfolio.creatorcdn.com/a4e63763-b374-4d89-bd0e-99f7203da30b/0/1/0/X2XL/0-0-0/"+jsonInnerObj.getString(JsonKeys.FIELD_VALUE_1.getValue())+"/1/1/27.jpg?fjkss=exp=1720024200~hmac=11382815ded447c211f556aaffcf81e35cbdd049d4c82c6fcc2042ede97badb7")
                                .build();
                    } catch (Exception e) {
                        log.error("Error retrieving jsonInnerObj", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out nulls
                .collect(Collectors.toList());

        return imageList;
    }

    public List<String> getImageUrls() {
        JSONObject jsonObject = fetchJsonData();
        JSONObject levelOneObject = JSONUtils.getSpecificJSONObject(jsonObject, JsonKeys.LEVEL_ONE_OBJECT.getValue());
        JSONObject levelTwoObject = JSONUtils.getSpecificJSONObject(levelOneObject, JsonKeys.LEVEL_TWO_OBJECT.getValue());
        JSONObject levelThreeObject = JSONUtils.getSpecificJSONObject(levelTwoObject, JsonKeys.LEVEL_THREE_OBJECT.getValue());
        JSONObject levelFourObject = JSONUtils.getSpecificJSONObject(levelThreeObject, JsonKeys.LEVEL_FOUR_OBJECT.getValue());
        List<JSONObject> levelOneArray = JSONUtils.getInnerJSONArrayJSONObjectList(levelFourObject, JsonKeys.LEVEL_ONE_ARRAY.getValue());

        // Using Streams to process the JSONObjects and collect Image URLs
        List<String> imageUrls = levelOneArray.stream()
                .flatMap(jsonObj -> {
                    try {
                        return JSONUtils.getInnerJSONArrayJSONObjectList(jsonObj, JsonKeys.LEVEL_TWO_ARRAY.getValue()).stream();
                    } catch (Exception e) {
                        log.error("Error processing jsonObj", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out nulls
                .map(jsonInnerObj -> {
                    try {
                        return "https://zenfolio.creatorcdn.com/a4e63763-b374-4d89-bd0e-99f7203da30b/0/1/0/X2XL/0-0-0/" + jsonInnerObj.getString(JsonKeys.FIELD_VALUE_1.getValue()) + "/1/1/27.jpg?fjkss=exp=1720024200~hmac=11382815ded447c211f556aaffcf81e35cbdd049d4c82c6fcc2042ede97badb7";
                    } catch (Exception e) {
                        log.error("Error retrieving jsonInnerObj", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Filter out nulls
                .collect(Collectors.toList());

        return imageUrls;
    }


}
