package mcmp.mc.observability.mco11yagent.trigger.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Encode;
import mcmp.mc.observability.mco11yagent.trigger.common.TriggerConstants;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerHistoryInfo;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TriggerConstants.TRIGGER_URI + "/history")
public class TriggerHistoryController {

    private final TriggerHistoryService triggerHistoryService;

    @Operation(summary = "Get Trigger History all list")
    @TriggerBase64Encode
    @GetMapping
    public ResBody<List<TriggerHistoryInfo>> list(@RequestParam("policySeq") Long policySeq) {
        ResBody<List<TriggerHistoryInfo>> res = new ResBody<>();
        res.setData(triggerHistoryService.getList(policySeq));
        return res;
    }

    @Hidden // OpenAPI 문서에서 숨김 처리
    @TriggerBase64Encode
    @GetMapping("/{historySeq}")
    public ResBody<TriggerHistoryInfo> detail(@PathVariable("historySeq") Long seq) {
        return triggerHistoryService.getDetail(new ResBody<>(), seq);
    }
}
