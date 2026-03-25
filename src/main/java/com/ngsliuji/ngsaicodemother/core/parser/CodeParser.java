package com.ngsliuji.ngsaicodemother.core.parser;

public interface CodeParser<T> {

    /**
     * 解析源码内容
     *
     * @param codeContent 原始代码内容
     * @return 解析后的结果
     */
    T parseCode(String codeContent);
}
