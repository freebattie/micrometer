package com.example.micrometer;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cloudwatch.endpoints.internal.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.math.BigDecimal.valueOf;

@RestController
public class TimerController implements ApplicationListener<ApplicationReadyEvent> {

    private Map<String, Integer> theBank = new HashMap();
    private MeterRegistry meterRegistry;

    @GetMapping(path = "/count")

    public String count() {
        Random rand = new Random();
        theBank.put("test",rand.nextInt(10));
        theBank.put("test2",rand.nextInt(99));
        // Increment a metric called "transfer" every time this is called, and tag with from- and to country
        meterRegistry.counter("update_account").increment();
        return "yes";
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Gauge.builder("incoming_count", theBank,
                b -> b.values().size()).register(meterRegistry);
    }
}
