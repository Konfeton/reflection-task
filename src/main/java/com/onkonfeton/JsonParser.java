package com.onkonfeton;

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JsonParser {

    private static final char OBJECT_START = '{';
    private static final char OBJECT_END = '}';
    private static final char ARRAY_START = '[';
    private static final char ARRAY_END = ']';
    private static final char QUOTE = '"';
    private static final char COMMA = ',';
    private static final char COLON = ':';
    private static final char ESCAPE_SEQUENCE = '\n';
    private static final char SPACE = ' ';

    private String json;
    private int index = 0;

    @SneakyThrows
    public Map<String, Object> parse(String json) {
        this.json = json;
        Object object = parseValue();
        Map<String, Object> value = (Map<String, Object>) object;
        return value;
    }

    public Object parseValue() throws IOException {
        skipUnnecessarySymbols();
        char currentChar = peek();
        if (currentChar == OBJECT_START) {
            return parseObject();
        } else if (currentChar == ARRAY_START) {
            return parseArray();
        } else if (currentChar == QUOTE) {
            return parseString();
        } else if (Character.isDigit(currentChar) || currentChar == '-') {
            return parseNumber();
        } else if (json.startsWith("true", index)) {
            index += 4;
            return true;
        } else if (json.startsWith("false", index)) {
            index += 5;
            return false;
        } else if (json.startsWith("null", index)) {
            index += 4;
            return null;
        } else {
            throw new IOException("Unexpected character: " + currentChar);
        }

    }

    private Object parseObject() throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        index++; // Skip '{'
        skipUnnecessarySymbols();
        while (peek() != OBJECT_END) {
            Object key = parseString();
            skipUnnecessarySymbols();
            if (peek() != COLON) {
                throw new RuntimeException("Expected colon after key");
            }
            index++; // Skip ':'
            skipUnnecessarySymbols();
            Object value = parseValue();
            map.put((String) key, value);
            skipUnnecessarySymbols();
            if (peek() == COMMA) {
                index++; // Skip ','
                skipUnnecessarySymbols();
            } else if (peek() != OBJECT_END) {
                throw new RuntimeException("Expected comma or closing brace");
            }
        }
        index++; // Skip '}'
        return map;
    }

    private Object parseArray() throws IOException {
        List<Object> array = new ArrayList<>();
        index++;// Skip '['
        skipUnnecessarySymbols();
        while (peek() != ARRAY_END) {
            Object value = parseValue();
            array.add(value);
            skipUnnecessarySymbols();
            if (peek() == COMMA) {
                index++; // Skip ','
                skipUnnecessarySymbols();
            } else if (peek() != ARRAY_END) {
                throw new IOException("Expected comma or closing bracket");
            }
        }
        index++; // Skip ']'
        return array;
    }

    private Object parseString() {
        index++; // Skip '"'
        StringBuilder sb = new StringBuilder();
        while (peek() != '"') {
            sb.append(peek());
            index++;
        }
        index++; // Skip '"'
        return sb.toString();

    }

    private Object parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(peek()) || peek() == '-' || peek() == '.') {
            sb.append(peek());
            index++;
        }
        String numberString = sb.toString();
        if (numberString.contains(".")) {
            return Double.parseDouble(numberString);
        } else {
            return Integer.parseInt(numberString);
        }
    }

    private void skipUnnecessarySymbols() {
        while (json.charAt(index) == SPACE || json.charAt(index) == ESCAPE_SEQUENCE) {
            index++;
        }
    }

    private char peek() {
        return this.json.charAt(index);
    }
}
