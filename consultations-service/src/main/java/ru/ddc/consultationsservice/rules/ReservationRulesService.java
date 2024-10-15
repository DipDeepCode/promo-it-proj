package ru.ddc.consultationsservice.rules;

import lombok.RequiredArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.Agenda;
import org.springframework.stereotype.Service;
import ru.ddc.consultationsservice.entity.Reservation;
import ru.ddc.consultationsservice.exception.ApiException;

@Service
@RequiredArgsConstructor
public class ReservationRulesService extends EntityRulesService<Reservation, RuleResponse> {
    private final KieContainer kieContainer;

    protected RuleResponse getRuleResponse(Reservation reservation, String groupName) {
        RuleResponse ruleResponse = new RuleResponse();
        KieBase kieBase = kieContainer.getKieBase("reservations");
        KieSession session = kieBase.newKieSession();
        try {
            Agenda agenda = session.getAgenda();
            agenda.getAgendaGroup(groupName).setFocus();
            session.insert(reservation);
            session.insert(reservation.getSlot());
            session.setGlobal("response", ruleResponse);
            session.fireAllRules();
        } catch (Exception e) {
            throw new ApiException("Error in ReservationRulesService", e.getMessage());
        } finally {
            session.dispose();
        }
        return ruleResponse;
    }
}
