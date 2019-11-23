package fr.gnark.sound.port.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QueryTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        final String query = "{  \"query\": \"{getMusicEnums{notes degrees}}\", \"variables\": null }";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(query, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/ainur/graphql-api", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        final String body = responseEntity.getBody();
        assertTrue(body.contains("\"C\""));
    }
}