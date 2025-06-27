package org.apache.rocketmq.spring.support;

import org.apache.rocketmq.common.message.MessageExt;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2020
 *
 * @Description:
 * @Author: 梁建军
 * @Date: 2025-06-27 18:02
 */
public interface RocketMQMessageHandlerChain {
    void consume(MessageExt message) throws Exception;
}
