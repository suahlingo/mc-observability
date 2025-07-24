package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerEmailUserCreateDto;

@Data
public class TriggerEmailUserInfo {

    @Schema(description = "Sequence by Trigger Email User", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Sequence by trigger policy", example = "1")
    @JsonProperty("policy_seq")
    private Long policySeq;

    @Schema(description = "Trigger Alert Email User name", example = "Alice")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Alert Receiver Email", example = "alice@example.com")
    @JsonProperty("email")
    private String email;

    public void setCreateDto(TriggerEmailUserCreateDto dto) {
        this.policySeq = dto.getPolicySeq();
        this.name = dto.getName();
        this.email = dto.getEmail();
    }
}
