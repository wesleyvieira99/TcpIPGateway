package br.com.gatewaytcp;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ApiController {

    @PostMapping("/test-gateway")
    public String handleRequest(@RequestBody String requestPayload) {
        // Simulate processing and generating a response
        String response = "Dados da response: " + requestPayload;
        return response;
    }
}