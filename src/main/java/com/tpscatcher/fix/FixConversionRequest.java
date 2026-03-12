package com.tpscatcher.fix;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload containing either FIX data in hexadecimal or base64 form")
public record FixConversionRequest(
    @Schema(
        description = "FIX message encoded as hexadecimal characters",
        example = "383D4649582E342E3401393D31320133353D3001")
    String hexData,
    @Schema(
        description = "FIX message encoded as base64",
        example = "OD1GSVguNC40ATk9MTIBMzU9MAE=")
    String base64Data) {

    public boolean hasHexData() {
        return hexData != null && !hexData.isBlank();
    }

    public boolean hasBase64Data() {
        return base64Data != null && !base64Data.isBlank();
    }
}