package mcmp.mc.observability.mco11yagent.trigger.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64DecodeField;
import mcmp.mc.observability.mco11yagent.trigger.enums.TaskStatus;

import java.util.List;

@Getter
@Setter
public class TriggerPolicyUpdateDto {

    @JsonIgnore
    @Schema(hidden = true)
    private Long seq = 0L;

    @Schema(description = "Base64 Encoded policy name", example = "Y3B1IHBvbGljeQ==")
    @TriggerBase64DecodeField
    @JsonProperty("name")
    private String name;

    @Schema(description = "Base64 Encoded host description", example = "ZGVzY3JpcHRpb24=")
    @TriggerBase64DecodeField
    @JsonProperty("description")
    private String description;

    @Schema(description = "Metric name", example = "cpu")
    @JsonProperty("measurement")
    private String metric;

    @Schema(description = "Field name to apply trigger on", example = "usage_idle")
    @JsonProperty("field")
    private String field;

    @Schema(description = "Fields used for group-by aggregation", example = "[]", hidden = true)
    @JsonProperty("group_fields")
    private List<String> groupFields;

    @Schema(description = "Statistic function for metric", example = "min")
    @JsonProperty("statistics")
    private String statistics;

    @Schema(description = "Base64 Encoded threshold JSON", example = "{\"crit\": \"value >= 100\", \"warn\": \"value > 99.9\"}")
    @TriggerBase64DecodeField
    @JsonProperty("threshold")
    private String threshold;

    @Schema(description = "Trigger policy status", example = "ENABLED")
    @JsonProperty("status")
    private TaskStatus status;
}
