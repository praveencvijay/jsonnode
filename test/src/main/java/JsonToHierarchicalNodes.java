import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Node class to represent each field in the JSON
class Node {
    String fieldName;
    String value;
    int level;
    List<Node> children;

    public Node(String fieldName, String value, int level) {
        this.fieldName = fieldName;
        this.value = value;
        this.level = level;
        this.children = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Node{" +
                "fieldName='" + fieldName + '\'' +
                ", value='" + value + '\'' +
                ", level=" + level +
                ", children=" + children +
                '}';
    }
}

public class JsonToHierarchicalNodes {
    public static void main(String[] args) {
    	String jsonFilePath = "E:\\java\\ws\\test\\src\\main\\resources\\sample.json";

        try {
            // Create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON string and convert to JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            // Parse JSON and construct the hierarchical tree structure
            Node root = parseJson(rootNode, "", 0);

            // Print the hierarchical tree structure
            printNode(root, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Node parseJson(JsonNode node, String fieldName, int level) {
        Node currentNode = new Node(fieldName, node.isValueNode() ? node.asText() : "", level);

        if (node.isObject()) {
            node.fieldNames().forEachRemaining(field -> {
                JsonNode childNode = node.get(field);
                currentNode.children.add(parseJson(childNode, field, level + 1));
            });
        } else if (node.isArray()) {
            for (JsonNode arrayItem : node) {
                currentNode.children.add(parseJson(arrayItem, "", level + 1));
            }
        }

        return currentNode;
    }

    private static void printNode(Node node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.fieldName + ": " + node.value +" : " + level);
        for (Node child : node.children) {
            printNode(child, level + 1);
        }
    }
}
