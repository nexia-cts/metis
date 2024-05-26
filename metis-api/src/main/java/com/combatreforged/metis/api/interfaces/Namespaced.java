package com.combatreforged.metis.api.interfaces;

import com.combatreforged.metis.api.util.Identifier;
import com.combatreforged.metis.api.util.ImplementationUtils;

public interface Namespaced {
    default Identifier getNamespaceId() {
        return ImplementationUtils.getInstance().getIdentifier((com.combatreforged.metis.api.entrypoint.interfaces.Namespaced) this);
    }
}
