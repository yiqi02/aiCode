package com.yy.aicode.ai;

/*
*使用工厂模式的设计模式
* 为ai服务创建工厂 使用时创建对象
 */

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yy.aicode.ai.guardrail.PromptSafetyInputGuardrail;
import com.yy.aicode.ai.guardrail.RetryOutputGuardrail;
import com.yy.aicode.ai.tools.FileWriteTool;
import com.yy.aicode.ai.tools.ToolManager;
import com.yy.aicode.exception.BusinessException;
import com.yy.aicode.exception.ErrorCode;
import com.yy.aicode.model.entity.enums.CodeGenTypeEnum;
import com.yy.aicode.service.ChatHistoryService;
import com.yy.aicode.utils.SpringContextUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import com.yy.aicode.ai.tools.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class AiGeneratorServiceFactory {

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;

    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolManager toolManager;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存键: {}, 原因: {}", key, cause);
            })
            .build();
    //1 => AiService  appId = 1
    //2 => AiService  appId = 2


    /**
     * 根据 appId 获取服务
     * 有缓存直接取 没有缓存调用方法创建
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId,CodeGenTypeEnum.HTML);
    }

    /**
     * 根据 appId 获取服务
     *
     * @param appId       应用 id
     * @param codeGenType 生成类型
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        String cacheKey = buildCacheKey(appId, codeGenType);
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenType));
    }


    /**
     * 创建新的 AI 服务实例
     *
     * @param appId       应用 id
     * @param codeGenType 生成类型
     * @return
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenType) {
        log.info("为 appId: {} 创建新的 AI 服务实例", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        //从数据库中加载对话历史到记忆中
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
        return switch (codeGenType) {
            // Vue 项目生成，使用工具调用和推理模型
            case VUE_PROJECT -> {

                //多例模式的chatmodel解决并发问题
                StreamingChatModel reasoningStreamingChatModel = SpringContextUtil.getBean("reasoningStreamingChatModelPrototype",StreamingChatModel.class);

                yield AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(reasoningStreamingChatModel)
                    .chatMemoryProvider(memoryId -> chatMemory)
//                    .tools(new FileWriteTool())
                    .tools(toolManager.getAllTools())
                    //处理工具调用幻觉问题
                    .hallucinatedToolNameStrategy(toolExecutionRequest ->
                            ToolExecutionResultMessage.from(toolExecutionRequest, "Error: there is no tool called " + toolExecutionRequest.name())
                    )
                        .maxSequentialToolsInvocations(20)   //最多调用20次工具
                        .inputGuardrails(new PromptSafetyInputGuardrail())  //添加输入护轨
//                        .outputGuardrails(new RetryOutputGuardrail()) 输出护轨会影响流式输出
                    .build();
            }
            // HTML 和 多文件生成，使用流式对话模型
            case HTML, MULTI_FILE -> {

                //使用多利模式的chatmodel解决并发问题
                StreamingChatModel openAiStreamingChatModel = SpringContextUtil.getBean("streamingChatModelPrototype",StreamingChatModel.class);

                yield AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(chatModel)
                    .streamingChatModel(openAiStreamingChatModel)
                    .chatMemory(chatMemory)
                    .build();
            }
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType.getValue());
        };
    }



    /**
     * 创建 AI 代码生成器服务
     *
     * @return
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return getAiCodeGeneratorService(0);
    }


    /**
     * 构造缓存键
     *
     * @param appId
     * @param codeGenType
     * @return
     */
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenType) {
        return appId + "_" + codeGenType.getValue();
    }
}
