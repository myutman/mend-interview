package com.mend.interview;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolutionHTTPHandler implements HttpHandler {

    static Logger LOGGER = Logger.getLogger("Http Handler");

    private static class ParseException extends RuntimeException {
        public ParseException(String msg) {
            super(msg);
        }
    }

    private Solution.Edge parseEdge(JSONObject edge) throws ParseException {
        int v1;
        int v2;
        String color;

        try {
            v1 = edge.getInt("v1");
        } catch (JSONException e) {
            throw new ParseException("v1 field was not provided in the edge");
        }

        try {
            v2 = edge.getInt("v2");
        } catch (JSONException e) {
            throw new ParseException("v2 field was not provided in the edge");
        }

        try {
            color = edge.getString("color");
        } catch (JSONException e) {
            throw new ParseException("color field was not provided in the edge");
        }

        if (!"red".equals(color) && !"blue".equals(color)) {
            throw new ParseException("color must be either red or blue in the edge");
        }

        return new Solution.Edge(v1, v2, color);
    }

    private Solution.TaskInput parseRequestBody(String requestBody) throws ParseException {


        JSONObject jsonData;
        try {
            jsonData = new JSONObject(requestBody);
        } catch (JSONException e) {
            throw new ParseException("Cannot parse JSON");
        }

        int nNodes;
        try {
            nNodes = jsonData.getInt("n_nodes");
        } catch (JSONException e) {
            throw new ParseException("n_nodes field was not provided or has an incorrect format");
        }
        LOGGER.log(Level.INFO, "n_nodes: {0}", nNodes);

        int start;
        try {
            start = jsonData.getInt("start");
        } catch (JSONException e) {
            throw new ParseException("start field was not provided or has an incorrect format");
        }
        LOGGER.log(Level.INFO, "start: {0}", start);

        if (start < 1 || start > nNodes) {
            throw new ParseException("Node start (#%d) is out of bounds for a node number [1; %d]"
                    .formatted(start, nNodes));
        }

        JSONArray edges;
        try {
            edges = jsonData.getJSONArray("edges");
        } catch (JSONException e) {
            throw new ParseException("edges field was not provided or has an incorrect format");
        }

        ArrayList<Solution.Edge> inputEdges = new ArrayList<>();
        for (int index = 0; index < edges.length(); index++) {
            JSONObject edge;
            try {
                edge = edges.getJSONObject(index);
            } catch (JSONException e) {
                throw new ParseException("Edge #%d has an incorrect format".formatted(index + 1));
            }

            Solution.Edge inputEdge;
            try {
                inputEdge = parseEdge(edge);
            } catch (ParseException e) {
                throw new ParseException(e.getMessage() + " #%d".formatted(index + 1));
            }

            if (inputEdge.v1 > nNodes || inputEdge.v1 < 1) {
                throw new ParseException(
                        "Node v1 (#%d) in edge #%d is out of bounds for a node number [1; %d]"
                                .formatted(inputEdge.v1, index + 1, nNodes)
                );
            }

            if (inputEdge.v2 > nNodes || inputEdge.v2 < 1) {
                throw new ParseException(
                        "Node v2 (#%d) in edge #%d is out of bounds for a node number [1; %d]"
                                .formatted(inputEdge.v2, index + 1, nNodes)
                );
            }

            inputEdges.add(inputEdge);
        }

        return new Solution.TaskInput(nNodes, start, inputEdges);
    }

    private String formErrorMessage(String errorMessage) {
        JSONObject message = new JSONObject();
        try {
            message.put("status", "ERROR");
            message.put("cause", errorMessage);
        } catch (JSONException e) {
            LOGGER.log(Level.INFO, "Something went wrong during building error message {0}", errorMessage);
        }
        return message.toString();
    }

    private String formAnswer(ArrayList<Integer> answer) {
        JSONObject message = new JSONObject();
        try {
            message.put("status", "OK");
            message.put("answer", new JSONArray(answer));
        } catch (JSONException e) {
            LOGGER.log(Level.INFO, "Something went wrong during building answer");
        }
        return message.toString();
    }

    private String buildResponse(HttpExchange exchange) {
        if (!"POST".equals(exchange.getRequestMethod())) {
            return formErrorMessage("Incorrect method");
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder builder = new StringBuilder();

        String inputLine;
        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                return formErrorMessage("Error reading request body");
            }
            builder.append(inputLine);
        }

        Solution.TaskInput taskInput;
        try {
            taskInput = parseRequestBody(builder.toString());
        } catch (ParseException e) {
            return formErrorMessage(e.getMessage());
        }

        ArrayList<Integer> answer = Solution.solve(taskInput);
        return formAnswer(answer);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = buildResponse(exchange);
        OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
