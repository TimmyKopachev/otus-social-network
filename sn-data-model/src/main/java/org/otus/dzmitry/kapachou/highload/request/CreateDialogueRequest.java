package org.otus.dzmitry.kapachou.highload.request;

import lombok.Data;

import java.util.Set;

@Data
public class CreateDialogueRequest {
    String title;
    Set<Long> attenderIds;
}
