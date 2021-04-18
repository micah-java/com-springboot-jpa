package com.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class UserBatchChunkListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        System.out.println("userBatchChunkListener beforeChunk");
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        System.out.println("userBatchChunkListener afterChunk");
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }
}
