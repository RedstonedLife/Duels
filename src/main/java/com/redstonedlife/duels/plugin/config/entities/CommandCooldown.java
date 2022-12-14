package com.redstonedlife.duels.plugin.config.entities;

import com.redstonedlife.duels.plugin.config.processors.DeleteIfIncompleteProcessor;

import java.util.regex.Pattern;

public class CommandCooldown implements DeleteIfIncompleteProcessor.IncompleteEntity {
    private Pattern pattern;

    public Pattern pattern() {return this.pattern;}
    public void pattern(final Pattern value) {this.pattern = value;}

    private Long value;

    public Long value() {return this.value;}
    public void value(final Long value) {this.value = value;}

    @Override public boolean isIncomplete() {return value == null || pattern == null;}
}
