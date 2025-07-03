package com.tcs.hr;

import io.jsonwebtoken.*;
import javax.servlet.http.*;
import java.util.*;

public final class JwtUtil {
    private JwtUtil() {}                                    // utility class

    /** Pull the JWT from ① Authorization header or ② HttpSession. */
    public static Optional<String> extractToken(HttpServletRequest req) {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            return Optional.of(h.substring(7));
        }
        HttpSession session = req.getSession(false);
        if (session != null) {
            String t = (String) session.getAttribute("token");
            if (t != null) return Optional.of(t);
        }
        return Optional.empty();
    }

    /** Verify the token with the *same* secret key used in LoginServlet. */
    public static Jws<Claims> verify(String token) throws JwtException {
        return Jwts.parser()                          // OK for JJWT < 0.12; use parserBuilder() for newer
                   .setSigningKey(LoginServlet.SECRET_KEY)
                   .parseClaimsJws(token);
    }
}
