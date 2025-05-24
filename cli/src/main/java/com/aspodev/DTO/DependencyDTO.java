package com.aspodev.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DependencyDTO {
    private String with;
    private int amount;

    public DependencyDTO(String with) {
        this.with = with;
        this.amount = 1;
    }

    public String getWith() {
        return with;
    }

    public int getAmount() {
        return amount;
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
