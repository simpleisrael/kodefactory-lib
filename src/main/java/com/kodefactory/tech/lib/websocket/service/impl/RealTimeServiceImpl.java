package com.kodefactory.tech.lib.websocket.service.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.websocket.dto.RealTimeResponseDTO;
import com.kodefactory.tech.lib.websocket.service.RealTimeService;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RealTimeServiceImpl extends BaseServiceImpl implements RealTimeService {

    private SimpMessagingTemplate template;
    private MappingJackson2MessageConverter messageConverter;


    public RealTimeServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
        this.messageConverter = new MappingJackson2MessageConverter();
        this.template.setMessageConverter(messageConverter);
    }

    @Override
    public void publish(String type, Object payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            RealTimeResponseDTO responseDTO = new RealTimeResponseDTO(type, payload);
            String jsonString = objectMapper.writeValueAsString(responseDTO);
            template.convertAndSend("/topic/data", jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
