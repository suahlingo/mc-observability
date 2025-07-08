package mcmp.mc.observability.mco11yagent.trigger.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64DecodeField;

@Getter
@Setter
public class TriggerSlackUserCreateDto {

    @JsonIgnore
    @Schema(hidden = true)
    private Long policySeq;

    @Schema(description = "Trigger Alert Slack User name", example = "slack-user")
    @JsonProperty("name")
    private String name;

    @TriggerBase64DecodeField
    @Schema(description = "Base64 Encoded Slack token", example = "dG9rZW4=")
    @JsonProperty("token")
    private String token;

    @TriggerBase64DecodeField
    @Schema(description = "Base64 Encoded Slack channel", example = "Z2VuZXJhbA==")
    @JsonProperty("channel")
    private String channel;
}
