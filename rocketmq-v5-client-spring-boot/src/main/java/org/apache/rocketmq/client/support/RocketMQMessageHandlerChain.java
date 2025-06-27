package org.apache.rocketmq.client.support;

import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2020
 *
 * @Description:
 * @Author: 梁建军
 * @Date: 2025-06-27 18:02
 */
public interface RocketMQMessageHandlerChain {
    ConsumeResult consume(MessageView message);
}
