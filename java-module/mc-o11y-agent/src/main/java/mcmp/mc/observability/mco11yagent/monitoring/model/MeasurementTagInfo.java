package mcmp.mc.observability.mco11yagent.monitoring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Builder
public class MeasurementTagInfo {


    @Schema(description = "influxDB measurement name", example = "cpu")
    @JsonProperty("measurement")
    private String measurement;


    @Schema(description = "influxDB tag list on measurement", example = "[\"cpu\",\"host\"]")
    @JsonProperty("tags")
    private List<String> tags;
}