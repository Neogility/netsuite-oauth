package com.neogility.suitetalk.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;


@RestController
public class QueryController {

    private final WebClient webClient;
    
    @Value("${netsuite.account.id}")
    private String companyId;

    public QueryController(@Autowired WebClient webClient){
        this.webClient = webClient;
    }
    
    @GetMapping(path="/netsuite/q", produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String,Object>> query(){
        String jsonBody = "{ "+
            "\"q\": \"SELECT id, companyName, email, dateCreated FROM customer WHERE dateCreated >= '1/1/2022' AND dateCreated < '1/1/2023'\" "+
        "}";
        System.out.println("jsonBody: "+jsonBody);
        Map<String, Object> jsonMap = new HashMap<>();
        Map response = webClient.post()
        .uri("https://"+companyId+".suitetalk.api.netsuite.com/services/rest/query/v1/suiteql?limit=5")
        .header("prefer", "transient").accept(MediaType.APPLICATION_JSON)
        .bodyValue(jsonBody).exchangeToMono(clientResponse->{
            return clientResponse.bodyToMono(Map.class);
        }).block();
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping(path="/netsuite/introspect", produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, ?>> introspect(@RegisteredOAuth2AuthorizedClient("netsuite") OAuth2AuthorizedClient authorizedClient) {
    	
    	String token = authorizedClient.getAccessToken().getTokenValue();
        ResponseEntity<Map<String,?>> response = webClient.post()
        .uri("https://"+companyId+".suitetalk.api.netsuite.com/services/rest/auth/oauth2/v1/introspect")
        .header("prefer", "transient").accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromFormData("token",token))
        .exchangeToMono(clientResponse->{
            return clientResponse.toEntity(new ParameterizedTypeReference<Map<String,?>>(){});
        }).block();
        System.out.println(response);	
        return response;
    }

}
