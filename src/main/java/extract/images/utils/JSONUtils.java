package extract.images.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class will provide JSON common utility functionalities...
 *
 * @author Om Prakash
 */
@Slf4j
public class JSONUtils {

    /**
     * This method is to read JSON URL and get loaded data as string
     * <p>
     * First preference to get data from JSON URL
     *
     * @param url
     * @return string
     */
    public static String readJSONFromUrlWithOkhttp(String url) {
        log.info( "Attempting to read JSON from URL using OkHttpClient: {0}", url );
        String jsonResponse = null;
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url( url ).get().addHeader( "cache-control", "no-cache" ).build();
            jsonResponse = client.newCall( request ).execute().body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info( "Attempted to read JSON from URL using OkHttpClient: {0}", url );
        return jsonResponse;
    }


    /**
     * This method will provide to read JSON URL and get loaded data as string
     * <p>
     * Second preference to get data from JSON URL
     *
     * @param url
     * @return String
     * @throws IOException,JSONException
     */

    public static String readJsonFromUrlWithInputStram(String url) throws IOException, JSONException {
        log.info( "Attempting to read JSON from URL using InputStreamReader: {0}", url );
        InputStream is = new URL( url ).openStream();
        try {
            BufferedReader rd = new BufferedReader( new InputStreamReader( is, Charset.forName( "UTF-8" ) ) );
            String jsonText = readAll( rd );
            JSONObject json = new JSONObject( jsonText );
            return json.toString();
        } finally {
            log.info( "Attempted to read JSON from URL using InputStreamReader: {0}", url );
            is.close();
        }
    }

    /**
     * This method is meant for providing reading with character stream -
     * internally utilized by readJsonFromUrl()
     *
     * @param Character stream object
     * @return String
     * @throws IOException
     */

    private static String readAll(Reader rd) throws IOException {
        log.info( "Reading all content from the Reader" );
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append( (char) cp );
        }
        log.info( "Read all content from the Reader" );
        return sb.toString();
    }


    /**
     * This method is to save JSON as a text file
     *
     * @param output,file path
     * @return void
     */
    public static void saveJSONAsaTextfile(String output, String filepath) {
        log.info( "Attempting to save JSON to file: {}", filepath );
        Writer writer;
        try {
            writer = new FileWriter( filepath );
            BufferedWriter bufferedWriter = new BufferedWriter( writer );
            bufferedWriter.write( output );
            bufferedWriter.close();
            log.info( "Successfully saved JSON to file: {}", filepath );
        } catch (IOException e) {
            e.printStackTrace();
            log.error( "Failed to save JSON to file: {}", filepath, e );
        }
    }

    /**
     * This method is to Get List of inner JSON Objects Which contains in the Given JSON Object
     *
     * @param jsonObject,specificString
     * @return List<JSONObject>
     */
    public static List<JSONObject> getInnerJSONArrayJSONObjectList(JSONObject jsonObject, String specificString) {
        log.info( "Attempting to extract inner JSON objects from array: {}", specificString );
        List<JSONObject> innerJSONObjectList = new ArrayList<JSONObject>();
        try {
            JSONArray innerJSONArray = jsonObject.getJSONArray( specificString );
            int arraySize = innerJSONArray.length();
            if (arraySize > 0) {
                for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                    JSONObject innerJsonObject = innerJSONArray.getJSONObject( arrayIndex );
                    innerJSONObjectList.add( innerJsonObject );
                    log.debug( "Added JSON object to list at index: {}", arrayIndex );
                }
            }
            log.info( "Successfully extracted {} inner JSON objects from array: {}", innerJSONObjectList.size(), specificString );
        } catch (Exception e) {
            log.error( "Failed to extract inner JSON objects from array: {}", specificString, e );
        }
        return innerJSONObjectList;
    }

    /**
     * This method is to Get Specific JSON Object Which contains in the Given JSON Object
     *
     * @param jsonObject,specificString
     * @return JSONObject
     */
	public static JSONObject getSpecificJSONObject(JSONObject jsonObject, String propertyName) {
		log.info("Attempting to retrieve specific JSON object with property name: {}", propertyName);
		JSONObject specificJSONObject = null;
		try {
			if (isValid(jsonObject, propertyName)) {
				specificJSONObject = jsonObject.getJSONObject(propertyName);
				log.info("Successfully retrieved specific JSON object with property name: {}", propertyName);
			} else {
				log.warn("Property name {} is not valid or does not exist in the JSON object", propertyName);
			}
		} catch (Exception e) {
			log.error("Failed to retrieve specific JSON object with property name: {}", propertyName, e);
		}
		return specificJSONObject;
	}
    /**
     * This method is to Get List of Strings Which contains in the Given the JSON Object
     *
     * @param jsonObject,specificString
     * @return List<String>
     */
	public static List<String> getSpecificFeildFromJSONArray(JSONObject jsonObject, String specificString) {
		log.info("Attempting to retrieve specific field from JSON array: {}", specificString);
		List<String> specificFeildList = new ArrayList<String>();
		try {
			if (isValid(jsonObject, specificString)) {
				JSONArray jsonArray = jsonObject.getJSONArray(specificString);
				int arraySize = jsonArray.length();
				if (arraySize > 0) {
					for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
						try {
							specificFeildList.add(jsonArray.getString(arrayIndex));
                            log.debug("Added field value to list at index: {}", arrayIndex);
						} catch (Exception e) {
                            log.error("Failed to retrieve field value at index: {}", arrayIndex, e);
						}
					}
				}
                log.info("Successfully retrieved {} field values from JSON array: {}", specificFeildList.size(), specificString);
			} else {
                log.warn("Specific string {} is not valid or does not exist in the JSON object", specificString);
			}
		} catch (Exception e) {
            log.error("Failed to retrieve specific field from JSON array: {}", specificString, e);
		}
		return specificFeildList;
	}
    /**
     * This method is to Get List of Strings Which contains in the Given the JSON Object
     *
     * @param jsonObject,specificString
     * @return data
     */
    public static String getInnerJSONArrayJSONObjectDataList(JSONObject jsonObject, String propertyName) {
        log.info("Attempting to retrieve data from inner JSON array with property name: {}", propertyName);
        String data = null;
        List<String> innerJSONObjectList = new ArrayList<>();
        try {
            if (isValid(jsonObject, propertyName)) {
                JSONArray propertyNameArray = jsonObject.getJSONArray(propertyName);
                if (propertyNameArray != null) {
                    int arraySize = propertyNameArray.length();
                    if (arraySize > 0) {
                        for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                            String str = propertyNameArray.getString(arrayIndex);
                            innerJSONObjectList.add(str);
                            log.debug("Added data to list at index: {}", arrayIndex);
                        }
                        data = innerJSONObjectList.stream().collect(Collectors.joining(","));
                        log.info("Successfully retrieved data from inner JSON array: {}", data);
                    }
                }
            } else {
                log.warn("Property name {} is not valid or does not exist in the JSON object", propertyName);
            }
        } catch (Exception e) {
            log.error("Failed to retrieve data from inner JSON array with property name: {}", propertyName, e);
        }
        return data;
    }

    /**
     * This method is to Get Particular String comma separated Which contains in inner inner jsonObject
     *
     * @param jsonObject,specificString
     * @return string
     */

    public static String getInnerJsonObjectCommaSepStrings(JSONObject jsonObject, String innerObject, String innerInnerObject) {
        log.info("Attempting to retrieve comma-separated strings from inner JSON objects: {} -> {}", innerObject, innerInnerObject);
        String data = null;
        List<String> innerJSONObjectList = new ArrayList<>();
        List<JSONObject> innerJsonObjectList = getInnerJSONArrayJSONObjectList(jsonObject, innerObject);
        if (innerJsonObjectList != null && !innerJsonObjectList.isEmpty()) {
            for (JSONObject innerJsonObject : innerJsonObjectList) {
                try {
                    String str = getSpecificFeildFromJSONObject(innerJsonObject, innerInnerObject);
                    innerJSONObjectList.add(str);
                    log.debug("Added string to list: {}", str);
                } catch (Exception e) {
                    log.error("Failed to retrieve string from inner JSON object: {}", innerInnerObject, e);
                }
            }
            data = innerJSONObjectList.stream().collect(Collectors.joining(","));
            log.info("Successfully retrieved comma-separated strings: {}", data);
        } else {
            log.warn("No inner JSON objects found for property: {}", innerObject);
        }
        return data;
    }

    /**
     * This method is to Get Particular String Which contains in the Given JSON Object
     *
     * @param jsonObject,specificString
     * @return string
     */
    public static String getSpecificFeildFromJSONObject(JSONObject jsonObject, String specificString) {
        log.info("Attempting to retrieve specific field from JSON object: {}", specificString);
        String propertyName = null;
        try {
            if (isValid(jsonObject, specificString)) {
                propertyName = jsonObject.get(specificString).toString();
                log.info("Successfully retrieved specific field: {}", propertyName);
            } else {
                log.warn("Specific string {} is not valid or does not exist in the JSON object", specificString);
            }
        } catch (Exception e) {
            log.error("Failed to retrieve specific field from JSON object: {}", specificString, e);
        }
        return propertyName;
    }

    /**
     * This method is meant for validating property for a provided JSON Object
     *
     * @param jsonObject,proeprtyName
     * @return boolean
     */
    public static boolean isValid(JSONObject jsonObject, String propertyName) {
        boolean valid = false;
        try {
            if (propertyName != null) {
                if (jsonObject.has(propertyName) && !jsonObject.get(propertyName).toString().equalsIgnoreCase("null") && !jsonObject.get(propertyName).toString().equalsIgnoreCase("")) {
                    valid = true;
                    log.debug("Property {} is valid and not null or empty", propertyName);
                } else {
                    log.debug("Property {} is either null, empty, or does not exist", propertyName);
                }
            } else {
                log.debug("Property name is null");
            }
        } catch (Exception ex) {
            log.error("Error checking validity of property {}: {}", propertyName, ex.getMessage());
            valid = false;
        }
        return valid;
    }


    //2 new methods added

    /**
     * This method is to get Comma Separated Feilds Which contains in the Given the JSON Array
     *
     * @param jsonObject,specificString
     * @return String
     */
    public static String getCommaSeparatedFeildsFromJSONArray(JSONObject jsonObject, String specificString) {
        log.info("Attempting to retrieve comma-separated fields from JSON array: {}", specificString);
        List<String> specificFeildList = new ArrayList<String>();
        try {
            if (isValid(jsonObject, specificString)) {
                JSONArray jsonArray = jsonObject.getJSONArray(specificString);
                int arraySize = jsonArray.length();
                if (arraySize > 0) {
                    for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                        try {
                            String data = jsonArray.getString(arrayIndex);
                            specificFeildList.add(data);
                            log.debug("Added field value to list at index: {}", arrayIndex);
                        } catch (Exception e) {
                            log.error("Failed to retrieve field value at index: {}", arrayIndex, e);
                        }
                    }
                }
                String commaSeparatedFields = specificFeildList.stream().collect(Collectors.joining(","));
                log.info("Successfully retrieved comma-separated fields: {}", commaSeparatedFields);
                return commaSeparatedFields;
            } else {
                log.warn("Specific string {} is not valid or does not exist in the JSON object", specificString);
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to retrieve comma-separated fields from JSON array: {}", specificString, e);
            return null;
        }
    }

    /**
     * This method is to get List Of String Which contains in the Given the JSON Object
     *
     * @param jsonObject,specificString
     * @return List<String>
     */
    public static List<String> getAllFieldsFromJSONArray(JSONObject jsonObject, String specificString) {
        log.info("Attempting to retrieve all fields from JSON array: {}", specificString);
        List<String> innerJSONObjectList = new ArrayList<String>();
        try {
            JSONArray innerJSONArray = jsonObject.getJSONArray(specificString);
            int arraySize = innerJSONArray.length();
            if (arraySize > 0) {
                for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                    try {
                        String fieldValue = innerJSONArray.getString(arrayIndex);
                        innerJSONObjectList.add(fieldValue);
                        log.debug("Added field value to list at index: {}", arrayIndex);
                    } catch (Exception e) {
                        log.error("Failed to retrieve field value at index: {}", arrayIndex, e);
                    }
                }
            }
            log.info("Successfully retrieved {} field values from JSON array: {}", innerJSONObjectList.size(), specificString);
        } catch (Exception e) {
            log.error("Failed to retrieve all fields from JSON array: {}", specificString, e);
        }
        return innerJSONObjectList;
    }

    public static String extractJsonFromScriptTag(String htmlContent,String targetScript) {
        log.info("Attempting to extract JSON from script tag in HTML content");
        if (htmlContent == null || htmlContent.isEmpty()) {
            log.warn("HTML content is null or empty");
            return null;
        }
        Document document = Jsoup.parse(htmlContent);
        Elements scriptTags = document.select(targetScript);
        if (scriptTags.isEmpty()) {
            log.warn("No script tags found matching the JSON string criteria");
            return null;
        }
        Element scriptTag = scriptTags.first();
        String jsonString = scriptTag.data();
        if (jsonString == null || jsonString.isEmpty()) {
            log.warn("Extracted JSON string is null or empty");
            return null;
        }
        log.info("Successfully extracted JSON string from script tag");
        return jsonString;
    }


    public static JSONObject parseJsonStringToJsonObject(String jsonString) {
        JSONObject jsonObject = null;
        if (jsonString != null) {
            try {
                log.info("Attempting to parse JSON string: {}", jsonString.substring(0, Math.min(25, jsonString.length())));
                jsonObject = new JSONObject(jsonString);
                log.info("Successfully parsed JSON string into JSONObject.");
            } catch (Exception e) {
                log.error("Failed to parse JSON string into JSONObject. Error: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Input JSON string is null. Cannot parse null string.");
        }
        return jsonObject;
    }

}
