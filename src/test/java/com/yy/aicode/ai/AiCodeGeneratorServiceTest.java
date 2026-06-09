package com.yy.aicode.ai;

import com.yy.aicode.ai.model.HtmlCodeResult;
import com.yy.aicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult Result = aiCodeGeneratorService.generateHtmlCode("做个博客，不超过50行");
        Assertions.assertNotNull(Result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult Result = aiCodeGeneratorService.generateMultiFileCode("做个网页，不超过50行");
        Assertions.assertNotNull(Result);
    }

}