package essalud.gob.pe.serverapigateway.dto;

import lombok.Data;

@Data
public class SecurityFilterDto {
    private Integer type;
    private String code;
    private String credentials;
}
