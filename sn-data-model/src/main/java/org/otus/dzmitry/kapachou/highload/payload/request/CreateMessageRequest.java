package org.otus.dzmitry.kapachou.highload.payload.request;

import lombok.Data;

@Data
public class CreateMessageRequest {
    String text;
    Long authorId;
}
