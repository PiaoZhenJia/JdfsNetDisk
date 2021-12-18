package com.company.jdfs.controller;

import com.company.app.common.base.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Api(tags = "杂项工具控制类")
@RestController
@RequestMapping("/misc")
public class MiscController {

    @PostMapping("/base64Encode")
    public R<String> base64Encode(String str) {
        return new R().setDataValue(Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)));
    }

    @PostMapping("/base64Decode")
    public R<String> base64Decode(String str) {
        return new R().setDataValue(new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8));
    }

    @PostMapping("/base64UrlEncode")
    public R<String> base64UrlEncode(String str) {
        return new R().setDataValue(Base64.getUrlEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)));
    }

    @PostMapping("/base64UrlDecode")
    public R<String> base64UrlDecode(String str) {
        return new R().setDataValue(new String(Base64.getUrlDecoder().decode(str), StandardCharsets.UTF_8));
    }

}
