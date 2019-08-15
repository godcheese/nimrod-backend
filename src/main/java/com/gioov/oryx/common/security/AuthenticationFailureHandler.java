package com.gioov.oryx.common.security;

import com.gioov.tile.web.http.FailureEntity;
import com.gioov.oryx.common.Common;
import com.gioov.oryx.common.FailureMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@Component
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

    @Autowired
    private Common common;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = httpServletResponse.getWriter();

        // 检查 e 是否为验证码错误类
        if (e instanceof VerifyCodeFilter.VerifyCodeCheckException) {
            printWriter.write(common.objectToJson(new FailureEntity(e.getMessage())));
        } else if (e instanceof BadCredentialsException) {
            printWriter.write(common.objectToJson(new FailureEntity(FailureMessage.LOGIN_FAIL)));
        } else if (e instanceof DisabledException) {
            LOGGER.info("e.getClass={}", e.getClass());
            LOGGER.info("e.getMessage={}", e.getMessage());
            printWriter.write(common.objectToJson(new FailureEntity(FailureMessage.LOGIN_FAIL_USER_IS_DISABLED)));
        } else {
            printWriter.write(common.objectToJson(new FailureEntity(FailureMessage.LOGIN_FAIL)));
        }
        e.printStackTrace();
        printWriter.flush();
        printWriter.close();
    }

}