package mcmp.mc.observability.mco11yagent.monitoring.service.Interface;

import mcmp.mc.observability.mco11yagent.monitoring.model.MiningDBInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.MiningDBSetDTO;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;

public interface MiningDBService {
  ResBody<MiningDBInfo> detail();
  ResBody<Void> updateMiningDB(MiningDBSetDTO info);

}
