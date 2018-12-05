package example.springjwtexample.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal (
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(Constans.AUTH_HEADER);

        getHeadersInfo(request);

        if (header == null || !header.startsWith(Constans.TOKEN_PREFIX)) {
            chain.doFilter(request, response);

            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);

    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Constans.AUTH_HEADER);
        if (token != null) {
            String userToken = JWT.require(Algorithm.HMAC512(Constans.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(Constans.TOKEN_PREFIX, ""))
                    .getSubject();

            if (userToken != null) {
                return new UsernamePasswordAuthenticationToken(userToken, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }


    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headersNames = request.getHeaderNames();

        while (headersNames.hasMoreElements()) {
            String key = headersNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        map.forEach((k, v) -> System.out.println(k + ": " + v));
        return map;
    }


}
