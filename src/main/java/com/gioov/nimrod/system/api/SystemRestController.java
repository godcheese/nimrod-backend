package com.gioov.nimrod.system.api;

import com.gioov.common.util.ColorUtil;
import com.gioov.common.util.ImageUtil;
import com.gioov.common.util.RandomUtil;
import com.gioov.common.util.ResourceUtil;
import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.operationlog.OperationLog;
import com.gioov.nimrod.common.operationlog.OperationLogType;
import com.gioov.nimrod.system.System;
import com.gioov.nimrod.system.entity.OperationLogEntity2;
import com.gioov.nimrod.system.service.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@RestController
@RequestMapping(value = System.Api.SYSTEM, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SystemRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRestController.class);

    public static final String VERIFY_CODE_NAME = "verifyCode";

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取验证码
     */
    @OperationLog(value = "获取验证码", type = OperationLogType.API)
    @GetMapping(value = "/verify_code")
    public void verifyCode(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws BaseResponseException {
        long expireIn = Long.valueOf((String) dictionaryService.get("VERIFY_CODE", "EXPIRE_IN"));
        boolean yawp = Boolean.valueOf((String) dictionaryService.get("VERIFY_CODE", "YAWP"));
        int stringLength = Integer.valueOf((String) dictionaryService.get("VERIFY_CODE", "STRING_LENGTH"));
        int interLine = Integer.valueOf((String) dictionaryService.get("VERIFY_CODE", "INTER_LINE"));
        String hexBackgroundColor = String.valueOf(dictionaryService.get("VERIFY_CODE", "HEX_BACKGROUND_COLOR"));
        stringLength = (stringLength >= 3 && stringLength <= 8) ? stringLength : 4;
        interLine = (interLine >= 1 && interLine <= 8) ? interLine : 0;
        expireIn = (expireIn >= 20) ? expireIn : 60;
        hexBackgroundColor = hexBackgroundColor.length() == 7 ? hexBackgroundColor : "#147cd3";
        ImageUtil.VerifyCodeImage verifyCodeImage;
        try {
            URL url =  ResourceUtil.getResource("/fonts/Arial.ttf");
            File file = new File(url.getFile());
            if(!file.exists()) {
                throw new BaseResponseException("字体文件不存在");
            }
            verifyCodeImage = ImageUtil.createVerifyCodeImage(114, 40, ColorUtil.getRGBColorByHexString(hexBackgroundColor), RandomUtil.randomString(stringLength, RandomUtil.NUMBER_LETTER), Color.WHITE, file, yawp, interLine, expireIn);

            httpServletResponse.addHeader("Pragma", "no-cache");
            httpServletResponse.addHeader("Cache-Control", "no-cache");
            httpServletResponse.addHeader("Expires", "0");
            // 生成验证码，写入用户session
            httpServletRequest.getSession().setAttribute(VERIFY_CODE_NAME, verifyCodeImage);
            // 输出验证码给客户端
            httpServletResponse.setContentType("image/jpeg");
            ImageIO.write(verifyCodeImage.getBufferedImage(), "jpg", httpServletResponse.getOutputStream());

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            throw new BaseResponseException("验证码生成发生错误");
        }

    }

    /**
     * 获取系统信息
     */
    @OperationLog(value = "获取系统信息", type = OperationLogType.API)
    @GetMapping(value = "/system_info")
    public ResponseEntity<Map<String, String>> systemInfo(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws BaseResponseException {
        Map<String, String> map = new HashMap<>(1);

//        java.util.Map<String,String> envMap = java.lang.System.getenv();
//        for(Map.Entry entry: envMap.entrySet()) {
//            map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
//        }
//        Set<String> names = java.lang.System.getProperties().stringPropertyNames();
//        for(String name: names) {
//            map.put(name, java.lang.System.getProperty(name));
//        }

        map.put("osName", java.lang.System.getProperty("os.name"));
        map.put("osVersion", java.lang.System.getProperty("os.version"));
        map.put("osArch", java.lang.System.getProperty("os.arch"));
        map.put("javaHome", java.lang.System.getProperty("java.home"));
        map.put("javaVersion", java.lang.System.getProperty("java.version"));

        /**
         *
         *  "user.timezone": "Asia/Shanghai",
         *     "os.name": "Mac OS X",
         *      "os.version": "10.14.5",
         *      "os.arch": "x86_64",
         *     "java.home": "/Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home/jre",
         *           "java.version": "1.8.0_192",
         */


        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
