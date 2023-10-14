package essalud.gob.pe.serverapigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Config {
    private String id;
    private boolean withParams;

    public Config(String id) {
        this.id = id;
    }
}
