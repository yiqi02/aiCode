package com.yy.aicode.ai;


import com.yy.aicode.utils.SpringContextUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI代码生成类型路由服务工厂
 *
 * @author yupi
 */
@Slf4j
@Configuration
public class AiCodeGenTypeRoutingServiceFactory {


//    /**
//     * 创建AI代码生成类型路由服务实例
//     */
//    public AiCodeGenTypeRoutingService createAiCodeGenTypeRoutingService() {
//        ChatModel chatModel = SpringContextUtil.getBean("routingChatModelPrototype", ChatModel.class);
//        return AiServices.builder(AiCodeGenTypeRoutingService.class)
//                .chatModel(chatModel)
//                .build();
//    }

    private final ObjectProvider<ChatModel> routingChatModelProvider;

    public AiCodeGenTypeRoutingServiceFactory(
            @Qualifier("routingChatModelPrototype") ObjectProvider<ChatModel> routingChatModelProvider
    ) {
        this.routingChatModelProvider = routingChatModelProvider;
    }

    /**
     * 每次调用都创建一个新的 AI 路由服务实例
     * 适合并发场景
     */
    public AiCodeGenTypeRoutingService createAiCodeGenTypeRoutingService() {
        ChatModel chatModel = routingChatModelProvider.getObject();

        return AiServices.builder(AiCodeGenTypeRoutingService.class)
                .chatModel(chatModel)
                .build();
    }

    /**
     * 默认提供一个 Bean
     */
    @Bean
    public AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService() {
        return createAiCodeGenTypeRoutingService();
    }
}