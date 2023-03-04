package pro.sky.socks.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import pro.sky.socks.model.Color;
import pro.sky.socks.model.SocksSize;

@Getter
@AllArgsConstructor
@Builder
public class SocksDto {
    @NotNull(message = "Цвет-обязательное поле")
    @EnumNamePattern(regexp = "BLACK|RED|YELLOW|BLUE")
    private Color color;
    @NotNull(message = "Размер-обязательное поле")
    @EnumNamePattern(regexp = "S|M|L")
    private SocksSize socksSize;
    @Range(min = 0, max = 100, message = "Содержание хлопка должно быть от 0 до 100")
    private Integer cottonContent;
    @Positive(message = "Количество должно быть положительным числом")
    @Setter
    private Integer quantity;
}
