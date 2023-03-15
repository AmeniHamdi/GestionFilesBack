package com.example.csv.domain;


import lombok.Data;

import java.util.List;

@Data
public class GetAllType<T> {
    Long count;
    List<T> rows;
}
