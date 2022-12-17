package com.redstonedlife.duels.api;

import java.util.Locale;

import static com.redstonedlife.duels.plugin.I18n.tl;

public class MaxMoneyException extends Exception { public MaxMoneyException(Locale lang) {super(tl((lang == null) ? Locale.ENGLISH : lang,"maxMoney"));}}
