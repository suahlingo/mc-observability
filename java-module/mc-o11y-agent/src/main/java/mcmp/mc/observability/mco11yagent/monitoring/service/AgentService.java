package mcmp.mc.observability.mco11yagent.monitoring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcmp.mc.observability.mco11yagent.monitoring.common.Constants;
import mcmp.mc.observability.mco11yagent.monitoring.mapper.TargetMapper;
import mcmp.mc.observability.mco11yagent.monitoring.util.CollectorExecutor;
import mcmp.mc.observability.mco11yagent.monitoring.util.Utils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService {

    @Value("${spring.datasource.url:0.0.0.0}")
    private String datasourceUrl;

    private final CollectorExecutor collectorExecutor;
    private final TargetMapper targetMapper;

    @EventListener(ContextRefreshedEvent.class)
    public void onStartup() {
        if (datasourceUrl.contains(Constants.EMPTY_HOST)) {
            log.warn("Invalid datasource host. Skipping agent initialization.");
            return;
        }

        try {
            createConfigFiles();

            Utils.writeFile(
                collectorExecutor.globalTelegrafConfig(),
                Constants.COLLECTOR_CONFIG_PATH
            );

            collectorExecutor.startCollector();

            updateTargetState("ACTIVE");

        } catch (IOException e) {
            log.error("Failed to initialize AgentService: {}", ExceptionUtils.getMessage(e), e);
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onShutdown() {
        updateTargetState("INACTIVE");
    }

    private void createConfigFiles() throws IOException {
        File confRootDirectory = new File(Constants.CONFIG_ROOT_PATH);
        if (!confRootDirectory.exists()) confRootDirectory.mkdir();

        File confDirectory = new File(Constants.COLLECTOR_CONFIG_DIR_PATH);
        if (!confDirectory.exists()) confDirectory.mkdir();

        File confFile = new File(Constants.COLLECTOR_CONFIG_PATH);
        if (!confFile.exists()) confFile.createNewFile();
    }

    private void updateTargetState(String state) {
        String nsId = Optional.ofNullable(System.getenv(Constants.PROPERTY_NS_ID)).orElse("");
        String mciId = Optional.ofNullable(System.getenv(Constants.PROPERTY_MCI_ID)).orElse("");
        String targetId = Optional.ofNullable(System.getenv(Constants.PROPERTY_TARGET_ID)).orElse("");

        targetMapper.updateState(nsId, mciId, targetId, state);
    }
}
