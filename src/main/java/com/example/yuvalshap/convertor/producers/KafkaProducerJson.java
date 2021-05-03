package com.example.yuvalshap.convertor.producers;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static com.example.yuvalshap.convertor.config.KafkaTopicConfig.TOPIC;

@Component
public class KafkaProducerJson {

//    @Autowired
//    private NewTopic topic1;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, String>> kafkaCall = //
            kafkaTemplate.send( TOPIC,msg);
        SendResult<String, String> resp = kafkaCall.get(); //
        System.out.println("msg = " + resp); //
    }

}
