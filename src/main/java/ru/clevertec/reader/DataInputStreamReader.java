package ru.clevertec.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.exception.InputStreamException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DataInputStreamReader {

    public String getString(InputStream inputStream) {
        try(InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("DATA INPUT STREAM READER - SERVICE IO EXCEPTION.");
            throw new InputStreamException(e);
        } finally {
            closeInputStream(inputStream);
        }
    }

    private void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            log.error("DATA INPUT STREAM READER - CAN NOT CLOSE INPUT STREAM: " + e.getMessage());
        }
    }
}
