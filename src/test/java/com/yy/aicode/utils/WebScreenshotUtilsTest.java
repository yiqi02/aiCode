package com.yy.aicode.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScreenshotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String testurl = "https://www.baidu.com";
        String webPageScreenshot = WebScreenshotUtils.saveWebPageScreenshot(testurl);
        Assertions.assertNotNull(webPageScreenshot);
    }
}