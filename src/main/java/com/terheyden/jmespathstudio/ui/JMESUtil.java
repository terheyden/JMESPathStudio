package com.terheyden.jmespathstudio.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.jackson.JacksonRuntime;

import java.io.IOException;

/* DEPENDENCIES:

<!-- I had to download and compile the io.burt one myself: -->
<dependency>
    <groupId>io.burt</groupId>
    <artifactId>jmespath-jackson</artifactId>
    <version>0.2.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.8.9</version>
</dependency>

 */

/**
 * Wrapper class for the JMESPath library to make it easy to use.
 *
 * Just call: searchN(json, jmesPath) -- to get results as a JsonNode
 *        or: searchS(json, jmesPath) -- to get results as a String
 */
public enum JMESUtil {
    ;

    // For JSON mapping.
    private static final ObjectMapper mapper = new ObjectMapper();

    // For JSON pretty printing.
    private static final ObjectWriter jsonWriter = mapper.writerWithDefaultPrettyPrinter();

    // Create a Jackson / JSON runtime.
    private static final JmesPath<JsonNode> jmes = new JacksonRuntime();

    /**
     * Search some JSON using a JMESPath expression. Returns a JsonNode object as a result.
     * Use searchS() instead to get results as a String.
     */
    public static JsonNode searchN(JsonNode node, Expression<JsonNode> expression) {
        // Run the JMESPath magic...
        return expression.search(node);
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JsonNode object as a result.
     * Use searchS() instead to get results as a String.
     */
    public static JsonNode searchN(JsonNode json, String jmesPath) {
        return searchN(json, jmes.compile(jmesPath));
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JsonNode object as a result.
     * Use searchS() instead to get results as a String.
     */
    public static JsonNode searchN(String json, Expression<JsonNode> jmesPath) {
        return searchN(json2Node(json), jmesPath);
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JsonNode object as a result.
     * Use searchS() instead to get results as a String.
     */
    public static JsonNode searchN(String json, String jmesPath) {
        return searchN(json2Node(json), jmes.compile(jmesPath));
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JSON String as a result.
     * Use searchN() instead to get results as a JsonNode.
     */
    public static String searchS(JsonNode node, Expression<JsonNode> expression) {
        return node2Str(searchN(node, expression));
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JSON String as a result.
     * Use searchN() instead to get results as a JsonNode.
     */
    public static String searchS(JsonNode json, String jmesPath) {
        return node2Str(searchN(json, jmes.compile(jmesPath)));
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JSON String as a result.
     * Use searchN() instead to get results as a JsonNode.
     */
    public static String searchS(String json, Expression<JsonNode> jmesPath) {
        return node2Str(searchN(json2Node(json), jmesPath));
    }

    /**
     * Search some JSON using a JMESPath expression. Returns a JSON String as a result.
     * Use searchN() instead to get results as a JsonNode.
     */
    public static String searchS(String json, String jmesPath) {
        return node2Str(searchN(json2Node(json), jmes.compile(jmesPath)));
    }

    /**
     * Quietly convert a JsonNode to a String.
     */
    private static String node2Str(JsonNode node) {
        try {
            return jsonWriter.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error writing JsonNode as a String: " + node.asText(), e);
        }
    }

    /**
     * Quietly convert JSON to a JsonNode.
     */
    private static JsonNode json2Node(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalStateException("Error mapping JSON: " + json, e);
        }
    }
}
