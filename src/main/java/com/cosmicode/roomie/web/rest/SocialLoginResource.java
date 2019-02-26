package com.cosmicode.roomie.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.cosmicode.roomie.domain.User;
import com.cosmicode.roomie.security.jwt.JWTFilter;
import com.cosmicode.roomie.security.jwt.TokenProvider;
import com.cosmicode.roomie.service.UserService;
import com.cosmicode.roomie.service.dto.UserDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SocialLoginResource {
    private final Logger log = LoggerFactory.getLogger(SocialLoginResource.class);

    private final UserService userService;
    private final TokenProvider tokenProvider;


    public SocialLoginResource(UserService userService, TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    @PostMapping("/authenticate/facebook")
    @Timed
    public ResponseEntity authorizeClientFromFacebook(@RequestBody String token, HttpServletResponse response) throws IOException {

        token=token.replace("\"","");

        String graph="";
        try {

            String g = "https://graph.facebook.com/v3.0/me?access_token=" + token + "&fields=id,email,first_name,last_name";
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                c.getInputStream()));
            String inputLine;
            StringBuffer b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine).append("\n");
            in.close();
            graph = b.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }
        FBData data = getGraphData(graph);

        Optional<User> findUser = userService.getUserWithAuthoritiesByEmail(data.email);

        UserDTO userDto = new UserDTO();

        userDto.setLogin(data.email);
        userDto.setEmail(data.email);
        userDto.setFirstName(data.firstName);
        userDto.setLastName(data.lastName);
        Set<String> authorities =  new HashSet<>();
        authorities.add("ROLE_USER");
        authorities.add("ROLE_USER_FACEBOOK");
        userDto.setAuthorities(authorities);

        User user = findUser.orElseGet(() -> userService.createUser(userDto));

        return authenticateSocialUser(user,response);
    }

    static class FBData{
        public String firstName;
        public String lastName;
        public String email;
        boolean isValid(){
            return firstName != null && lastName!=null && email!=null;
        }
    }

    private FBData getGraphData(String fbGraph) {
        log.debug("Facebook User data : {}", fbGraph);
        FBData ret=new FBData();
        try {
            JSONObject json = new JSONObject(fbGraph);

            if (json.has("email"))
                ret.email= json.getString("email");
            if (json.has("last_name"))
                ret.lastName= json.getString("last_name");
            if (json.has("first_name"))
                ret.firstName= json.getString("first_name");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in parsing FB graph data. " + e);
        }
        return ret;
    }

    @PostMapping("/authenticate/google")
    @Timed
    public ResponseEntity authorizeClientFromGoogle(@RequestBody String token, HttpServletResponse response) throws IOException {

        Reader fileReader = null;

        if(System.getenv("GOOGLE_OATH2_CREDENTIALS") != null) {
            log.debug("Using env google credentials");
            fileReader = new StringReader(System.getenv("GOOGLE_OATH2_CREDENTIALS"));
        } else {
            log.debug("Using file google credentials");
            File file = new File(getClass().getResource("/googlecredentials.json").getFile());
            fileReader = new FileReader(file);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), fileReader);

        token=token.replace("\"","");

        GoogleTokenResponse tokenResponse =
            new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://www.googleapis.com/oauth2/v4/token",
                clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getClientSecret(),
                token,
                "")
                .execute();

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();

        log.debug("Google User data : {}", payload.toString());

        String email = payload.getEmail();
        String name = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");

        Optional<User> findUser = userService.getUserWithAuthoritiesByEmail(email);

        UserDTO userDto = new UserDTO();

        userDto.setLogin(email);
        userDto.setEmail(email);
        userDto.setFirstName(name);
        userDto.setLastName(familyName);
        Set<String> authorities =  new HashSet<>();
        authorities.add("ROLE_USER");
        authorities.add("ROLE_USER_GOOGLE");
        userDto.setAuthorities(authorities);

        User user = findUser.orElseGet(() -> userService.createUser(userDto));

        return authenticateSocialUser(user,response);
    }

    private ResponseEntity authenticateSocialUser(User user, HttpServletResponse response) {
        try {
            Authentication authentication = new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    ArrayList<SimpleGrantedAuthority> ret = new ArrayList<>();
                    ret.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return ret;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return user;
                }

                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean b) throws IllegalArgumentException {

                }

                @Override
                public String getName() {
                    return user.getEmail();
                }
            };

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication, false);
            response.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new UserJWTController.JWTToken(jwt));
        } catch (AuthenticationException ae) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",
                ae.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
