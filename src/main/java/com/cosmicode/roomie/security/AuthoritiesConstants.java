package com.cosmicode.roomie.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String FACEBOOK = "ROLE_USER_FACEBOOK";

    public static final String GOOGLE = "ROLE_USER_GOOGLE";

    public static final String PRO = "ROLE_USER_PRO";

    private AuthoritiesConstants() {
    }
}
