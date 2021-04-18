package com.chunk;

import com.entity.TmUser;
import org.springframework.batch.item.ItemProcessor;

public class TmUserProcessorConverter implements ItemProcessor<ExternalSourceData, TmUser> {
    @Override
    public TmUser process(ExternalSourceData externalSourceData) throws Exception {
        if(externalSourceData instanceof TmUser){
            TmUser tmUser = (TmUser) externalSourceData;
            System.out.println("process tmUser:"+tmUser);
            return tmUser;
        }
        return null;
    }
}
