package com.aspodev.DTO;

public class DependencyDTO {
    private String with;
    private int amount;

    public DependencyDTO(String with) {
        this.with = with;
        this.amount = 1;
    }

    public void increaseAmount() {
        this.amount++;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DependencyDTO) {
            DependencyDTO other = (DependencyDTO) obj;
            return this.with.equals(other.with);
        }
        return false;
    }

    public String toString() {
        return "{\n\twith:  %s,\n\tamount:  %d\n}".formatted(with, amount);
    }
}
