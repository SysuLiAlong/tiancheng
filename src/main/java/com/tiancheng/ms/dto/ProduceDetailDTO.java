package com.tiancheng.ms.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProduceDetailDTO {

    private ProduceDTO produceDTO;

    private List<ProduceProductDTO> produceProductDTOs;

    public ProduceDetailDTO(ProduceDTO produceDTO, List<ProduceProductDTO> produceProductDTOs) {
        this.produceDTO = produceDTO;
        this.produceProductDTOs = produceProductDTOs;
    }

}
