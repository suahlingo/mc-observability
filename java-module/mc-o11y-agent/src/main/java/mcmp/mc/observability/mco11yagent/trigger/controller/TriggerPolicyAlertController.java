package mcmp.mc.observability.mco11yagent.trigger.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Decode;
import mcmp.mc.observability.mco11yagent.trigger.common.Constants;
import mcmp.mc.observability.mco11yagent.trigger.annotation.TriggerBase64Encode;
import mcmp.mc.observability.mco11yagent.trigger.model.ResBody;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerEmailUserInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.TriggerSlackUserInfo;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerEmailUserCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.model.dto.TriggerSlackUserCreateDto;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerPolicyAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.TRIGGER_URI + "/policy/{policySeq}" + "/alert")
public class TriggerPolicyAlertController {

    private final TriggerPolicyAlertService triggerPolicyAlertService;

    @ApiOperation(value = "Get Trigger Alert Slack User all list")
    @TriggerBase64Encode
    @GetMapping("/slack")
    public ResBody<List<TriggerSlackUserInfo>> getSlackUserList(@PathVariable(value = "policySeq") Long policySeq) {
        ResBody<List<TriggerSlackUserInfo>> res = new ResBody<>();
        res.setData(triggerPolicyAlertService.getSlackUserList(policySeq));
        return res;
    }

    @ApiOperation(value = "Create request Trigger Alert Slack User")
    @TriggerBase64Decode(TriggerSlackUserCreateDto.class)
    @PostMapping("/slack")
    public ResBody<Void> createSlackUser(@PathVariable("policySeq") Long policySeq, @RequestBody TriggerSlackUserCreateDto dto) {
        dto.setPolicySeq(policySeq);
        return triggerPolicyAlertService.createSlackUser(dto);
    }

    @ApiOperation(value = "Delete Request Trigger Alert Slack User")
    @DeleteMapping("/slack/{seq}")
    public ResBody<Void> deleteSlackUser(@PathVariable("policySeq") Long policySeq, @PathVariable("seq") Long seq) {
        return triggerPolicyAlertService.deleteSlackUser(seq);
    }

    @ApiOperation(value = "Get Trigger Alert Email User all list")
    @TriggerBase64Encode
    @GetMapping("/email")
    public ResBody<List<TriggerEmailUserInfo>> getEmailUserList(@PathVariable(value = "policySeq") Long policySeq) {
        ResBody<List<TriggerEmailUserInfo>> res = new ResBody<>();
        res.setData(triggerPolicyAlertService.getEmailUserList(policySeq));
        return res;
    }

    @ApiOperation(value = "Create request Trigger Alert Email User")
    @TriggerBase64Decode(TriggerSlackUserCreateDto.class)
    @PostMapping("/email")
    public ResBody<Void> createEmailUser(@PathVariable("policySeq") Long policySeq, @RequestBody TriggerEmailUserCreateDto dto) {
        dto.setPolicySeq(policySeq);
        return triggerPolicyAlertService.createEmailUser(dto);
    }

    @ApiOperation(value = "Delete Request Trigger Alert Email User")
    @DeleteMapping("/email/{seq}")
    public ResBody<Void> deleteEmailUser(@PathVariable("policySeq") Long policySeq, @PathVariable("seq") Long seq) {
        return triggerPolicyAlertService.deleteEmailUser(seq);
    }
}
