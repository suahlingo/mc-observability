package mcmp.mc.observability.mco11yagent.trigger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManageTriggerTargetDto {

    @Schema(description = "Namespace ID", example = "ns-abc123")
    @JsonProperty("ns_id")
    private String nsId;

    @Schema(description = "VM or Target ID", example = "vm-001")
    @JsonProperty("target_id")
    private String targetId;
}
