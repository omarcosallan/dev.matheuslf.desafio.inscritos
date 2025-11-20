package dev.matheuslf.desafio.inscritos.dto.error;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String path;
    private HttpStatus error;
    private int statusCode;

    private Map<String, Object> properties;

    public ErrorResponse(LocalDateTime timestamp, String message, String path, HttpStatus error, int statusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
        this.error = error;
        this.statusCode = statusCode;
    }

    public void setProperty(String key, Object value) {
        this.properties = this.properties != null ? this.properties : new LinkedHashMap<>();
        this.properties.put(key, value);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}
