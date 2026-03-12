package com.tpscatcher.fix;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FixBinaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void convertsHexPayloadOverHttp() throws Exception {
        mockMvc.perform(post("/api/fix/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hexData": "383D4649582E342E3401393D31320133353D3001"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asciiMessage").value("8=FIX.4.4\u00019=12\u000135=0\u0001"))
                .andExpect(jsonPath("$.readableMessage").value("8=FIX.4.4|9=12|35=0|"));
    }

    @Test
    void rejectsRequestsWithBothFormats() throws Exception {
        mockMvc.perform(post("/api/fix/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "hexData": "AA",
                                  "base64Data": "YQ=="
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provide exactly one of hexData or base64Data"));
    }

                            @Test
                            void convertsUploadedBinaryFileOverHttp() throws Exception {
                          MockMultipartFile file = new MockMultipartFile(
                            "file",
                            "fix-message.bin",
                            MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            "8=FIX.4.4\u00019=12\u000135=0\u0001".getBytes(StandardCharsets.US_ASCII));

                          mockMvc.perform(multipart("/api/fix/convert-file").file(file))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.asciiMessage").value("8=FIX.4.4\u00019=12\u000135=0\u0001"))
                            .andExpect(jsonPath("$.readableMessage").value("8=FIX.4.4|9=12|35=0|"));
                            }

                            @Test
                            void rejectsEmptyUploadedFile() throws Exception {
                          MockMultipartFile file = new MockMultipartFile(
                            "file",
                            "empty.bin",
                            MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            new byte[0]);

                          mockMvc.perform(multipart("/api/fix/convert-file").file(file))
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.message").value("Uploaded file must not be empty"));
                            }
}