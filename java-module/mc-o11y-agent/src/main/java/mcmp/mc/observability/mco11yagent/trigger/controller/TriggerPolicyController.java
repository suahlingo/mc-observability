package mcmp.mc.observability.mco11yagent.trigger.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Decode;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Encode;
import mcmp.mc.observability.mco11yagent.trigger.common.TriggerConstants;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerPolicyInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerPolicyCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerPolicyUpdateDto;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerPolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TriggerConstants.TRIGGER_URI)
public class TriggerPolicyController {

    private final TriggerPolicyService triggerPolicyService;

    @Operation(summary = "Get all Trigger Policies")
    @TriggerBase64Encode
    @GetMapping
    public ResBody<List<TriggerPolicyInfo>> list() {
        ResBody<List<TriggerPolicyInfo>> res = new ResBody<>();
        res.setData(triggerPolicyService.getList());
        return res;
    }

    @Operation(summary = "Get Trigger Policy details by policySeq")
    @TriggerBase64Encode
    @GetMapping("/{policySeq}")
    public ResBody<TriggerPolicyInfo> detail(@PathVariable("policySeq") Long seq) {
        return triggerPolicyService.getDetail(new ResBody<>(), seq);
    }

    @Operation(summary = "Create a new Trigger Policy")
    @TriggerBase64Decode(TriggerPolicyCreateDto.class)
    @PostMapping
    public ResBody<Void> create(@RequestBody TriggerPolicyCreateDto dto) {
        return triggerPolicyService.createPolicy(dto);
    }

    @Operation(summary = "Update an existing Trigger Policy")
    @TriggerBase64Decode(TriggerPolicyUpdateDto.class)
    @PatchMapping("/{policySeq}")
    public ResBody<Void> update(@PathVariable("policySeq") Long seq,
        @RequestBody TriggerPolicyUpdateDto dto) {
        dto.setSeq(seq);
        return triggerPolicyService.updatePolicy(dto);
    }

    @Operation(summary = "Delete a Trigger Policy")
    @DeleteMapping("/{policySeq}")
    public ResBody<Void> delete(@PathVariable("policySeq") Long policySeq) {
        return triggerPolicyService.deletePolicy(policySeq);
    }
}

