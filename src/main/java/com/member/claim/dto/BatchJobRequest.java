package com.member.claim.dto;

import lombok.Data;

@Data
public class BatchJobRequest {
    private String inputfilepath;
    private String outputFilePath;
}
