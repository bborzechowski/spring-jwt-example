package example.springjwtexample.security;

public class Constans {
    public static final String SIGN_UP_URL = "/sign";

    public static final int EXPIRATION_TIME = 432000000; //5 days
    public static final String SECRET = "SecretKay";
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "SecretToken ";
}
