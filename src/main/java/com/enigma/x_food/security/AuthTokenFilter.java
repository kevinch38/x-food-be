//package com.enigma.x_food.security;
//
//import com.enigma.x_food.feature.admin.AdminService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class AuthTokenFilter extends OncePerRequestFilter {
//    private final JwtUtil jwtUtil;
//    private final AdminService adminService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        try {
//            String token = parseJwt(request);
//
//            if (token != null && jwtUtil.verifyJwtToken(token)) {
//                setAuthentication(request, token);
//            }
//        } catch (Exception e) {
//            log.error("Cannot set user authentication: {}", e.getMessage());
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private void setAuthentication(HttpServletRequest request, String token) {
//        JwtClaim claim = jwtUtil.getAdminInfoByToken(token);
//
//        UserDetails admin = adminService.getById(claim.getAdminId());
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                admin,
//                null,
//                admin.getAuthorities()
//        );
//
//        authenticationToken.setDetails(new WebAuthenticationDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    }
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7);
//        }
//        return null;
//    }
//}
