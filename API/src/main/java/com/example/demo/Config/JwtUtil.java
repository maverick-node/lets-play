package com.example.demo.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.UserRepo;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtUtil {

    private final UserRepo userRepo;

    public JwtUtil(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "zone01secretkey_jwt_for_testssss".getBytes());

    public String generateToken(String username, String userUuid) {
        return Jwts.builder()
                .setSubject(username) // 👈 KEEP IT
                .claim("uuid", userUuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

 public String extractUsername(String token) {
    if (token == null || token.isEmpty()) {
        return "Error";
    }

    Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();

    String username = claims.getSubject();
    String uuidFromToken = claims.get("uuid", String.class);

    RegisterEntity foundUser = userRepo.findByUsername(username)
            .orElseThrow(() -> new SecurityException("User not found"));

    if (!foundUser.getuserUuid().equals(uuidFromToken)) {
        throw new SecurityException("Invalid token");
    }

    return username;
}


}