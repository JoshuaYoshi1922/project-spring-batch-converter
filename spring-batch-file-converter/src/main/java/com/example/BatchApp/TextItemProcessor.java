package com.example.BatchApp;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TextItemProcessor  implements ItemProcessor<String, String> { //probably could delete this class
    @Override
    public String process(String message) throws Exception {

        return message;
    }
}
