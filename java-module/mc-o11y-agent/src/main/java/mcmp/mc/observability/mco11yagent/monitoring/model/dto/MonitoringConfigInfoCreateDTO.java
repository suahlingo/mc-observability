package mcmp.mc.observability.mco11yagent.monitoring.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MonitoringConfigInfoCreateDTO {


    @JsonProperty("name")
    private String name;

    @JsonProperty("plugin_seq")
    private Long pluginSeq;


    @JsonProperty("plugin_config")
    private String pluginConfig;
}
