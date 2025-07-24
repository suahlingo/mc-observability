package mcmp.mc.observability.mco11yagent.trigger.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.monitoring.model.dto.ResBody;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Decode;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Encode;
import mcmp.mc.observability.mco11yagent.trigger.common.TriggerConstants;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerEmailUserInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerSlackUserInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerEmailUserCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerSlackUserCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerPolicyAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(TriggerConstants.TRIGGER_URI + "/{policySeq}/alert")
public class TriggerPolicyAlertController {

    private final TriggerPolicyAlertService triggerPolicyAlertService;

    @Operation(summary = "Get all Slack alert users for a policy")
    @TriggerBase64Encode
    @GetMapping("/slack")
    public ResBody<List<TriggerSlackUserInfo>> getSlackUserList(@PathVariable("policySeq") Long policySeq) {
        ResBody<List<TriggerSlackUserInfo>> res = new ResBody<>();
        res.setData(triggerPolicyAlertService.getSlackUserList(policySeq));
        return res;
    }

    @Operation(summary = "Create Slack alert user for a policy")
    @TriggerBase64Decode(TriggerSlackUserCreateDto.class)
    @PostMapping("/slack")
    public ResBody<Void> createSlackUser(@PathVariable("policySeq") Long policySeq,
        @RequestBody TriggerSlackUserCreateDto dto) {
        dto.setPolicySeq(policySeq);
        return triggerPolicyAlertService.createSlackUser(dto);
    }

    @Operation(summary = "Delete Slack alert user for a policy")
    @DeleteMapping("/slack/{seq}")
    public ResBody<Void> deleteSlackUser(@PathVariable("policySeq") Long policySeq,
        @PathVariable("seq") Long seq) {
        return triggerPolicyAlertService.deleteSlackUser(seq);
    }

    @Operation(summary = "Get all Email alert users for a policy")
    @TriggerBase64Encode
    @GetMapping("/email")
    public ResBody<List<TriggerEmailUserInfo>> getEmailUserList(@PathVariable("policySeq") Long policySeq) {
        ResBody<List<TriggerEmailUserInfo>> res = new ResBody<>();
        res.setData(triggerPolicyAlertService.getEmailUserList(policySeq));
        return res;
    }

    @Operation(summary = "Create Email alert user for a policy")
    @TriggerBase64Decode(TriggerEmailUserCreateDto.class)
    @PostMapping("/email")
    public ResBody<Void> createEmailUser(@PathVariable("policySeq") Long policySeq,
        @RequestBody TriggerEmailUserCreateDto dto) {
        dto.setPolicySeq(policySeq);
        return triggerPolicyAlertService.createEmailUser(dto);
    }

    @Operation(summary = "Delete Email alert user for a policy")
    @DeleteMapping("/email/{seq}")
    public ResBody<Void> deleteEmailUser(@PathVariable("policySeq") Long policySeq,
        @PathVariable("seq") Long seq) {
        return triggerPolicyAlertService.deleteEmailUser(seq);
    }
}
