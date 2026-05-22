/**
 * SimpleServer.java - Main HTTP server that handles all API endpoints for user authentication, quiz questions, and result submission.
 * Uses Java's built-in HttpServer to create a REST API with CORS support and JSON request/response handling.
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/questions", new QuestionsHandler());
        server.createContext("/api/admin/add", new AddQuestionHandler());
        server.createContext("/api/admin/upload", new UploadHandler());
        server.createContext("/api/quiz/submit", new SubmitHandler());
        server.createContext("/api/admin/results", new ResultsHandler());
        
        server.setExecutor(null);
        System.out.println("SimpleServer started at http://localhost:8080");
        server.start();
    }

    static String readAll(InputStream is) throws IOException {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        byte[] b = json.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, b.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(b); }
    }

    static String extractString(String body, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(body);
        return m.find() ? m.group(1) : null;
    }

    static int extractInt(String body, String key, int def) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(body);
        return m.find() ? Integer.parseInt(m.group(1)) : def;
    }

    static List<String> extractStringArray(String body, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher m = p.matcher(body);
        List<String> out = new ArrayList<>();
        if (m.find()) {
            String inner = m.group(1);
            Pattern q = Pattern.compile("\"([^\"]*)\"");
            Matcher mm = q.matcher(inner);
            while (mm.find()) out.add(mm.group(1));
        }
        return out;
    }

    static class RegisterHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { 
                ex.sendResponseHeaders(405, -1); 
                return; 
            }
            String body = readAll(ex.getRequestBody());
            String username = extractString(body, "username");
            String password = extractString(body, "password");
            if (username == null || password == null) { 
                sendJson(ex, 400, "{\"success\":false,\"message\":\"missing\"}"); 
                return; 
            }
            if (InMemoryDatabase.getInstance().findUserByUsername(username) != null) { 
                sendJson(ex, 200, "{\"success\":false,\"message\":\"exists\"}"); 
                return; 
            }
            InMemoryDatabase.getInstance().saveUser(new User(username, password));
            sendJson(ex, 200, "{\"success\":true}");
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            String body = readAll(ex.getRequestBody());
            String username = extractString(body, "username");
            String password = extractString(body, "password");
            if (username == null || password == null) { sendJson(ex,400,"{\"success\":false,\"message\":\"missing\"}"); return; }
            User u = InMemoryDatabase.getInstance().findUserByUsername(username);
            if (u == null || !u.getPassword().equals(password)) { sendJson(ex,200,"{\"success\":false,\"message\":\"invalid\"}"); return; }
            sendJson(ex,200,"{\"success\":true,\"userId\":\"" + u.getId() + "\"}");
        }
    }

    static class QuestionsHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            List<Question> qs = InMemoryDatabase.getInstance().questions();
            int n = Math.min(5, qs.size());
            StringBuilder sb = new StringBuilder();
            sb.append("{\"questions\":[");
            for (int i=0;i<n;i++) {
                Question q = qs.get(i);
                sb.append("{\"id\":").append(i+1).append(",\"question\":\"").append(escape(q.getText())).append("\",\"options\":[");
                List<String> opts = q.getOptions();
                for (int j=0;j<opts.size();j++) {
                    if (j>0) sb.append(',');
                    sb.append('\"').append(escape(opts.get(j))).append('\"');
                }
                sb.append("],\"correctIndex\":").append(q.getCorrectIndex()).append("}");
                if (i<n-1) sb.append(',');
            }
            sb.append("]}");
            sendJson(ex,200,sb.toString());
        }
    }

    static class AddQuestionHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            String body = readAll(ex.getRequestBody());
            String text = extractString(body, "text");
            List<String> options = extractStringArray(body, "options");
            int correct = extractInt(body, "correctIndex", 0);
            if (text==null || options.size()<4) { sendJson(ex,400,"{\"success\":false}"); return; }
            Question q = new Question(text, options, correct);
            InMemoryDatabase.getInstance().saveQuestion(q);
            sendJson(ex,200,"{\"success\":true}");
        }
    }

    static class UploadHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            String body = readAll(ex.getRequestBody()).trim();
            if (body.isEmpty()) { sendJson(ex,400,"{\"success\":false,\"message\":\"empty\"}"); return; }
            String arr = body;
            if (!arr.startsWith("[")) { sendJson(ex,400,"{\"success\":false,\"message\":\"expected array\"}"); return; }
            arr = arr.substring(1, Math.max(1, arr.length()-1));
            List<String> objs = new ArrayList<>();
            int brace=0; int start=-1;
            for (int i=0;i<arr.length();i++) {
                char c = arr.charAt(i);
                if (c=='{') { if (brace==0) start=i; brace++; }
                else if (c=='}') { brace--; if (brace==0 && start>=0) { objs.add(arr.substring(start, i+1)); start=-1; } }
            }
            int added=0;
            for (String o : objs) {
                String text = extractString(o, "text");
                List<String> options = extractStringArray(o, "options");
                int correct = extractInt(o, "correctIndex", 0);
                if (text!=null && options.size()>=2) {
                    Question q = new Question(text, options, correct);
                    InMemoryDatabase.getInstance().saveQuestion(q);
                    added++;
                }
            }
            sendJson(ex,200,"{\"success\":true,\"added\":"+added+"}");
        }
    }

    static class SubmitHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "POST,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            String body = readAll(ex.getRequestBody());
            String userId = extractString(body, "userId");
            if (userId == null) userId = "anonymous";
            
            List<Question> allQuestions = InMemoryDatabase.getInstance().questions();
            int n = Math.min(5, allQuestions.size());
            int score = 0;
            
            for (int i = 0; i < n; i++) {
                int answerIndex = extractAnswerAtIndex(body, i);
                Question q = allQuestions.get(i);
                if (answerIndex == q.getCorrectIndex()) {
                    score++;
                }
            }
            
            Result r = new Result(userId, n, score, (score * 100.0) / n);
            InMemoryDatabase.getInstance().saveResult(r);
            
            StringBuilder resp = new StringBuilder();
            resp.append("{\"success\":true,\"score\":").append(score).append(",\"total\":").append(n).append(",\"answers\":[");
            for (int i = 0; i < n; i++) {
                if (i > 0) resp.append(',');
                resp.append(extractAnswerAtIndex(body, i));
            }
            resp.append("]}");
            
            sendJson(ex, 200, resp.toString());
        }
        
        private int extractAnswerAtIndex(String body, int index) {
            Pattern p = Pattern.compile("\"answers\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
            Matcher m = p.matcher(body);
            if (m.find()) {
                String inner = m.group(1);
                String[] parts = inner.split(",");
                if (index < parts.length) {
                    try {
                        return Integer.parseInt(parts[index].trim());
                    } catch (Exception ignored) {}
                }
            }
            return -1;
        }
    }

    static class ResultsHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(ex.getRequestMethod())) {
                ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
                ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET,OPTIONS");
                ex.sendResponseHeaders(204, -1);
                return;
            }
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) { ex.sendResponseHeaders(405, -1); return; }
            
            List<Result> allResults = InMemoryDatabase.getInstance().results();
            List<User> allUsers = InMemoryDatabase.getInstance().users();
            
            StringBuilder resp = new StringBuilder();
            resp.append("{\"results\":[");
            
            for (int i = 0; i < allResults.size(); i++) {
                Result result = allResults.get(i);
                User user = allUsers.stream().filter(u -> u.getId().equals(result.getUserId())).findFirst().orElse(null);
                String username = user != null ? user.getUsername() : "Unknown User";
                
                if (i > 0) resp.append(',');
                resp.append("{\"username\":\"").append(escape(username)).append("\",");
                resp.append("\"score\":").append(result.getCorrect()).append(",");
                resp.append("\"total\":").append(result.getTotal()).append(",");
                resp.append("\"percentage\":").append(String.format("%.2f", result.getPercentage())).append("}");
            }
            
            resp.append("]}");
            sendJson(ex, 200, resp.toString());
        }
    }

    static String escape(String s) { return s.replace("\\","\\\\").replace("\"","\\\""); }
}
