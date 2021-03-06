package com.bookxchange.service;

import com.bookxchange.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApplicationUtils {

    private static JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ApplicationUtils(JwtTokenUtil jwtTokenUtil) {
        ApplicationUtils.jwtTokenUtil = jwtTokenUtil;
    }


    public static String getUserFromToken(String token) {
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token.substring(7));
        return claims.get("userUUID").toString();

    }

    public static boolean  checkGrade(Integer grade) {
        Pattern pattern = Pattern.compile("^[0-5]*$");
        Matcher matcher = pattern.matcher(grade.toString());

        return matcher.matches();
    }


}
