package mcmp.mc.observability.mco11yagent.monitoring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcmp.mc.observability.mco11yagent.monitoring.enums.ResultCode;
import mcmp.mc.observability.mco11yagent.monitoring.mapper.TargetMapper;
import mcmp.mc.observability.mco11yagent.monitoring.model.TargetInfo;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.monitoring.service.Interface.TargetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TargetServiceImpl implements TargetService {

    private final TargetMapper targetMapper;

    @Override
    public List<TargetInfo> getList() {
        return targetMapper.getList();
    }

    @Override
    public List<TargetInfo> getList(String nsId, String mciId) {
        return targetMapper.getListNSMCI(nsId, mciId);
    }

    @Override
    public TargetInfo getTarget(String nsId, String mciId, String targetId) {
        return targetMapper.getTarget(nsId, mciId, targetId);
    }

    @Override
    public int insert(TargetInfo targetInfo) {
        return targetMapper.insert(targetInfo);
    }

    @Override
    public int update(TargetInfo targetInfo) {
        return targetMapper.update(targetInfo);
    }

    @Override
    public int delete(TargetInfo targetInfo) {
        return targetMapper.delete(targetInfo);
    }

}
