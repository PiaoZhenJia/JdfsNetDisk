package com.company.jdfs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAttribute {
    Boolean isFolder;
    String name;
    Number length;
}
