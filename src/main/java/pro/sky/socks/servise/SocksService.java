package pro.sky.socks.servise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.socks.dto.SocksDto;
import pro.sky.socks.dto.SocksMapper;
import pro.sky.socks.dto.SocksTransactionDto;
import pro.sky.socks.exception.EmptyStockException;
import pro.sky.socks.exception.FileProcessingException;
import pro.sky.socks.model.Color;
import pro.sky.socks.model.OperationType;
import pro.sky.socks.model.Sock;
import pro.sky.socks.model.SocksSize;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocksService {
    private static Map<Sock, Integer> socksMap = new HashMap<>();
    private static List<SocksTransactionDto> transactions = new ArrayList<>();
    private final SocksMapper socksMapper;
    private final FileService fileService;

    public void addSocks(SocksDto socksDto) {
        Sock sock = socksMapper.toSocks(socksDto);
        if (socksMap.containsKey(sock)) {
            socksMap.put(sock, socksMap.get(sock) + socksDto.getQuantity());
        } else {
            socksMap.put(sock, socksDto.getQuantity());
        }
        // transactions.add(socksMapper.toSocksTransactionDto(socksDto, OperationType.ACCEPTANCE, LocalDateTime.now()));
    }

    public void sellSocks(SocksDto socksDto) {
        decreaseSockQuantity(socksDto);
        // transactions.add(socksMapper.toSocksTransactionDto.OperationType.ISSUANCE, LocalDateTime.now());
    }

    public void removeDefectiveSocks(SocksDto socksDto) {
        decreaseSockQuantity(socksDto);
        //transactions.add(socksMapper.toSocksTransactionDto(socksDto, OperationType.WRTEOFF, LocalDateTime.now());
    }

    public void decreaseSockQuantity(SocksDto socksDto) {
        Sock sock = socksMapper.toSocks(socksDto);
        Integer sockQuantity = socksMap.getOrDefault(sock, 0);
        if (sockQuantity >= socksDto.getQuantity()) {
            socksMap.put(sock, sockQuantity - socksDto.getQuantity());
        } else {
            throw new EmptyStockException("На складе нет носков.");
        }
    }

    public Integer getSocksQuantity(Color color, SocksSize size, Integer cottonMin, Integer cottonMax) {
        return
        socksMap.entrySet().stream()
                .filter(color != null ? s -> color.equals(s.getKey().getColor()) : s -> true)
                .filter(size != null ? s -> size.equals(s.getKey().getSize()) : s -> true)
                .filter(cottonMin != null ? s -> cottonMin <= s.getKey().getCottonContent() : s -> true)
                .filter(cottonMax != null ? s -> cottonMax >= s.getKey().getCottonContent() : s -> true)
                .mapToInt(s -> s.getValue()).sum();
    }
    public void importFile(MultipartFile multipartFile) throws FileProcessingException, FileNotFoundException {
        fileService.importFile(multipartFile);
        try {
            String json = fileService.readFromFile();
            List<SocksDto> socksList = new ObjectMapper().readValue(json, new TypeReference<List<SocksDto>>() {
            });
            socksMap = socksMapper.fromListOfSocksDto(socksList);
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Файл не удалось прочитать.");
        }
    }
    public InputStreamResource exportFile() throws FileProcessingException,FileNotFoundException {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMapper.fromMapOfSocks(socksMap));
            fileService.saveToFile(json);
            return fileService.exportFile();
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Файл не удалось сохранить.")
        }
    }
    public void importOperations(MultipartFile multipartFile) throws FileProcessingException,FileNotFoundException {
        String json = fileService.importOperations(multipartFile);
        try{
            List<SocksDto>transactionDtoList=new ObjectMapper().readValue(json, new TypeReference<ArrayList<SocksTransactionDto>>() {
            })
        }
    }
}
