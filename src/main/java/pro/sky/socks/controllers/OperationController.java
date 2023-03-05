package pro.sky.socks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socks.exception.FileProcessingException;
import pro.sky.socks.servise.FileService;
import pro.sky.socks.servise.SocksService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
@RestController
@RequestMapping("/operations")
@RequiredArgsConstructor
@Tag(name = "Конфигурация операций")
public class OperationController {
    private final SocksService socksService;
    private final FileService sockFileService;

    @GetMapping("/export")
    @Operation(description = "Экспорт операций")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        InputStreamResource inputStreamResource = socksService.exportOperations();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(Files.size(sockFileService.getOperationPath()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename =\"operation.json\"")
                .body(inputStreamResource);
    }
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Импорт операций")
    public ResponseEntity<Void>upLoadDataFile(@RequestParam MultipartFile file) throws FileNotFoundException, FileProcessingException {
        socksService.importOperations(file);
        return ResponseEntity.ok().build();
    }
}
