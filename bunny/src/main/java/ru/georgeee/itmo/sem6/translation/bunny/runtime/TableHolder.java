package ru.georgeee.itmo.sem6.translation.bunny.runtime;

public class TableHolder {
    final int[][] action;
    final Action[][] actionType;
    final int[][] goTo;

    public TableHolder(int[][] actionTable, Action[][] actionTypeTable, int[][] gotoTable) {
        this.action = actionTable;
        this.actionType = actionTypeTable;
        this.goTo = gotoTable;
    }
}
