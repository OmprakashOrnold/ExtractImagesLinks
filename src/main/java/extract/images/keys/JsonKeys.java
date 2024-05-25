package extract.images.keys;

public enum JsonKeys {
    IDENTIFY_JSON_STRING("script[id=__NEXT_DATA__]"),
    TARGET_URL("https://brothersphotographyj.zenfoliosite.com/prewedding-catalog?mediaId=a6408b6b-f05a-4f19-ba6a-c81ffcb0a3c6"),

    LEVEL_ONE_OBJECT("props"),
    LEVEL_TWO_OBJECT("pageProps"),
    LEVEL_THREE_OBJECT("dataGallery"),
    LEVEL_FOUR_OBJECT("parentFolder"),
    LEVEL_FIVE_OBJECT("parentFolder"),

    LEVEL_ONE_ARRAY("folders"),
    LEVEL_TWO_ARRAY("photos"),
    LEVEL_THREE_ARRAY("pageProps"),

    FIELD_VALUE_1("id"),
    FIELD_VALUE_2("size"),
    FIELD_VALUE_3("width"),
    FIELD_VALUE_4("height"),
    FIELD_VALUE_5("fileName");

    private final String value;

    JsonKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
