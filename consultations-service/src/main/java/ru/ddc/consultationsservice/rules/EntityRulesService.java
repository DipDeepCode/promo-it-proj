package ru.ddc.consultationsservice.rules;

public abstract class EntityRulesService<S, T> {

    public T canCreate(S entity) {
        return getRuleResponse(entity, "create");
    }

    public T canUpdate(S entity) {
        return getRuleResponse(entity, "update");
    }

    public T canDelete(S entity) {
        return getRuleResponse(entity, "delete");
    }

    protected abstract T getRuleResponse(S entity, String groupName);
}
