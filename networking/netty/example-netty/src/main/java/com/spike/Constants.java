package com.spike;

public class Constants {
    /**
     * 默认的填充符
     */
    public static final String DEFAULT_PADDING = " ";

    /**
     * 制表符
     */
    public static final String TAB = "\t";

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    // TODO add specific System.getProperties()
    /**
     * 换行符
     */
    public static final String NEWLINE = System.lineSeparator();
    /**
     * 文件路径分隔符
     */
    public static final String FILE_SEP = System.getProperty("file.separator");
    /**
     * 默认文件编码
     */
    public static final String DEFAULT_FILE_ENCODING = System.getProperty("file.encoding");
    /**
     * 用户Home
     */
    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * 用户当前目录
     */
    public static final String CWD = System.getProperty("user.dir");

    /**
     * left padding with `padding` to construct a `allLength` sized String value
     *
     * @param content
     * @param allLength
     * @param padding
     * @return
     */
    public static final String LP(final String content, int allLength, String padding) {
        if (content == null || content.length() >= allLength) {
            return content;
        }

        int length = allLength - content.length();
        StringBuilder sb = new StringBuilder();
        if (padding == null || padding.length() == 0) {
            padding = DEFAULT_PADDING;
        }

        for (int i = 0; i < length; i++) {
            sb.append(padding);
        }
        sb.append(content);

        return sb.toString();
    }

    /**
     * right padding with `padding` to construct a `allLength` sized String value
     *
     * @param content
     * @param allLength
     * @param padding
     * @return
     */
    public static final String RP(final String content, int allLength, String padding) {
        if (content == null || content.length() >= allLength) {
            return content;
        }

        int length = allLength - content.length();
        StringBuilder sb = new StringBuilder();
        if (padding == null || padding.length() == 0) {
            padding = DEFAULT_PADDING;
        }

        sb.append(content);
        for (int i = 0; i < length; i++) {
            sb.append(padding);
        }

        return sb.toString();
    }

    /**
     * repeat content times
     *
     * @param content
     * @param times
     * @return
     */
    public static final String REPEAT(String content, int times) {
        if (content == null || times <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(content);
        }

        return sb.toString();
    }
}
