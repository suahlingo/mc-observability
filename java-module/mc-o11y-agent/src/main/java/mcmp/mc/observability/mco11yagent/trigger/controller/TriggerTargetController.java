package mcmp.mc.observability.mco11yagent.trigger.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Encode;
import mcmp.mc.observability.mco11yagent.trigger.common.TriggerConstants;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerTargetInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.ManageTriggerTargetDto;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerTargetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TriggerConstants.TRIGGER_URI)
public class TriggerTargetController {

    private final TriggerTargetService triggerTargetService;

    @Operation(summary = "Get all trigger targets for a policy")
    @TriggerBase64Encode
    @GetMapping("/target")
    public ResBody<List<TriggerTargetInfo>> list(@RequestParam("policySeq") Long policySeq) {
        ResBody<List<TriggerTargetInfo>> res = new ResBody<>();
        res.setData(triggerTargetService.getList(policySeq));
        return res;
    }

    @Hidden
    @TriggerBase64Encode
    @GetMapping("/target/{targetSeq}")
    public ResBody<TriggerTargetInfo> detail(@PathVariable("targetSeq") Long seq) {
        return triggerTargetService.getDetail(new ResBody<>(), seq);
    }

    @Operation(summary = "Set trigger targets for a policy")
    @PutMapping("/{policySeq}/target")
    public ResBody<Void> setTargets(@PathVariable("policySeq") Long policySeq,
        @RequestBody List<ManageTriggerTargetDto> targets) {
        return triggerTargetService.setTargets(policySeq, targets);
    }
}
