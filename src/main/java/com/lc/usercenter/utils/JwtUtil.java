package com.lc.usercenter.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类（适配 JJwt 0.12.3 版本）
 */
public class JwtUtil {

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return 加密后的token
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 1. 创建符合HS256要求的密钥（0.12.x版本要求密钥长度至少256位）
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 2. JWT的过期时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 3. 构建JWT（适配0.12.x的新API）
        JwtBuilder builder = Jwts.builder()
                .claims(claims) // 替换原setClaims方法
                .expiration(exp) // 设置过期时间
                .signWith(key); // 替换原signWith方法，无需指定算法（密钥已隐含算法）

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return 解密后的token
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 1. 创建验证用的密钥
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 2. 解析JWT（适配0.12.x的新API）
        return Jwts.parser()
                .verifyWith(key) // 替换原setSigningKey方法
                .build() // 必须调用build()创建JwtParser实例
                .parseSignedClaims(token) // 替换原parseClaimsJws方法
                .getPayload(); // 替换原getBody方法
    }
}