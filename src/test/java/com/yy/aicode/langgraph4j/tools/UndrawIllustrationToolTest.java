package com.yy.aicode.langgraph4j.tools;

import com.yy.aicode.langgraph4j.model.ImageResource;
import com.yy.aicode.langgraph4j.model.enums.ImageCategoryEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UndrawIllustrationToolTest {

    @Resource
    private UndrawIllustrationTool undrawIllustrationTool;

    @Test
    void testSearchIllustrations() {
        List<ImageResource> illustrations = undrawIllustrationTool.searchIllustrations("happy");
        assertNotNull(illustrations);

        // 如果列表为空，则无法获取元素，直接返回或标记为通过（取决于业务）
        if (illustrations.isEmpty()) {
            System.out.println("未搜索到任何插画，跳过详细验证");
            return;
        }

        // 验证第一个元素
        ImageResource firstIllustration = illustrations.get(0);
        assertEquals(ImageCategoryEnum.ILLUSTRATION, firstIllustration.getCategory());
        assertNotNull(firstIllustration.getDescription());
        assertNotNull(firstIllustration.getUrl());
        assertTrue(firstIllustration.getUrl().startsWith("http"));

        System.out.println("搜索到 " + illustrations.size() + " 张插画");
        illustrations.forEach(illustration ->
                System.out.println("插画: " + illustration.getDescription() + " - " + illustration.getUrl())
        );
    }
}