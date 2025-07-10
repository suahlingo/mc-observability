package mcmp.mc.observability.mco11yagent.monitoring.service.Interface;

import java.util.List;
import mcmp.mc.observability.mco11yagent.monitoring.model.PluginDefInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;

public interface PluginService {

  ResBody<List<PluginDefInfo>> getList();

}
