package com.combatreforged.metis.builder.extension.wrap;

public interface ChangeableWrap<T> extends Wrap<T> {
    void setWrap(T wrap);
}
