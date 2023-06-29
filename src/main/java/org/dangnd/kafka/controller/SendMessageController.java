package org.dangnd.kafka.controller;

import org.dangnd.kafka.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/messages")
public class SendMessageController {

    @Autowired
    private MessagingService messagingService;

    @PostMapping
    public String send(@RequestBody Map<String, String> body) {
        int times = Integer.parseInt(body.get("times"));
        if (times == 0) times = 5;
        boolean samecontent = Boolean.parseBoolean(body.get("samecontent"));
        for (int i = 0; i < times; i++) {
            if (!samecontent) {
                body.put("content","Message " + i);
            } else {
                body.put("content","Message ");
            }
            messagingService.postMessageToKafka(body);
        }
        return "Ok";
    }
}
