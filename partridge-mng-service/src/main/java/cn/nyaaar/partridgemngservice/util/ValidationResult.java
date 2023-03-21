package cn.nyaaar.partridgemngservice.util;

import lombok.Data;

import java.text.MessageFormat;
import java.util.Map;

@Data
public class ValidationResult {

    /**
     * 是否有异常
     */
    private boolean hasErrors;

    /**
     * 异常消息记录
     */
    private Map<String, String> errorMsg;

    /**
     * 获取异常消息组装
     */
    public String getMessageInfo() {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return "";
        }
        StringBuilder message = new StringBuilder();
        errorMsg.forEach((key, value) -> {
            message.append(value);
            message.append("\n");
        });
        return message.toString();
    }

    /**
     * 获取异常消息组装
     */
    public String getMessage() {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return "";
        }
        StringBuilder message = new StringBuilder();
        errorMsg.forEach((key, value) -> {
            message.append(MessageFormat.format("{0}:{1} \r\n", key, value));
        });
        return message.toString();
    }

    /**
     * 获取简单异常消息，返回給前端
     */
    public String getSimpleMessage() {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return "";
        }
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, String> entry : errorMsg.entrySet()) {
            message.append(entry.getValue());
            break;
        }
        return message.toString();
    }

}
