package com.github.hu553in.to_do_list.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TaskSortableField {

    TEXT("text"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    @Override
    public String toString() {
        return fieldName;
    }
}
