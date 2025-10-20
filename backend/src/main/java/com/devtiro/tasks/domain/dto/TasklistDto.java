package com.devtiro.tasks.domain.dto;

import java.util.List;
import java.util.UUID;

public record TasklistDto(
        UUID id,
        String title,
        String description,
        Integer count,
        Double progress,
        List<TaskDto> tasks
) {
}
