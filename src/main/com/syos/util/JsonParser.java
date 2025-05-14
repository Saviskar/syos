package main.com.syos.util;

import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;

public class JsonParser {
    private static final ObjectMapper M = new ObjectMapper();

    public static <T> T fromJson(Reader r, Class<T> cls) throws IOException {
        return M.readValue(r, cls);
    }

    public static void toJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.setContentType("application/json");
        M.writeValue(resp.getWriter(), obj);
    }
}
