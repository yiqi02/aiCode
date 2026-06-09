import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
    public class ConfigTest {
        @Value("${langchain4j.open-ai.streaming-chat-model.base-url}")
        private String baseUrl;

        @Test
        void printBaseUrl() {
            System.out.println("baseUrl = " + baseUrl);
        }
    }