package com.example.demo;

import com.example.demo.api.CartApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Sanity test to validate Spring Application Context is
 * being created during test phase.
 */
@SpringBootTest
public class DemoApplicationSmokeTest {

    @Autowired
    private CartApi cartApi;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(cartApi);
    }
}
