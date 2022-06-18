package org.wrh.cloud.gateway.utils;


// import com.sun.org.apache.xpath.internal.operations.String;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

/**
 * @author wuruohong
 * @date 2022-06-18 21:48
 */
public class JwtUtils {

    public static String priKey1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK83vH4Ff8UxrJTu8CzYV4qgImKZCq82T6hPgNsqfG+wv4Am9EaC1Z/i2ijgrwrnh8RxQen54XZS2WJk3M8cwbJc6BYMqwLKvDkr1hlM7IYaJdj43Va/1qSTewxXVieC8yYHAfiD/XlvPK1YYnW8E7jx7Igwn3c0NaEhiOH63g3fAgMBAAECgYBHz+QlEkPnohBFihhNiO14F2GAX5ENdoj3Rn5dVPBjJmGWOGDtdTnwqZ0YG94h7fqq/HAzYQKS6CnINeZ5zCNmNRxc4qGfhiqkDLaU7kjFi5bdbHHwUkip+hX+02VZ+rKOkx3l0ky7Fj1dPASw7OemvzWIa4JTfhMsIwfCI5dtAQJBANk2UsmVMYiUdmR1NX5txiaO3Ti2sKtFUVhtKQiq7pXNSCzqttvaql0c3eakRsj3bGEUz+XNVcckcfIQI2Re7v8CQQDOgat7hbpPXRMchYLP2fFuuaDLU8eggpGc4TLIIQLd2vEM3fKf6kIjPMoOQpPiwAFSFqpeAkpKbO0/rNkqjcEhAkEAwS+/kepPk9n3MoHMghXAFqsZtkdF+g48uxjbVgoxCKr7dneLCx8ARrBV67xP+I2WpwGLpidqEyQm89PGpo6IoQJAU++k0epDFisQr5Ec4HsHhSfyUXLWO0mXAhsO1wWD8sUhIUe9bH59L3Fv92fcUFgWsVUBHoDzzViE/lK1WnSPwQJAeJWkY0fnQm0fN2J88creGWCKxQBImjn+YBdksxN3Qg0/tAImoP8stiDw7kPX74zNkU+s+ZUJOICMCgwhVBE+BQ==";
    // public static String priKey = "tn3MoHMghXAFqsZtkdF+g48uxjbVgoxCKr7dneLCx8ARrBV67xP+I2WpwGLpidqEyQm89PGpo6IoQJAU++k0epDFisQr5Ec4HsHhSfyUXLWO0mXAhsO1wWD8sUhIUe9bH59L3Fv92fcUFgWsVUBHoDzzViE/lK1WnSPwQJAeJWkY0fnQm0fN2J88creGWCKxQBImjn+YBdksxN3Qg0/tAImoP8stiDw7kPX74zNkU+s+ZUJOICMCgwhVBE+BQ==";

    /*public static String getToken(Integer uid, int exp) {
        long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
        return Jwts.builder().setSubject(String.valueOf(uid)).setExpiration(new Date(endTime)).signWith(SignatureAlgorithm.HS512, priKey).compact();
    }*/

    /**
     * 利用jwt生成token信息.
     * @param claims 数据声明（Claim）其实就是一个Map，比如我们想放入用户名，
     *               可以简单的创建一个Map然后put进去
     * @param secret 用于进行签名的秘钥
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claims,String secret) throws Exception {
//设置过期时间为10分钟
        Date ecpiration = new Date(System.currentTimeMillis()+600000L);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(ecpiration)
                .signWith(SignatureAlgorithm.HS512, secret) //采用什么算法是可以自己选择的，不一定非要采用HS512
                .compact();
    }

    public static String generateToken(Map<String, Object> claims,Key secret) throws Exception {
//设置过期时间为10分钟
        Date ecpiration = new Date(System.currentTimeMillis()+600000L);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(ecpiration)
                .signWith(SignatureAlgorithm.HS512, secret) //采用什么算法是可以自己选择的，不一定非要采用HS512
                .compact();
    }

    /**
     * 利用jwt解析token信息.
     * @param token 要解析的token信息
     * @param secret 用于进行签名的秘钥
     * @return
     * @throws Exception
     */
    public static Optional<Claims> getClaimsFromToken(String token,String secret) throws Exception {
        Claims claims;
        // DESCoder desCoder = new DESCoder();
        // Key key = desCoder.toKey(secret);
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (ExpiredJwtException e) {
            /** 代表已经过期可返回错误码之类的*/
            return Optional.empty();
        } catch (SignatureException e) {
            /** 说明是伪造的非法请求*/
            return Optional.empty();
        } catch (Exception e) {
            /** 非法请求*/
            return Optional.empty();
        }
    }

    public static Optional<Claims> getClaimsFromToken(String token,Key secret) throws Exception {
        Claims claims;
        // DESCoder desCoder = new DESCoder();
        // Key key = desCoder.toKey(secret);
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (ExpiredJwtException e) {
            /** 代表已经过期可返回错误码之类的*/
            return Optional.empty();
        } catch (SignatureException e) {
            /** 说明是伪造的非法请求*/
            return Optional.empty();
        } catch (Exception e) {
            /** 非法请求*/
            return Optional.empty();
        }
    }

    /**
     * 验证token是否过期
     * @param tooken 要解析的token信息
     * @param secret 用于进行签名的秘钥
     * @return true 表示过期，false表示不过期，如果没有设置过期时间，则也不认为过期
     * @throws Exception
     */
    public static boolean isExpired(String tooken,String secret) throws Exception{
        Optional<Claims> claims= getClaimsFromToken(tooken,secret);
        if(claims.isPresent()){
            Date expiration = claims.get().getExpiration();
            return expiration.before(new Date());
        }
        return false;
    }

    public static boolean isExpired(String tooken,Key secret) throws Exception{
        Optional<Claims> claims= getClaimsFromToken(tooken,secret);
        if(claims.isPresent()){
            Date expiration = claims.get().getExpiration();
            return expiration.before(new Date());
        }
        return false;
    }

    /**
     * 获取tooken中的参数值
     * @param token 要解析的token信息
     * @param secret 用于进行签名的秘钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> extractInfo(String token,String secret) throws Exception{
        Optional<Claims> claims = getClaimsFromToken(token,secret);
        if(claims.isPresent()){
            Map<String,Object> info = new HashMap<String,Object>();
            Set<String> keySet = claims.get().keySet();
            //通过迭代，提取token中的参数信息
            Iterator<String> iterator = keySet.iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value =  claims.get().get(key);
                info.put(key,value);

            }
            return info;
        }
        return null;
    }

    public static Map<String,Object> extractInfo(String token,Key secret) throws Exception{
        Optional<Claims> claims = getClaimsFromToken(token,secret);
        if(claims.isPresent()){
            Map<String,Object> info = new HashMap<String,Object>();
            Set<String> keySet = claims.get().keySet();
            //通过迭代，提取token中的参数信息
            Iterator<String> iterator = keySet.iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                Object value =  claims.get().get(key);
                info.put(key,value);

            }
            return info;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        // String priKey1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK83vH4Ff8UxrJTu8CzYV4qgImKZCq82T6hPgNsqfG+wv4Am9EaC1Z/i2ijgrwrnh8RxQen54XZS2WJk3M8cwbJc6BYMqwLKvDkr1hlM7IYaJdj43Va/1qSTewxXVieC8yYHAfiD/XlvPK1YYnW8E7jx7Igwn3c0NaEhiOH63g3fAgMBAAECgYBHz+QlEkPnohBFihhNiO14F2GAX5ENdoj3Rn5dVPBjJmGWOGDtdTnwqZ0YG94h7fqq/HAzYQKS6CnINeZ5zCNmNRxc4qGfhiqkDLaU7kjFi5bdbHHwUkip+hX+02VZ+rKOkx3l0ky7Fj1dPASw7OemvzWIa4JTfhMsIwfCI5dtAQJBANk2UsmVMYiUdmR1NX5txiaO3Ti2sKtFUVhtKQiq7pXNSCzqttvaql0c3eakRsj3bGEUz+XNVcckcfIQI2Re7v8CQQDOgat7hbpPXRMchYLP2fFuuaDLU8eggpGc4TLIIQLd2vEM3fKf6kIjPMoOQpPiwAFSFqpeAkpKbO0/rNkqjcEhAkEAwS+/kepPk9n3MoHMghXAFqsZtkdF+g48uxjbVgoxCKr7dneLCx8ARrBV67xP+I2WpwGLpidqEyQm89PGpo6IoQJAU++k0epDFisQr5Ec4HsHhSfyUXLWO0mXAhsO1wWD8sUhIUe9bH59L3Fv92fcUFgWsVUBHoDzzViE/lK1WnSPwQJAeJWkY0fnQm0fN2J88creGWCKxQBImjn+YBdksxN3Qg0/tAImoP8stiDw7kPX74zNkU+s+ZUJOICMCgwhVBE+BQ==";


        Key priKey = new SecretKeySpec("MIICdkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK83vH4Ff8UxrJTu8CzYV4qgImKZCq82T6hPgNsq".getBytes(),SignatureAlgorithm.HS512.getJcaName());
        System.out.println("priKey.getAlgorithm() = " + priKey.getAlgorithm());
        System.out.println("priKey.getFormat() = " + priKey.getFormat());
        System.out.println("priKey.getClass() = " + priKey.getClass());
        // String keyStr = priKey.toString();
        String str = new String(priKey.getEncoded());
        System.out.println("str = " + str);

        Key priKey1 = priKey;


        // System.out.println("getToken(1, 10000000) = " + getToken(1, 10000000));
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 123);
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            generateToken(map, priKey1);
        }
        long start2 = System.currentTimeMillis();
        System.out.println("generate start2-start1 = " + (start2 - start1));
        String token = generateToken(map, priKey1);
        System.out.println("token = " + token+ "length: " + token.length());

        start1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            getClaimsFromToken(token, priKey1);
        }
        start2 = System.currentTimeMillis();
        System.out.println("decode start2-start1 = " + (start2 - start1));

        Optional<Claims> optionalClaims = getClaimsFromToken(token, priKey1);
        Claims claims = optionalClaims.get();
        Object userId = claims.get("userId");
        System.out.println("userId = " + userId);

        Map<String, Object> stringObjectMap = extractInfo(token, priKey1);
        System.out.println("stringObjectMap = " + stringObjectMap);
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis()/1000);


    }
}
