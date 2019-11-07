package com.github.madz0.jerseytest.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {
    private List<T> contents;
    private long totalSize;
    private int page;
    private int pageSize;
}
