package com.aspodev.DTO;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.SCAR.Slice;

public class InheritanceInfoDTO {
    private String type;
    private List<InheritanceRelationDTO> list;

    public InheritanceInfoDTO(Slice slice) {
        this.type = slice.getMetaData().type().toString();
        this.list = CreateList(slice);
    }

    public String getType() {
        return type;
    }

    public List<InheritanceRelationDTO> getList() {
        return list;
    }

    private List<InheritanceRelationDTO> CreateList(Slice slice) {
        List<InheritanceRelationDTO> list = new ArrayList<>();
        if (slice.getParentName() != null) {
            InheritanceRelationDTO parent = new InheritanceRelationDTO(slice.getParentName(), "extends");
            list.add(parent);
        }
        for (String interfaceName : slice.getInterfaces()) {
            InheritanceRelationDTO interfaceRelation = new InheritanceRelationDTO(interfaceName, "implements");
            list.add(interfaceRelation);
        }
        return list;
    }

}