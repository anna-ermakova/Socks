package pro.sky.socks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socks.dto.SocksDto;
import pro.sky.socks.exception.FileProcessingException;
import pro.sky.socks.model.Color;
import pro.sky.socks.model.SocksSize;
import pro.sky.socks.servise.FileService;
import pro.sky.socks.servise.SocksService;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/socks")
@RequiredArgsConstructor
@Tag(name = "Склад носков", description = "CRUD-операции для работы с носками")
public class SocksController {
    private final SocksService socksService;
    private final FileService socksFileService;

    @PostMapping
    @Operation(summary = "Добавление носков на склад")
    public void addSocks(@Valid @RequestBody SocksDto socksDto) {
        socksService.addSocks(socksDto);
    }

    @PutMapping
    @Operation(summary = "Отгрузка со склада")
    public void sellSocks(@Valid @RequestBody SocksDto socksDto) {
        socksService.sellSocks(socksDto);
    }

    @GetMapping
    @Operation(summary = "Получение носков по параметрам")
    public Integer getSocksCount(@RequestParam(required = false, name = "color") Color color,
                                 @RequestParam(required = false, name = "size") SocksSize size,
                                 @RequestParam(required = false, name = "cottonMin") Integer cottonMin,
                                 @RequestParam(required = false, name = "cottonMax") Integer cottonMax) {
        return socksService.getSocksQuantity(color, size, cottonMin, cottonMax);
    }

    @DeleteMapping
    @Operation(summary = "Списание брака")
    public void removeDefectiveSocks(@Valid @RequestBody SocksDto socksDto) {
        socksService.removeDefectiveSocks(socksDto);
    }

    @GetMapping("/export")
    @Operation(description = "Экспорт файла")
    public ResponseEntity<InputStreamResource> downloadFile()throws IOException,FileProcessingException {
        InputStreamResource inputStreamResource = socksService.exportFile();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(Files.size(socksFileService.getPath()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"socks.json\"")
                .body(inputStreamResource);
    }
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Импорт файла")
    public ResponseEntity<Void> upLoadDataFile(@RequestParam MultipartFile file) throws FileNotFoundException, FileProcessingException {
        socksService.importFile(file);
        return ResponseEntity.ok().build();
    }
}
