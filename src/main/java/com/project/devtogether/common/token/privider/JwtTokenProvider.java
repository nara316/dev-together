package com.project.devtogether.common.token.privider;

import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.error.MemberErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.security.user.CustomUserDetail;
import com.project.devtogether.common.token.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenProvider {

    @Value("${{ secrets.TOKEN_SECRET_KEY }}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessPlusHour;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto issueToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetail.getId();
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessPlusHour);
        java.util.Date expiredAt = java.sql.Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("userId", userId)
                .claim("auth", authorities)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.of(
                accessToken,
                expiredLocalDateTime
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {

            if (e instanceof SignatureException) {
                throw new ApiException(MemberErrorCode.TOKEN_NOT_EXIST);
            } else if (e instanceof ExpiredJwtException) {
                throw new ApiException(MemberErrorCode.TOKEN_IS_EXPIRED);
            } else {
                throw new ApiException(ErrorCode.SERVER_ERROR);
            }
        }
    }

    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //userDetails 객체를 만들어서 Authentication 리턴
        CustomUserDetail userDetails = new CustomUserDetail(
                claims.getSubject(),
                "",
                authorities,
                Long.valueOf((Integer) claims.get("userId"))
        );
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(MemberErrorCode.TOKEN_IS_EXPIRED);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
