package com.member.claim.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class ItemCountListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
    }

    @Override
    public void afterChunk(ChunkContext context) {
        int count = context.getStepContext().getStepExecution().getReadCount();
        int writeCount = context.getStepContext().getStepExecution().getWriteCount();
        log.debug("Read Count: " + count + "  writeCount:  " + writeCount);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
    }
}