package mcmp.mc.observability.mco11yagent.monitoring.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MonitoringConfigInfoUpdateDTO {

    @JsonProperty(value = "seq")
    private Long seq;


    @JsonProperty("plugin_config")
    private String pluginConfig;
}
