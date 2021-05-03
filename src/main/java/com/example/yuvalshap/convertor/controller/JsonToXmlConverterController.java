package com.example.yuvalshap.convertor.controller;


import com.example.yuvalshap.convertor.producers.KafkaProducerJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("converters")
public class JsonToXmlConverterController {

    private Map<Long, JsonNode> jsonMap = new HashMap<>();
    private Map<Long, String> xmlMap = new HashMap<>();

    @Autowired
    private KafkaProducerJson kafkaProducer;

    long counter = 0l;

    private ObjectMapper jsonMapper = new ObjectMapper();
    public final static XmlMapper xmlMapper = new XmlMapper();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @PostMapping("/")
    ResponseEntity<String> save(@RequestBody JsonNode info)
            throws JsonProcessingException,
            ExecutionException, InterruptedException
    {
        System.out.println("Received REST req - "+ LocalDateTime.now() +": \n\t"+ info);

        jsonMap.put(++counter, info);
        String infoAsXml = xmlMapper.writeValueAsString(info);
        xmlMap.put(counter, infoAsXml);

        kafkaProducer.sendMessage(infoAsXml);

//        String ans = null;
//        while (ans != null/*lstener - havent got the msg*/){
//            ans = xmlMap.get(counter);
//            Thread.sleep(500);
//        }


        return ResponseEntity.ok(infoAsXml);
    }

    @GetMapping("/")
    Map<Long, String> getAll(){
        return xmlMap;
    }

    @GetMapping("/{id}")
    String get(@PathVariable Long id ){
        return xmlMap.get(id);
    }

    @DeleteMapping("/{id}")
    String delete(@PathVariable Long id ){
         jsonMap.remove(id);
         return xmlMap.remove(id);

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @GetMapping("/populate")
    public void populate() {
        try {
            //adding intial jsons (1 2 3)
           save(jsonMapper.readTree("\"{}\"") ) ;
           save(jsonMapper.readTree("{}") ) ;
           save(jsonMapper.readTree("{\"topic\": \"digital-events\", \"offset\": 9249, \"partition\": 5}") );

            //XML parse example
            class SimpleBean {
                private int x = 1;
                private int y = 2;

                //standard setters and getters
                public int getX() { return x; }
                public void setX(int x) { this.x = x; }
                public int getY() { return y; }
                public void setY(int y) { this.y = y; }
            }

            String xml = xmlMapper.writeValueAsString(new SimpleBean()); // OBJECT => XML
            System.out.println(xml);

        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}