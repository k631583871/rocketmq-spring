package org.apache.rocketmq.spring.support;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2020
 *
 * @Description:
 * @Author: 梁建军
 * @Date: 2025-06-27 19:48
 */
public class RocketMQMessagePostProcessorTest {

    @Test
    public void testMessagePostProcessor() {

        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(new DefaultMQProducer());
        rocketMQTemplate.setMessagePostProcessor(new MessagePostProcessor() {
            @Override
            public Message<?> postProcessMessage(Message<?> message) {
                throw new RuntimeException("postProcessMessage");
            }
        });
        try {
            rocketMQTemplate.syncSend("test", "payload");
        } catch (MessagingException e) {
            assertThat(e).hasMessageContaining("postProcessMessage");
        }
    }

    @Test
    public void testMessagePostProcessor2() {

        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(new DefaultMQProducer() {
            @Override
            public SendResult send(org.apache.rocketmq.common.message.Message msg, long timeout) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
                SendResult sendResult = new SendResult();
                if (Objects.equals(msg.getProperties().get("test"), "test")) {
                    sendResult.setSendStatus(SendStatus.SEND_OK);
                } else {
                    sendResult.setSendStatus(SendStatus.FLUSH_DISK_TIMEOUT);
                }
                return sendResult;
            }
        });
        rocketMQTemplate.setMessagePostProcessor(new MessagePostProcessor() {
            @Override
            public Message<?> postProcessMessage(Message<?> message) {
                MessageBuilder<?> builder = MessageBuilder.fromMessage(message);
                builder.setHeader("test", "test");
                return builder.build();
            }
        });
        SendResult sendResult = rocketMQTemplate.syncSend("test", "payload");
        assertEquals(SendStatus.SEND_OK, sendResult.getSendStatus());
    }
}