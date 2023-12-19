package org.app.datasource.builder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSourceBuilderInfo {
    private String url;
    private String username;
    private String password;

}
