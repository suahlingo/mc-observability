package mcmp.mc.observability.mco11yagent.monitoring.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcmp.mc.observability.mco11yagent.monitoring.enums.ResultCode;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBConnector;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MonitoringConfigInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.TargetInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.monitoring.service.InfluxDBServiceImpl;
import mcmp.mc.observability.mco11yagent.monitoring.service.MonitoringConfigService;
import mcmp.mc.observability.mco11yagent.monitoring.service.TargetServiceImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetFacadeService {

  private final InfluxDBServiceImpl influxDBService;
  private final TargetServiceImpl targetService;
  private final MonitoringConfigService monitoringConfigService;

  public ResBody<List<TargetInfo>> getAllTargetList() {
    ResBody<List<TargetInfo>> resBody = new ResBody<>();
    resBody.setData(targetService.getList());
    return resBody;
  }

  public ResBody<List<TargetInfo>> getByNsAndMciList(String nsId, String mciId) {
    ResBody<List<TargetInfo>> resBody = new ResBody<>();
    resBody.setData(targetService.getList(nsId, mciId));
    return resBody;
  }

  public ResBody<TargetInfo> fetchTarget(String nsId, String mciId, String targetId) {
    ResBody<TargetInfo> resBody = new ResBody<>();
    resBody.setData(targetService.getTarget(nsId, mciId, targetId));
    return resBody;
  }

  public ResBody<?> insertTarget(String nsId, String mciId, String targetId, TargetInfo targetInfo) {
    targetInfo.setNsId(nsId);
    targetInfo.setMciId(mciId);
    targetInfo.setId(targetId);

    if (targetService.insert(targetInfo) > 0) {
      syncInfluxDBList();
      return new ResBody<>(ResultCode.SUCCESS);
    } else {
      return new ResBody<>(ResultCode.FAILED);
    }
  }

  public ResBody<?> updateTarget(String nsId, String mciId, String targetId, TargetInfo targetInfo) {
    targetInfo.setNsId(nsId);
    targetInfo.setMciId(mciId);
    targetInfo.setId(targetId);

    if (targetService.update(targetInfo) > 0) {
      syncInfluxDBList();
      return new ResBody<>(ResultCode.SUCCESS);
    } else {
      return new ResBody<>(ResultCode.DATABASE_ERROR);
    }
  }

  public ResBody<?> removeTarget(String nsId, String mciId, String targetId) {
    TargetInfo targetInfo = new TargetInfo();
    targetInfo.setNsId(nsId);
    targetInfo.setMciId(mciId);
    targetInfo.setId(targetId);

    if (targetService.delete(targetInfo) > 0) {
      syncInfluxDBList();
      return new ResBody<>(ResultCode.SUCCESS);
    } else {
      return new ResBody<>(ResultCode.DATABASE_ERROR);
    }
  }

  private void syncInfluxDBList() {
    List<MonitoringConfigInfo> configInfos = monitoringConfigService.list(null, null, null);

    List<InfluxDBInfo> influxDBInfoList = configInfos.stream()
        .filter(f -> f.getPluginName().equalsIgnoreCase("influxdb"))
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
  }



}
