package mcmp.mc.observability.mco11yagent.monitoring.service.Interface;

import java.util.List;
import mcmp.mc.observability.mco11yagent.monitoring.model.TargetInfo;

public interface TargetService {

  List<TargetInfo> getList();

  List<TargetInfo> getList(String nsId, String mciId);

  TargetInfo getTarget(String nsId, String mciId, String targetId);

  int insert(TargetInfo targetInfo);

  int update(TargetInfo targetInfo);

  int delete(TargetInfo targetInfo);

}
