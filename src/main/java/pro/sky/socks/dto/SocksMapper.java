package pro.sky.socks.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.socks.model.OperationType;
import pro.sky.socks.model.Sock;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface SocksMapper {
    Sock toSocks(SocksDto socksDto);

    @Mapping(target = "quantity", ignore = true);

    SocksDto toSocksDto(Sock sock);

    default List<SocksDto> fromMapOfSocks(Map<Sock, Integer> socks) {
        List<SocksDto> sockList = new ArrayList<>();
        socks.forEach(k,v) -> {
            SocksDto socksDto = toSocksDto(k);
            socksDto.setQuantity(v);
            sockList.add(socksDto);
        });
        return sockList;
    }

    default Map<Sock, Integer> fromListOfSocksDto(List<SocksDto>) {

    }
}
