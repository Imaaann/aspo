package com.aspodev.DTO;

import java.util.Map;

public class SystemResultDTO {
    private String projectName;
    private SystemMetricsDTO systemMetrics;
    private Map<String, InheritanceInfoDTO> inheritance;
    private Map<String, ClassInfoDTO> classes;

    public SystemResultDTO(String projectName, SystemMetricsDTO systemMetrics,
            Map<String, InheritanceInfoDTO> inheritance,
            Map<String, ClassInfoDTO> classes) {
        this.projectName = projectName;
        this.systemMetrics = systemMetrics;
        this.inheritance = inheritance;
        this.classes = classes;
    }

}
