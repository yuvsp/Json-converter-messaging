package com.example.yuvalshap.convertor.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.yuvalshap.convertor.config.KafkaConsumerConfig.GROUP_ID;
import static com.example.yuvalshap.convertor.config.KafkaTopicConfig.TOPIC;
import static com.example.yuvalshap.convertor.controller.JsonToXmlConverterController.xmlMapper;

@Component
public class KafkaConsumer {


    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listenGroupFoo(String xmlMsg) throws JsonProcessingException {
        System.out.println("Consumed Message from KAFKA [group: "+GROUP_ID+"] - "+ LocalDateTime.now() +": \n\t" + xmlMsg);

        JsonNode nowItsJson = xmlMapper.readValue(xmlMsg, JsonNode.class);
        System.out.println("when parsed to XML - value is: \n"+nowItsJson.toPrettyString());

    }

}
