package edu.kit.iti.formal.keyserver;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Alexander Weigl
 * @version 1 (25.09.19)
 */
public class HttpServer implements Runnable {
    private final Thread thread;
    private final Backend backend = new Backend();

    private HttpServer() {
        thread = new Thread(this);
    }

    public static void main(String[] args) {
        new HttpServer().start();
    }

    private void start() {
        thread.start();
    }

    @Override
    public void run() {
        //Waiting for requests
        try {
            ServerSocket socket = new ServerSocket(9000); //bind on 0.0.0.0:8000
            Socket clientSocket = socket.accept();
            System.out.format("Incoming request %s\n", clientSocket.getInetAddress());
            RequestHandler handler = new RequestHandler(clientSocket);
            Thread t = new Thread(handler);
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class RequestHandler implements Runnable {
        private final Socket clientSocket;

        RequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try (InputStream is = clientSocket.getInputStream()) {
                String req = new String(is.readAllBytes());
                Request request = Request.parse(req);
                dispatchRequest(request, clientSocket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void dispatchRequest(Request request, OutputStream o) {
            int pos = request.getPath().indexOf('/', 2);
            String endpoint = request.getPath().substring(0, pos + 1);
            String parameter = request.getPath().substring(pos + 1);

            System.out.format("Parsed request: %s %s %s %s%n",
                    new Date(), request.getProtocol(), request.getMethod(), request.getPath());
            PrintStream out = new PrintStream(o);
            System.out.println(endpoint);
            try {
                switch (endpoint) {
                    case "/get/":
                        @Nullable String key = backend.get(parameter);
                        sendString(out, key);
                        break;
                    case "/add/":
                        String token = backend.add(parameter, request.getBody());
                        sendString(out, token);
                        break;
                    case "/add!/":
                        backend.confirmAdd(parameter);
                        sendString(out, "ok");
                        break;
                    case "/del/":
                        String t = backend.del(parameter, request.getBody());
                        sendString(out, t);
                        break;
                    case "/del!/":
                        backend.confirmDel(parameter);
                        sendString(out, "ok");
                        break;
                    default:
                        out.format("HTTP/1.1 404 NotFound\r\n\r\n");
                }
            } catch (Exception e) {
                out.format("HTTP/1.1 500 %s\r\n\r\n", e.getMessage());
            }
            try {
                o.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void sendString(PrintStream out, String response) {
            out.format("HTTP/1.1 200 OK\r\n\r\n");
            out.format("\"%s\"", response);
        }
    }
}