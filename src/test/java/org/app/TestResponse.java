package org.app;

import org.app.annotations.ResultColumn;

public class TestResponse {
    @ResultColumn("ep_createOn")
    String createOn;

    @ResultColumn("name_id")
    String nameId;
}
