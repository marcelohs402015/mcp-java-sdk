# MCP Java Server para Cursor

Servidor MCP (Model Context Protocol) em Java via STDIO para uso no Cursor.

Este projeto foi criado para disponibilizar um JAR executavel simples, que pode ser configurado em qualquer projeto Java no Cursor para expor ferramentas MCP.

## Objetivo

- Fornecer um servidor MCP minimo e funcional para o Cursor
- Entregar build simples (`mvn clean package`) com JAR pronto para uso
- Servir como base para evoluir novas tools MCP em Java

## Origem e fork

Este trabalho e baseado no ecossistema MCP Java SDK:

- SDK oficial MCP Java: [modelcontextprotocol/java-sdk](https://github.com/modelcontextprotocol/java-sdk)
- Repositorio alvo deste fork/publicacao: [marcelohs402015/mcp-java-sdk](https://github.com/marcelohs402015/mcp-java-sdk)

## Referencias uteis

- MCP Java SDK (repo oficial): [modelcontextprotocol/java-sdk](https://github.com/modelcontextprotocol/java-sdk)
- Documentacao MCP Java SDK: [MCP Java SDK docs](https://modelcontextprotocol.io/sdk/java/mcp-overview)
- Quickstart (dependencias/BOM): [Quickstart](https://java.sdk.modelcontextprotocol.io/latest/quickstart/)
- Guia de servidor MCP Java: [Server Guide](https://java.sdk.modelcontextprotocol.io/latest/server/)

## Build

```bash
mvn clean package
```

JAR gerado:

- `target/cursor-java-mcp-server-1.0.0.jar`

## Configuracao no Cursor

Use no `.cursor/mcp.json` (projeto) ou `%USERPROFILE%/.cursor/mcp.json` (global):

```json
{
  "mcpServers": {
    "java-tools": {
      "type": "stdio",
      "command": "java",
      "args": [
        "-jar",
        "C:/projetos/java/java-sdk/cursor-java-mcp-server/target/cursor-java-mcp-server-1.0.0.jar"
      ]
    }
  }
}
```

## Tools disponiveis

- `sum_numbers`: soma dois numeros (`a` + `b`)
- `echo_text`: devolve o texto recebido
