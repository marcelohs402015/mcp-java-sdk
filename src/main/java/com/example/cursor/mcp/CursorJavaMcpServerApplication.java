package com.example.cursor.mcp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Tool;

public final class CursorJavaMcpServerApplication {

    private CursorJavaMcpServerApplication() {
    }

    public static void main(String[] args) throws InterruptedException {
        var transport = new StdioServerTransportProvider(McpJsonDefaults.getMapper());

        var sumTool = Tool.builder()
            .name("sum_numbers")
            .description("Return the sum of two numbers: a + b")
            .inputSchema(toolInputSchema())
            .build();

        var echoTool = Tool.builder()
            .name("echo_text")
            .description("Echo the received text")
            .inputSchema(new McpSchema.JsonSchema(
                    "object",
                    Map.of("text", Map.of("type", "string")),
                    List.of("text"),
                    false,
                    null,
                    null))
            .build();

        var server = McpServer.sync(transport)
            .serverInfo("cursor-java-tools", "1.0.0")
            .capabilities(ServerCapabilities.builder().tools(true).build())
            .toolCall(sumTool, (exchange, request) -> {
                Number a = asNumber(request.arguments().get("a"));
                Number b = asNumber(request.arguments().get("b"));
                if (a == null || b == null) {
                    return CallToolResult.builder()
                        .isError(true)
                        .content(List.of(new McpSchema.TextContent("Arguments 'a' and 'b' must be numbers.")))
                        .build();
                }
                double sum = a.doubleValue() + b.doubleValue();
                return CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent(Double.toString(sum))))
                    .build();
            })
            .toolCall(echoTool, (exchange, request) -> {
                Object text = request.arguments().get("text");
                return CallToolResult.builder()
                    .content(List.of(new McpSchema.TextContent(text == null ? "" : text.toString())))
                    .build();
            })
            .build();

        Runtime.getRuntime().addShutdownHook(new Thread(server::closeGracefully));

        new CountDownLatch(1).await();
    }

    private static Number asNumber(Object value) {
        return (value instanceof Number number) ? number : null;
    }

    private static McpSchema.JsonSchema toolInputSchema() {
        return new McpSchema.JsonSchema(
                "object",
                Map.of(
                        "a", Map.of("type", "number"),
                        "b", Map.of("type", "number")),
                List.of("a", "b"),
                false,
                null,
                null);
    }

}
