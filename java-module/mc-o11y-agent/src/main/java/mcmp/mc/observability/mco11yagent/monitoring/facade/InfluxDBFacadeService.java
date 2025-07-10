package mcmp.mc.observability.mco11yagent.monitoring.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcmp.mc.observability.mco11yagent.monitoring.client.SpiderClient;
import mcmp.mc.observability.mco11yagent.monitoring.client.TumblebugClient;
import mcmp.mc.observability.mco11yagent.monitoring.enums.ResultCode;
import mcmp.mc.observability.mco11yagent.monitoring.exception.ResultCodeException;
import mcmp.mc.observability.mco11yagent.monitoring.mapper.MiningDBMapper;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBConnector;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MeasurementFieldInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MeasurementTagInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MiningDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MonitoringConfigInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.SpiderMonitoringInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.TumblebugMCI;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.SpiderMetricsRequestDTO;
import mcmp.mc.observability.mco11yagent.monitoring.service.InfluxDBServiceImpl;
import mcmp.mc.observability.mco11yagent.monitoring.service.MonitoringConfigService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxDBFacadeService {

  private final MonitoringConfigService monitoringConfigService;
  private final SpiderClient spiderClient;
  private final InfluxDBServiceImpl influxDBService;
  private final TumblebugClient tumblebugClient;
  private final MiningDBMapper miningDBMapper;


  public ResBody<List<InfluxDBInfo>> getSpiderDbList() {
    List<MonitoringConfigInfo> storageInfoList = monitoringConfigService.list(null, null, null);
    storageInfoList = storageInfoList.stream()
        .filter(f -> f.getPluginName().equalsIgnoreCase("influxdb"))
        .toList();

    List<InfluxDBInfo> influxDBInfoList = storageInfoList.stream()
        .map(info -> {
          InfluxDBConnector con = new InfluxDBConnector(info.getPluginConfig());
          return InfluxDBInfo.builder()
              .url(con.getUrl())
              .database(con.getDatabase())
              .retentionPolicy(con.getRetentionPolicy())
              .username(con.getUsername())
              .password(con.getPassword())
              .build();
        })
        .distinct()
        .toList();

    influxDBService.syncSummaryInfluxDBList(influxDBInfoList);

    return influxDBService.getList();
  }


  public void writeSpiderCPU(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo,
      String timeBeforeHour, String intervalMinute) {

    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);
    SpiderMonitoringInfo.Data cpuData = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "CPU_USAGE", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );

    InfluxDBConnector connector = new InfluxDBConnector(influxDBInfo);
    influxDBService.writeCPU(connector, cpuData.getTimestampValues());
  }


  public void writeSpiderMem(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo,
      String timeBeforeHour, String intervalMinute) {

    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);
    SpiderMonitoringInfo.Data memData = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "MEMORY_USAGE", vm.getConnectionName(), timeBeforeHour,
        intervalMinute
    );

    InfluxDBConnector connector = new InfluxDBConnector(influxDBInfo);
    influxDBService.writeMem(vm, connector, memData);
  }


  public void writeSpiderDiskIO(String nsId, String mciId, String targetId, InfluxDBInfo influxDBInfo,
      String timeBeforeHour, String intervalMinute) {

    TumblebugMCI.Vm vm = tumblebugClient.getVM(nsId, mciId, targetId);

    SpiderMonitoringInfo.Data dataRead = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "DISK_READ", vm.getConnectionName(), timeBeforeHour, intervalMinute
    );
    SpiderMonitoringInfo.Data dataWrite = spiderClient.getVMMonitoring(
        vm.getCspResourceName(), "DISK_WRITE", vm.getConnectionName(), timeBeforeHour,
        intervalMinute
    );

    InfluxDBConnector connector = new InfluxDBConnector(influxDBInfo);
    influxDBService.writeDiskIO(vm, connector, dataRead, dataWrite);
  }




  public void writeSpiderMetrics(SpiderMetricsRequestDTO request, InfluxDBInfo influxDBInfo) {
    if (influxDBInfo == null) {
      throw new ResultCodeException(ResultCode.INVALID_REQUEST, "influxDB info is null");
    }

    TumblebugMCI.Vm vm = tumblebugClient.getVM(
        request.getNsId(),
        request.getMciId(),
        request.getTargetId()
    );

    String plugin = request.getPluginName().toLowerCase();
    InfluxDBConnector connector = new InfluxDBConnector(influxDBInfo);

    switch (plugin) {
      case "cpu" -> {
        SpiderMonitoringInfo.Data data = spiderClient.getVMMonitoring(
            vm.getCspResourceName(), "CPU_USAGE", vm.getConnectionName(),
            request.getTimeBeforeHour(), request.getIntervalMinute()
        );
        influxDBService.writeCPU(connector, data.getTimestampValues());
      }
      case "mem" -> {
        SpiderMonitoringInfo.Data data = spiderClient.getVMMonitoring(
            vm.getCspResourceName(), "MEMORY_USAGE", vm.getConnectionName(),
            request.getTimeBeforeHour(), request.getIntervalMinute()
        );
        influxDBService.writeMem(vm, connector, data);
      }
      case "diskio" -> {
        SpiderMonitoringInfo.Data read = spiderClient.getVMMonitoring(
            vm.getCspResourceName(), "DISK_READ", vm.getConnectionName(),
            request.getTimeBeforeHour(), request.getIntervalMinute()
        );
        SpiderMonitoringInfo.Data write = spiderClient.getVMMonitoring(
            vm.getCspResourceName(), "DISK_WRITE", vm.getConnectionName(),
            request.getTimeBeforeHour(), request.getIntervalMinute()
        );
        influxDBService.writeDiskIO(vm, connector, read, write);
      }
      default -> log.warn("Unsupported plugin name: {}", plugin);
    }
  }



  public ResBody<List<MeasurementFieldInfo>> getMeasurementFields() {
    MiningDBInfo miningDBInfo = miningDBMapper.getDetail();

    if (miningDBInfo == null) {
      throw new ResultCodeException(ResultCode.INVALID_REQUEST, "miningDB info null");
    }

    InfluxDBInfo influxDBInfo = InfluxDBInfo.builder()
        .url(miningDBInfo.getUrl())
        .database(miningDBInfo.getDatabase())
        .retentionPolicy(miningDBInfo.getRetentionPolicy())
        .username(miningDBInfo.getUsername())
        .password(miningDBInfo.getPassword())
        .build();

    return influxDBService.getFields(influxDBInfo);
  }

  public ResBody<List<MeasurementTagInfo>> getMeasurementTags() {
    MiningDBInfo miningDBInfo = miningDBMapper.getDetail();

    if (miningDBInfo == null) {
      throw new ResultCodeException(ResultCode.INVALID_REQUEST, "miningDB info null");
    }

    InfluxDBInfo influxDBInfo = InfluxDBInfo.builder()
        .url(miningDBInfo.getUrl())
        .database(miningDBInfo.getDatabase())
        .retentionPolicy(miningDBInfo.getRetentionPolicy())
        .username(miningDBInfo.getUsername())
        .password(miningDBInfo.getPassword())
        .build();

    return influxDBService.getTags(influxDBInfo);
  }
}
