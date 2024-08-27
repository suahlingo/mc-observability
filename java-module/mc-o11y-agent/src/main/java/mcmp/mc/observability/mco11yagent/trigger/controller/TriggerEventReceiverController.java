package mcmp.mc.observability.mco11yagent.trigger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mcmp.mc.observability.mco11yagent.trigger.common.Constants;
import mcmp.mc.observability.mco11yagent.trigger.model.KapacitorAlertInfo;
import mcmp.mc.observability.mco11yagent.trigger.service.TriggerEventHandlerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.TRIGGER_URI + "/receiver")
public class TriggerEventReceiverController {

    private final TriggerEventHandlerService triggerEventHandlerService;

    @PostMapping
    public void getTriggerEvent(@RequestBody String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KapacitorAlertInfo kapacitorAlertInfo = objectMapper.readValue(data, KapacitorAlertInfo.class);
            triggerEventHandlerService.checkTriggerTarget(kapacitorAlertInfo);
        } catch (Exception e) {

        }
    }

}
