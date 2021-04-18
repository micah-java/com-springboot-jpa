package com.chunk;

import com.entity.TmUser;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@StepScope
public class UserBatchDataReader extends AbstractPagingItemReader<ExternalSourceData> implements StepExecutionListener {

    @Override
    protected void doReadPage() {

        if(Objects.isNull(results)){
            results = new CopyOnWriteArrayList<>();
        }else{
            results.clear();
        }
        int page = getPage();
        int pageSize = getPageSize();
        System.out.println("page:"+page+" ,pageSize:"+pageSize);
        List<TmUser> list = new ArrayList<>();
        for(int i = 0 ; i<10;i++){
            TmUser user = new TmUser();
            user.setLoginName("doReadPage xiaoxiao" + i);
            user.setAge(i);
            list.add(user);
        }
        results.addAll(list);
    }

    @Override
    protected void doJumpToPage(int i) {

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        setMaxItemCount(24);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
