package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64EncodeField;
import mcmp.mc.observability.mco11yagent.trigger.enums.TaskStatus;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerPolicyCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerPolicyUpdateDto;
import java.util.*;
import java.util.stream.Collectors;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerPolicyInfo {

    @Schema(description = "Sequence by trigger policy", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Base64 Encoded value", example = "Y3B1IHVzYWdlX2lkbGUgY2hlY2sgcG9saWN5")
    @TriggerBase64EncodeField
    @JsonProperty("name")
    private String name;

    @Schema(description = "Host description", example = "ZGVzY3JpcHRpb24=")
    @TriggerBase64EncodeField
    @JsonProperty("description")
    private String description;

    @Schema(description = "Trigger target metric", example = "cpu")
    @JsonProperty("measurement")
    private String metric;

    @Schema(description = "Trigger target metric field", example = "usage_idle")
    @JsonProperty("field")
    private String field;

    @Schema(description = "Trigger target metric statistics", example = "min")
    @JsonProperty("statistics")
    private String statistics;

    @Schema(description = "Thresholds for CRIT/WARN/INFO", example = "{\"crit\": \"value >= 100\", \"warn\": \"value > 99.9\"}")
    @TriggerBase64EncodeField
    @JsonProperty("threshold")
    private String threshold;

    @Schema(description = "Agent Manager IP (internal use only)", hidden = true, example = "http://localhost:18080")
    @JsonProperty("agent_manager_ip")
    private String agentManagerIp;

    @Schema(description = "Trigger Policy enablement status", example = "ENABLED")
    @JsonProperty("status")
    private TaskStatus status;

    @Schema(description = "Group by fields", example = "[\"cpu\"]", hidden = true)
    private List<String> groupFields;

    @JsonIgnore
    private String tickScript;

    @Schema(description = "Created at", example = "2024-05-24T11:31:55Z")
    @JsonProperty("create_at")
    private String createAt;

    @Schema(description = "Last updated at", example = "2024-05-24T11:31:55Z")
    @JsonProperty("update_at")
    private String updateAt;

    public void setCreateDto(TriggerPolicyCreateDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.metric = dto.getMetric();
        this.groupFields = dto.getGroupFields();
        this.threshold = dto.getThreshold();
        this.field = dto.getField();
        this.statistics = dto.getStatistics();
        this.status = dto.getStatus();
    }

    public void setUpdateDto(TriggerPolicyUpdateDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getDescription() != null) this.description = dto.getDescription();
        if (dto.getMetric() != null) this.metric = dto.getMetric();
        if (dto.getGroupFields() != null) this.groupFields = dto.getGroupFields();
        if (dto.getThreshold() != null) this.threshold = dto.getThreshold();
        if (dto.getField() != null) this.field = dto.getField();
        if (dto.getStatistics() != null) this.statistics = dto.getStatistics();
        if (dto.getStatus() != null) this.status = dto.getStatus();
    }

    public void makeTickScript(TriggerPolicyInfo policy) {
        String tick =
            """
            var db = '@DATABASE'
            var rp = '@RETENTION_POLICY'
            var measurement = '@MEASUREMENT'
            var groupBy = @GROUP_BY

            var streamData = stream
                |from()
                    .database(db)
                    .retentionPolicy(rp)
                    .measurement(measurement)
                    .groupBy(groupBy)
                    .where(lambda: isPresent("@FIELD")@WHERE_CONDITION)
                |eval()
                    .keep("@FIELD")

            var data = streamData
                |@STATISTICS("@FIELD")
                    .as("value")

            var trigger = data
                |alert()
            @ALERT_CONDITION
                    .id('{{ .TaskName }}')
                    .stateChangesOnly()
                    .post('@AGENT_MANAGER_IP/api/o11y/trigger/policy/receiver')

            trigger
                |httpOut('output')
            """;

        String where = "cpu".equals(policy.getMetric()) ? " AND \"cpu\" != 'cpu-total'" : "";

        this.tickScript = tick
            .replaceAll("@DATABASE", "")
            .replaceAll("@RETENTION_POLICY", "")
            .replaceAll("@MEASUREMENT", policy.getMetric())
            .replaceAll("@GROUP_BY", convertListToString(policy.getGroupFields()))
            .replaceAll("@WHERE_CONDITION", where)
            .replaceAll("@FIELD", policy.getField())
            .replaceAll("@STATISTICS", policy.getStatistics())
            .replaceAll("@ALERT_CONDITION", getAlertCondition(policy))
            .replaceAll("@AGENT_MANAGER_IP", this.agentManagerIp);
    }

    public void setTickScriptStorageInfo(String database, String retentionPolicy) {
        this.tickScript = this.tickScript
            .replaceAll("@DATABASE", database)
            .replaceAll("@RETENTION_POLICY", retentionPolicy);
    }

    public String convertListToString(List<String> groupByFields) {
        List<String> fields = new ArrayList<>(List.of("target_id", "ns_id"));
        if (groupByFields != null && !groupByFields.isEmpty()) {
            fields.addAll(groupByFields);
        }
        return fields.stream()
            .map(f -> "'" + f + "'")
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private String getAlertCondition(TriggerPolicyInfo policy) {
        Map<String, String> thresholds = parseThresholds(policy.getThreshold());
        StringBuilder sb = new StringBuilder();
        for (String level : List.of("crit", "warn", "info")) {
            if (thresholds.containsKey(level)) {
                sb.append("        .").append(level)
                    .append("(lambda: ")
                    .append(thresholds.get(level).replace("value", "\"value\""))
                    .append(")\n");
            }
        }
        return sb.toString();
    }

    private Map<String, String> parseThresholds(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Invalid threshold JSON: " + json, e);
        }
    }
}
