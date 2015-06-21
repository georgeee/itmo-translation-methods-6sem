package ru.georgeee.itmo.sem6.translation.bunny.runtime;

import java.io.Serializable;

public class TableHolder implements Serializable{
    private static final long serialVersionUID = -8213316307704510988L;
    final int[][] action;
    final Action[][] actionType;
    final int[][] goTo;

    public TableHolder(int[][] actionTable, Action[][] actionTypeTable, int[][] gotoTable) {
        this.action = actionTable;
        this.actionType = actionTypeTable;
        this.goTo = gotoTable;
    }
}
