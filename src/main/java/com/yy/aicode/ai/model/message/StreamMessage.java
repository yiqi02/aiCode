package com.yy.aicode.ai.model.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 流式消息响应基类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {


    /**
     * 消息类型
     */
    private String type;
}
