package com.episkey.SmartAnswerCloud.exception;

import com.episkey.SmartAnswerCloud.common.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 抛异常工具类
 *
 * @author <a href="https://github.com/Episkey-G">Episkey</a>
 * 
 */
public class ThrowUtils {
    private static final Properties ERROR_PROPERTIES = new Properties();

    static {
        try (InputStream input = ThrowUtils.class.getClassLoader().getResourceAsStream("error_messages.properties")) {
            if (input != null) {
                ERROR_PROPERTIES.load(input);
            }
        } catch (IOException e) {
            // 可以在这里处理配置文件读取失败的情况，例如打印错误日志或者抛出异常
            e.printStackTrace();
        }
    }

    public static Properties getErrorProperties() {
        return ERROR_PROPERTIES;
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        String errorMessage = ERROR_PROPERTIES.getProperty(errorCode.name() + "_message");
        throwIf(condition, new BusinessException(errorCode, errorMessage));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
