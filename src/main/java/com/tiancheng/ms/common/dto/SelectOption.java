package com.tiancheng.ms.common.dto;

import lombok.Data;

@Data
public class SelectOption {

    private String label;

    private String value;

    public SelectOption() {}

    public SelectOption(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
