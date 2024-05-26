package com.combatreforged.metis.api.exception;

public class MetisPluginException extends RuntimeException {
    public MetisPluginException(String message) {
        super(message);
    }

    public MetisPluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
