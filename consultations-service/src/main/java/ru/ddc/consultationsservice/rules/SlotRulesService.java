package ru.ddc.consultationsservice.rules;

import lombok.RequiredArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.stereotype.Service;
import ru.ddc.consultationsservice.entity.Slot;
import ru.ddc.consultationsservice.exception.ApiException;

@Service
@RequiredArgsConstructor
public class SlotRulesService extends EntityRulesService<Slot, RuleResponse> {
    private final KieContainer kieContainer;

    protected RuleResponse getRuleResponse(Slot slot, String groupName) {
        RuleResponse ruleResponse = new RuleResponse();
        KieBase kBase = kieContainer.getKieBase("slots");
        KieSession session = kBase.newKieSession();
        try {
            Agenda agenda = session.getAgenda();
            agenda.getAgendaGroup(groupName).setFocus();
            session.insert(slot);
            session.setGlobal("response", ruleResponse);
            session.fireAllRules();
        } catch (Exception e) {
            throw new ApiException("Error in SlotRulesService", e.getMessage());
        } finally {
            session.dispose();
        }
        return ruleResponse;
    }
}
