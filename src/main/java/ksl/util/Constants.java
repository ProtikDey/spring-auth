package ksl.util;

public final class Constants {

    public static final String TOKEN_DELIMETER = ":";
    public static final String JWT_SECRET = "demo+";  //demo

    public interface Headers {
        String USER_AGENT = "User-Agent";
        String X_KM_AUTH_TOKEN = "X-KM-AUTH-TOKEN";
        String X_KM_REFRESH_TOKEN = "X-KM-REFRESH-TOKEN";
    }

    public interface Claims {
        String SUB = "sub";
        String USERNAME = "username";
        String CREATED = "created";
        String RANDOM_VALUE = "randomvalue";
        String TYPE = "type";
        String USER_AGENT = "useragent";
    }

    public interface TokenType {
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String REFRESH_TOKEN = "REFRESH_TOKEN";
    }
}
