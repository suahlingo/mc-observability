package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64DecodeField;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerSlackUserCreateDto;

@Data
public class TriggerSlackUserInfo {

    @Schema(description = "Sequence by Trigger Slack User", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Sequence by trigger policy", example = "1")
    @JsonProperty("policy_seq")
    private Long policySeq;

    @Schema(description = "Trigger Alert Slack User name", example = "monitoring-bot")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Slack token (Base64 Encoded)", example = "dG9rZW4xMjM=")
    @TriggerBase64DecodeField
    @JsonProperty("token")
    private String token;

    @Schema(description = "Slack channel name (Base64 Encoded)", example = "I21vbnN0ZXI=")
    @TriggerBase64DecodeField
    @JsonProperty("channel")
    private String channel;

    public void setCreateDto(TriggerSlackUserCreateDto dto) {
        this.policySeq = dto.getPolicySeq();
        this.name = dto.getName();
        this.token = dto.getToken();
        this.channel = dto.getChannel();
    }
}
