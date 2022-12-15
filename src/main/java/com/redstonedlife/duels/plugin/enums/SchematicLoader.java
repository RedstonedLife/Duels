package com.redstonedlife.duels.plugin.enums;

public enum SchematicLoader {
    FAWE("FAWE"),
    WE("WE");

    String loader;

    SchematicLoader(String loader) {
        this.loader = loader;
    }

    @Override
    public String toString() {return super.toString();}
}
