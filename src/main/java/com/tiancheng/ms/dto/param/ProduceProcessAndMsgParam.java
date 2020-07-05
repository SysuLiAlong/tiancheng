package com.tiancheng.ms.dto.param;

import com.tiancheng.ms.entity.ProduceMsgEntity;
import com.tiancheng.ms.entity.ProduceProcessEntity;
import lombok.Data;

@Data
public class ProduceProcessAndMsgParam {

    public ProduceProcessEntity produceProcessEntity;

    public ProduceMsgEntity produceMsgEntity;
}
