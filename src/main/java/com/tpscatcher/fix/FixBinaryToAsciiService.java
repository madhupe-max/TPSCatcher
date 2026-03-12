package com.tpscatcher.fix;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class FixBinaryToAsciiService {

    public String convert(byte[] fixBinaryData) {
        if (fixBinaryData == null || fixBinaryData.length == 0) {
            return "";
        }
        return new String(fixBinaryData, StandardCharsets.US_ASCII);
    }

    public String convertHex(String hexData) {
        if (hexData == null || hexData.isBlank()) {
            return "";
        }
        return convert(parseHex(hexData));
    }

    public String convertHexToReadableFix(String hexData) {
        return toReadableFix(convertHex(hexData));
    }

    public String convertBase64(String base64Data) {
        if (base64Data == null || base64Data.isBlank()) {
            return "";
        }
        try {
            return convert(Base64.getDecoder().decode(base64Data));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Base64 input contains invalid characters", exception);
        }
    }

    public String convertBase64ToReadableFix(String base64Data) {
        return toReadableFix(convertBase64(base64Data));
    }

    public String toReadableFix(byte[] fixBinaryData) {
        return toReadableFix(convert(fixBinaryData));
    }

    public String toReadableFix(String asciiFixMessage) {
        if (asciiFixMessage == null || asciiFixMessage.isEmpty()) {
            return "";
        }
        return asciiFixMessage.replace('\u0001', '|');
    }

    private byte[] parseHex(String hexData) {
        String normalizedHex = hexData.replaceAll("\\s+", "");
        if ((normalizedHex.length() & 1) != 0) {
            throw new IllegalArgumentException("Hex input must contain an even number of characters");
        }

        byte[] bytes = new byte[normalizedHex.length() / 2];
        for (int index = 0; index < normalizedHex.length(); index += 2) {
            int high = Character.digit(normalizedHex.charAt(index), 16);
            int low = Character.digit(normalizedHex.charAt(index + 1), 16);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("Hex input contains non-hexadecimal characters");
            }
            bytes[index / 2] = (byte) ((high << 4) + low);
        }
        return bytes;
    }
}