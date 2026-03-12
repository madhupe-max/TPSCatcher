package com.tpscatcher.fix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class FixBinaryToAsciiServiceTest {

    private final FixBinaryToAsciiService service = new FixBinaryToAsciiService();

    @Test
    void convertsHexEncodedFixMessageToAscii() {
        String hexFix = "383D4649582E342E3401393D31320133353D3001";

        String asciiFix = service.convertHex(hexFix);

        assertEquals("8=FIX.4.4\u00019=12\u000135=0\u0001", asciiFix);
    }

    @Test
    void convertsFixDelimitersToReadablePipes() {
        String hexFix = "383D4649582E342E3401393D31320133353D3001";

        String readableFix = service.convertHexToReadableFix(hexFix);

        assertEquals("8=FIX.4.4|9=12|35=0|", readableFix);
    }

    @Test
    void convertsBase64EncodedFixMessageToAscii() {
        String base64Fix = "OD1GSVguNC40ATk9MTIBMzU9MAE=";

        String asciiFix = service.convertBase64(base64Fix);

        assertEquals("8=FIX.4.4\u00019=12\u000135=0\u0001", asciiFix);
    }

    @Test
    void rejectsOddLengthHexInput() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.convertHex("ABC"));

        assertEquals("Hex input must contain an even number of characters", exception.getMessage());
    }

    @Test
    void rejectsInvalidBase64Input() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.convertBase64("***"));

        assertEquals("Base64 input contains invalid characters", exception.getMessage());
    }
}