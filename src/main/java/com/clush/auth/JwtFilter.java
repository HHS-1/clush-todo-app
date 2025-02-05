package com.clush.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clush.service.RedisTokenService;
import com.clush.util.CookieUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
    private JwtUtil jwtUtil;
	@Autowired
	private RedisTokenService redisTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 쿠키에서 JWT 액세스 토큰 추출
        String accessToken= CookieUtil.getCookie(request, "accessToken");
        // 토큰이 존재하고 유효하면 인증 처리
        if (accessToken != null) {
        	String userId = null;
        	long id = 0;
        	if(jwtUtil.isTokenExpired(accessToken)) {
        		String refreshToken = redisTokenService.getRefreshToken(accessToken);
        		if (refreshToken == null || jwtUtil.isTokenExpired(refreshToken)) {
                    //로그아웃 처리
        		}else {
                	userId = jwtUtil.extractUserId(refreshToken);
                	id = jwtUtil.extractId(refreshToken);
        			
        			String newAccessToken = jwtUtil.generateToken(userId, id);
        			String newRefreshToken = jwtUtil.generateRefreshToken(userId, id);
        			jwtUtil.saveCookie(response, newAccessToken);
        			redisTokenService.saveRefreshToken(newAccessToken, newRefreshToken, 3600000*24*10);
        		}
        	}else {
        		userId = jwtUtil.extractUserId(accessToken);
        		id = jwtUtil.extractId(accessToken);
        	}
        	

            // 인증 정보 생성
        	CustomUserDetails customUserDetails = new CustomUserDetails(id,userId);
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(customUserDetails, null, null); // CustomUserDetails를 principal로 사용
            
        	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            Map<String, Object> details = new HashMap<>();
            details.put("userId", userId);
            details.put("id", id);
            authentication.setDetails(details);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);  // 필터 체인에 요청 전달
    }
}
