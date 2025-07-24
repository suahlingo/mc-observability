package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTargetInfo {

    @Schema(description = "Sequence by trigger target", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Sequence by trigger policy", example = "1")
    @JsonProperty("policy_seq")
    private Long policySeq;

    @Schema(description = "Namespace ID", example = "ns-abc123")
    @JsonProperty("ns_id")
    private String nsId;

    @Schema(description = "VM or Target ID", example = "vm-001")
    @JsonProperty("target_id")
    private String targetId;

    @Schema(description = "Host name", example = "vm1")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Host alias name", example = "test-vm")
    @JsonProperty("alias_name")
    private String aliasName;

    @Schema(description = "Created at timestamp", example = "2024-05-24T11:31:55Z")
    @JsonProperty("create_at")
    private String createAt;

    @Schema(description = "Updated at timestamp", example = "2024-05-24T11:31:55Z")
    @JsonProperty("update_at")
    private String updateAt;
}
