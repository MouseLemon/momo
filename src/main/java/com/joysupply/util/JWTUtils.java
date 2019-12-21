package com.joysupply.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joysupply.entity.SystemUser;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {


    /**
     * 密钥
     */
    public final static String SECRET = "byoakeyscret";

    /**
     *
     * @param user
     * @param istDate 签发时间
     * @param expiresDate 过期时间
     * @return
     * @throws Exception
     */
        public static String createToken(SystemUser user, Date istDate, Date expiresDate) throws Exception {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    .withHeader(map)
                    .withClaim("userName", user.getUserName())
                    .withClaim("personCode", user.getPersonCode())
                    .withExpiresAt(expiresDate)
                    .withIssuedAt(istDate)
                    .sign(Algorithm.HMAC256(SECRET));

            return token;
        }

    /**
     * 验签
     * @param token
     * @return
     * @throws Exception
     */
        public static Map<String, Claim> verifyToken(String token) throws Exception{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = null;
            try {
                jwt = verifier.verify(token);
            } catch (Exception e) {
                throw new RuntimeException("凭证过期！");
            }
            return jwt.getClaims();
        }
    }

