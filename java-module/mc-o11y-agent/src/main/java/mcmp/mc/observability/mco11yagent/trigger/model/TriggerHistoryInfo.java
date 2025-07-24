package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64EncodeField;

@Data
@Builder
public class TriggerHistoryInfo {

    @Schema(description = "Sequence by history", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Sequence by trigger policy", example = "1")
    @JsonProperty("policy_seq")
    private Long policySeq;

    @Schema(description = "Sequence by trigger target", example = "1")
    @JsonProperty("target_seq")
    private Long targetSeq;

    @Schema(description = "Namespace Id", example = "ns-abc123")
    @JsonProperty("ns_id")
    private String nsId;

    @Schema(description = "VM or Target Id", example = "vm-001")
    @JsonProperty("target_id")
    private String targetId;

    @Schema(description = "Triggered metric name", example = "cpu")
    @JsonProperty("measurement")
    private String metric;

    @Schema(description = "Alarm detail data (raw or encoded)")
    @JsonProperty("data")
    private String data;

    @Schema(description = "Trigger severity level", example = "CRITICAL")
    @JsonProperty("level")
    private String level;

    @Schema(description = "Base64 Encoded display name", example = "localhost")
    @TriggerBase64EncodeField
    @JsonProperty("name")
    private String name;

    @Schema(description = "Timestamp when the history was saved", example = "2024-05-24T11:31:55Z")
    @JsonProperty("create_at")
    private String createAt;

    @Schema(description = "Timestamp when the trigger event occurred", example = "2024-05-24T11:31:55Z")
    @JsonProperty("occur_time")
    private String occurTime;
}
