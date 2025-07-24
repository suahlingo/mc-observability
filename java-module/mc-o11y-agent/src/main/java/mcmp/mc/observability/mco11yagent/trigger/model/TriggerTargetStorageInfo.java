package mcmp.mc.observability.mco11yagent.trigger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBConnector;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTargetStorageInfo {

    @Schema(description = "Sequence by trigger target storage", example = "1")
    @JsonProperty("seq")
    private Long seq;

    @Schema(description = "Sequence by trigger target", example = "1")
    @JsonProperty("target_seq")
    private Long targetSeq;

    @Schema(description = "Storage URL", example = "http://localhost:8086")
    @JsonProperty("url")
    private String url;

    @Schema(description = "Storage database name", example = "mc_agent")
    @JsonProperty("database")
    private String database;

    @Schema(description = "Retention policy name", example = "autogen")
    @JsonProperty("retention_policy")
    private String retentionPolicy;

    @Schema(description = "Created at timestamp", example = "2024-05-24T11:31:55Z")
    @JsonProperty("create_at")
    private String createAt;

    public void updateInfluxDBConfig(InfluxDBConnector influxDBConnector) {
        this.url = influxDBConnector.getUrl();
        this.database = influxDBConnector.getDatabase();
        this.retentionPolicy = influxDBConnector.getRetentionPolicy();
    }
}
