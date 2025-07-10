package mcmp.mc.observability.mco11yagent.monitoring.service.Interface;

import java.util.List;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBConnector;
import mcmp.mc.observability.mco11yagent.monitoring.model.InfluxDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MeasurementFieldInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MeasurementTagInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MetricInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.MetricsInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.SpiderMonitoringInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.TumblebugMCI;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;

public interface InfluxDBService {

  ResBody<List<InfluxDBInfo>> getList();

  void syncSummaryInfluxDBList(List<InfluxDBInfo> influxDBInfoList);

  ResBody<List<MeasurementFieldInfo>> getFields();

  ResBody<List<MeasurementFieldInfo>> getFields(InfluxDBInfo influxDBInfo);

  ResBody<List<MeasurementTagInfo>> getTags();

  ResBody<List<MeasurementTagInfo>> getTags(InfluxDBInfo influxDBInfo);

  List<MetricInfo> getMetrics(MetricsInfo metricsInfo);

  List<MetricInfo> getMetrics(InfluxDBInfo influxDBInfo, MetricsInfo metricsInfo);

  void writeCPU(InfluxDBConnector influxDBConnector, List<SpiderMonitoringInfo.Data.TimestampValue> timestampValues);

  void writeMem(TumblebugMCI.Vm vm,
      InfluxDBConnector influxDBConnector,
      SpiderMonitoringInfo.Data memData);

  void writeDiskIO(TumblebugMCI.Vm vm,
      InfluxDBConnector influxDBConnector,
      SpiderMonitoringInfo.Data dataReadBytes,
      SpiderMonitoringInfo.Data dataWriteBytes);

  void writeMetrics(TumblebugMCI.Vm vm,
      InfluxDBInfo influxDBInfo,
      String pluginName,
      SpiderMonitoringInfo.Data cpuData,
      SpiderMonitoringInfo.Data diskReadData,
      SpiderMonitoringInfo.Data diskWriteData,
      SpiderMonitoringInfo.Data memData);



}














