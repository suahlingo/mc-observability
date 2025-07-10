package mcmp.mc.observability.mco11yagent.monitoring.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcmp.mc.observability.mco11yagent.monitoring.client.SpiderClient;
import mcmp.mc.observability.mco11yagent.monitoring.enums.ResultCode;
import mcmp.mc.observability.mco11yagent.monitoring.exception.ResultCodeException;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MonitoringConfigInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.SpiderMonitoringInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.TumblebugMCI;
import mcmp.mc.observability.mco11yagent.monitoring.service.InfluxDBService;
import mcmp.mc.observability.mco11yagent.monitoring.service.MonitoringConfigService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxDbFacadeService {

  private final MonitoringConfigService monitoringConfigService;
  private final SpiderClient spiderClient;
  private final InfluxDBService influxDBService;


  public ResBody<List<InfluxDBInfo>> getList() {
    List<MonitoringConfigInfo> storageInfoList = monitoringConfigService.list(null, null, null);
    List<MonitoringConfigInfo> influxConfigs = storageInfoList.stream()
        .filter(f -> f.getPluginName().equalsIgnoreCase("influxdb"))
        .toList();

    List<InfluxDBInfo> influxDBInfoList = influxConfigs.stream()
        .map(config -> {
          InfluxDBConnector connector = new InfluxDBConnector(config.getPluginConfig());
          return InfluxDBInfo.builder()
              .url(connector.getUrl())
              .database(connector.getDatabase())
              .retentionPolicy(connector.getRetentionPolicy())
              .username(connector.getUsername())
              .password(connector.getPassword())
              .build();
        }).distinct().toList();


    return influxDBService.syncSummaryInfluxDBList(influxDBInfoList);
  }

  public void writeCPU(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo, String timeBeforeHour, String intervalMinute) {
    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);
    SpiderMonitoringInfo.Data data = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "cpu", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );
    influxDBService.writeCPU(vm, influxDBInfo, data);
  }

  public void writeMem(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo, String timeBeforeHour, String intervalMinute) {
    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);
    SpiderMonitoringInfo.Data data = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "mem", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );
    influxDBService.writeMem(vm, influxDBInfo, data);
  }

  public void writeDiskIO(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo, String timeBeforeHour, String intervalMinute) {
    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);
    SpiderMonitoringInfo.Data dataRead = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "diskio_read", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );
    SpiderMonitoringInfo.Data dataWrite = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "diskio_write", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );
    influxDBService.writeDiskIO(vm, influxDBInfo, dataRead, dataWrite);
  }


  public void writeMetrics(TumblebugMCI.Vm vm,
      InfluxDBInfo influxDBInfo,
      String pluginName,
      String timeBeforeHour,
      String intervalMinute) {

    if (influxDBInfo == null) {
      throw new ResultCodeException(ResultCode.INVALID_REQUEST, "influxDB info is null");
    }

    SpiderMonitoringInfo.Data cpuData = null;
    SpiderMonitoringInfo.Data memData = null;
    SpiderMonitoringInfo.Data diskReadData = null;
    SpiderMonitoringInfo.Data diskWriteData = null;

    switch (pluginName) {
      case "cpu" -> {
        cpuData = spiderClient.getVMMonitoring(vm.getCspResourceName(), "CPU_USAGE", vm.getConnectionName(), timeBeforeHour, intervalMinute);
        influxDBService.writeCPU(vm, influxDBInfo, cpuData);
      }
      case "mem" -> {
        memData = spiderClient.getVMMonitoring(vm.getCspResourceName(), "MEMORY_USAGE", vm.getConnectionName(), timeBeforeHour, intervalMinute);
        influxDBService.writeMem(vm, influxDBInfo, memData);
      }
      case "diskio" -> {
        diskReadData = spiderClient.getVMMonitoring(vm.getCspResourceName(), "DISK_READ", vm.getConnectionName(), timeBeforeHour, intervalMinute);
        diskWriteData = spiderClient.getVMMonitoring(vm.getCspResourceName(), "DISK_WRITE", vm.getConnectionName(), timeBeforeHour, intervalMinute);
        influxDBService.writeDiskIO(vm, influxDBInfo, diskReadData, diskWriteData);
      }
      default -> log.warn("Unsupported plugin name: {}", pluginName);
    }
  }


}
