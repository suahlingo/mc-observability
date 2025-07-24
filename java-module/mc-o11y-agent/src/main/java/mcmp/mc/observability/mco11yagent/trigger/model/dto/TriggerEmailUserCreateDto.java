package mcmp.mc.observability.mco11yagent.trigger.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TriggerEmailUserCreateDto {

    @JsonIgnore
    @Schema(hidden = true)
    private Long policySeq;

    @Schema(description = "Trigger Alert Email User name", example = "alert-user")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Alert Receiver Email", example = "user@example.com")
    @JsonProperty("email")
    private String email;
}
