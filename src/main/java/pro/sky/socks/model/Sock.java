package pro.sky.socks.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class Sock {
    private Color color;
    private SocksSize socksSize;
    public Integer cottonContent;

}
