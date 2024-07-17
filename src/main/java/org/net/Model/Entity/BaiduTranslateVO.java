package org.net.Model.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaiduTranslateVO {

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("error_code")
    private String error_code;

    @JsonProperty("error_msg")
    private String error_msg;

    @JsonProperty("trans_result")
    private List<TransResult> trans_result;



    @Data
    @NoArgsConstructor
    public static class  TransResult{

        @JsonProperty("src")
        private String src;

        @JsonProperty("dst")
        private String dst;

    }

}
