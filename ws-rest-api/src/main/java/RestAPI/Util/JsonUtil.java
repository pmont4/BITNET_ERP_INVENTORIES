package RestAPI.Util;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.AccessLevel;
import lombok.Getter;

public class JsonUtil implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Getter(AccessLevel.PRIVATE)
    private static final ObjectMapper mapper;
    
    static {
        mapper = new ObjectMapper()
                .configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    
    public static <T> String getJsonString(T object) throws JsonProcessingException {
        return getMapper().writeValueAsString(object);
    }
    
    public static <T> T getFromJson(String json, TypeReference<T> type) throws JsonProcessingException {
        return getMapper().readValue(json, type);
    }
    
}
