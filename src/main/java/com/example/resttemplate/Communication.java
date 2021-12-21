package com.example.resttemplate;

import com.example.resttemplate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
public class Communication {

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private final String URL = "http://91.241.64.178:7081/api/users";

    @Autowired
    public Communication(RestTemplate restTemplate, HttpHeaders headers) {
        this.headers = headers;
        this.headers.set("Cookie", String.join(";",
                Objects.requireNonNull(restTemplate.headForHeaders(URL).get("Set-Cookie"))));
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> response = restTemplate
                .exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>(){});
        System.out.println(response.getHeaders());
        return response.getBody();
    }

    public ResponseEntity<String> addUser() {
        User user = new User(3L, "James", "Brown", (byte) 15);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(URL, entity, String.class);
    }

    public ResponseEntity<String> updateUser() {
        User user = new User(3L, "Thomas", "Shelby", (byte) 15);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class, 3);
    }

    public ResponseEntity<String> deleteUser() {
        Map<String, Long> uriVariables = new HashMap<>() {
            {
                put("id", 3L);
            }
        };
        HttpEntity<User> entity = new HttpEntity<>(null, headers);
        String URL_DELETE = "http://91.241.64.178:7081/api/users/{id}";
        return restTemplate.exchange(URL_DELETE, HttpMethod.DELETE,entity, String.class, uriVariables);
    }

    public String allAnswer() {
        return addUser().getBody() +
                updateUser().getBody() +
                deleteUser().getBody();
    }
}
