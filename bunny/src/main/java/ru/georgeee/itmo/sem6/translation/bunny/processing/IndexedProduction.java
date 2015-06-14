package ru.georgeee.itmo.sem6.translation.bunny.processing;

import lombok.Getter;
import ru.georgeee.itmo.sem6.translation.bunny.grammar.Production;

class IndexedProduction {
    @Getter
    private final Production production;
    @Getter
    private final int index;

    public IndexedProduction(Production production, int index) {
        if (index > production.size()) {
            throw new IllegalArgumentException("Can't create indexed production: index=" + index + " production=" + production);
        }
        this.production = production;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedProduction that = (IndexedProduction) o;

        if (index != that.index) return false;
        if (!production.equals(that.production)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "{" + production +
                ", " + index +
                '}';
    }

    @Override
    public int hashCode() {
        int result = production.hashCode();
        result = 31 * result + index;
        return result;
    }

    public IndexedProduction next() {
        return new IndexedProduction(production, index + 1);
    }
}
