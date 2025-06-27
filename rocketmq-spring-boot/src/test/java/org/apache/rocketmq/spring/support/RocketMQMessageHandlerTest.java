package org.apache.rocketmq.spring.support;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2020
 *
 * @Description:
 * @Author: 梁建军
 * @Date: 2025-06-27 20:02
 */
public class RocketMQMessageHandlerTest {

    @Test
    public void testRocketMQMessageHandler() throws Exception {
        DefaultRocketMQListenerContainer listenerContainer = new DefaultRocketMQListenerContainer();

        listenerContainer.setApplicationContext(new GenericApplicationContext(){
            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return (T) listenerContainer;
            }
        });
        listenerContainer.setRocketMQMessageHandler(new RocketMQMessageHandler() {
            @Override
            public void doHandler(MessageExt message, RocketMQMessageHandlerChain chain) throws Exception {
                message.putUserProperty("test", "test");
                chain.doHandler(message);
            }
        });

        listenerContainer.setRocketMQListener(new RocketMQListener<MessageExt>() {
            @Override
            public void onMessage(MessageExt message) {
                assertEquals(message.getProperties().get("test"), "test");
            }
        });

        Field messageTypeField = DefaultRocketMQListenerContainer.class.getDeclaredField("messageType");
        messageTypeField.setAccessible(true);
        messageTypeField.set(listenerContainer,MessageExt.class);

        DefaultRocketMQListenerContainer.DefaultMessageListenerConcurrently concurrently =  listenerContainer.new DefaultMessageListenerConcurrently();
        MessageExt messageExt = new MessageExt();
        messageExt.getProperties();
        concurrently.consumeMessage(Arrays.asList(messageExt),null);
    }

    @Test
    public void testRocketMQMessageHandler1() throws Exception {
        DefaultRocketMQListenerContainer listenerContainer = new DefaultRocketMQListenerContainer();

        listenerContainer.setApplicationContext(new GenericApplicationContext(){
            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return (T) listenerContainer;
            }
        });
        listenerContainer.setRocketMQMessageHandler(new RocketMQMessageHandler() {
            @Override
            public void doHandler(MessageExt message, RocketMQMessageHandlerChain chain) throws Exception {
                message.putUserProperty("test", "test");
                chain.doHandler(message);
            }
        });

        listenerContainer.setRocketMQListener(new RocketMQListener<MessageExt>() {
            @Override
            public void onMessage(MessageExt message) {
                assertEquals(message.getProperties().get("test"), "test");
            }
        });

        Field messageTypeField = DefaultRocketMQListenerContainer.class.getDeclaredField("messageType");
        messageTypeField.setAccessible(true);
        messageTypeField.set(listenerContainer,MessageExt.class);

        DefaultRocketMQListenerContainer.DefaultMessageListenerOrderly concurrently =  listenerContainer.new DefaultMessageListenerOrderly();
        MessageExt messageExt = new MessageExt();
        messageExt.getProperties();
        concurrently.consumeMessage(Arrays.asList(messageExt),null);
    }

}