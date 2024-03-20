package org.otus.dzmitry.kapachou.highload.pipeline;

import lombok.Data;

@Data
public class StreamPipelineProperties {

    private String inputTopic;
    private String outputTopic;
    private String bootstrapServer;
    private String applicationId;
}
