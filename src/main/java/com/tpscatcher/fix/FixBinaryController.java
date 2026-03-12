package com.tpscatcher.fix;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fix")
public class FixBinaryController {

    private final FixBinaryToAsciiService converter;

    public FixBinaryController(FixBinaryToAsciiService converter) {
        this.converter = converter;
    }

        @Operation(
            summary = "Convert FIX binary payload to ASCII",
            description = "Accepts exactly one input format: hexData or base64Data. Returns both the raw ASCII FIX message and a readable version with SOH rendered as |.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "FIX payload converted successfully"),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request payload",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = "{\"message\":\"Provide exactly one of hexData or base64Data\"}")))
        })
    @PostMapping("/convert")
        public FixConversionResponse convert(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FixConversionRequest.class),
                    examples = {
                        @ExampleObject(
                            name = "Hex payload",
                            value = "{\"hexData\":\"383D4649582E342E3401393D31320133353D3001\"}"),
                        @ExampleObject(
                            name = "Base64 payload",
                            value = "{\"base64Data\":\"OD1GSVguNC40ATk9MTIBMzU9MAE=\"}")
                    }))
            @RequestBody FixConversionRequest request) {
        validateRequest(request);

        String asciiMessage = request.hasHexData()
                ? converter.convertHex(request.hexData())
                : converter.convertBase64(request.base64Data());

            return buildResponse(asciiMessage);
            }

            @Operation(
                summary = "Upload raw FIX binary file",
                description = "Accepts a multipart file upload containing raw FIX binary bytes and returns both the ASCII FIX message and a readable version with SOH rendered as |.")
            @ApiResponses({
                @ApiResponse(responseCode = "200", description = "FIX file converted successfully"),
                @ApiResponse(
                    responseCode = "400",
                    description = "Missing or empty file upload",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = @ExampleObject(value = "{\"message\":\"Multipart file 'file' is required\"}")))
            })
            @PostMapping(path = "/convert-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            public FixConversionResponse convertFile(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                        mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                        schema = @Schema(type = "object", requiredProperties = {"file"})))
                @RequestPart("file") MultipartFile file) {
            validateFile(file);
            try {
                return buildResponse(converter.convert(file.getBytes()));
            } catch (java.io.IOException exception) {
                throw new IllegalArgumentException("Unable to read uploaded file", exception);
            }
    }

    private void validateRequest(FixConversionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.hasHexData() == request.hasBase64Data()) {
            throw new IllegalArgumentException("Provide exactly one of hexData or base64Data");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("Multipart file 'file' is required");
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file must not be empty");
        }
    }

    private FixConversionResponse buildResponse(String asciiMessage) {
        return new FixConversionResponse(asciiMessage, converter.toReadableFix(asciiMessage));
    }
}