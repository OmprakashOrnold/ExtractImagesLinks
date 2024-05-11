package extract.images.keys;

public enum JsonKeys {
    IDENTIFY_JSON_STRING("script[id=__NEXT_DATA__]"),
    TARGET_URL("https://brothersphotographyj.zenfoliosite.com/sai-krishna--nikitha-prewed-1?mediaId=3be48791-0d0f-49f8-8051-291fef896c29"),

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
