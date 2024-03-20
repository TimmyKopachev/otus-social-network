package org.otus.dzmitry.kapachou.highload.pipeline;

import org.apache.kafka.streams.Topology;

import java.util.Properties;

public interface StreamPipeline {

    Properties initProperties();

    Topology buildTopology();
}
