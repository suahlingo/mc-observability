package mcmp.mc.observability.mco11yagent.monitoring.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpiderMetricsRequestDTO {

    private String nsId;

    private String mciId;


    private String targetId;


    private String pluginName;


    private String timeBeforeHour;

    private String intervalMinute;
}
