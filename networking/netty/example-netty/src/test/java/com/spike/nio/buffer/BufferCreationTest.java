package com.spike.nio.buffer;

import com.spike.nio.BufferUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.CharBuffer;

/**
 * <pre>
 * Buffer创建, 以CharBuffer为例.
 *
 * </pre>
 */
public class BufferCreationTest {

    /**
     * 按容量分配
     */
    @Test
    public void allocate() {
        // 在堆中分配了一个char数组作为备份存储器
        // 间接缓冲区使用备份数组
        CharBuffer charBuffer = CharBuffer.allocate(10);

        Assert.assertEquals(10, charBuffer.capacity());

        // 初始状态: position=0, limit=capacity, mark未定义
        Assert.assertEquals(0, charBuffer.position());
        Assert.assertEquals(charBuffer.capacity(), charBuffer.limit());
    }

    /**
     * 使用底层数组
     */
    @Test
    public void wrap() {
        // 使用自定义数组作为缓冲区的备份存储器
        char[] charArray = new char[]{'a', 'b', 'c', 'd', 'e'};
        CharBuffer charBuffer = CharBuffer.wrap(charArray);

        Assert.assertEquals(charArray.length, charBuffer.capacity());

        // 缓冲区或数组中元素的修改, 对对方可见
        charBuffer.put(0, 'A');
        Assert.assertEquals('A', charArray[0]);

        charArray[1] = 'B';
        Assert.assertEquals('B', charBuffer.get(1));
    }

    /**
     * 使用底层数组, 带offset和length参数
     */
    @Test
    public void wrapWithOffset() {
        char[] charArray = new char[]{'a', 'b', 'c', 'd', 'e'};
        // offset=1, length=2
        // 可以存取数组的整个范围, 两个参数只是设置了初始状态
        CharBuffer charBuffer = CharBuffer.wrap(charArray, 1, 2);

        System.out.println(BufferUtil.details(charBuffer));
        // position=1, capacity=5, limit=3: bc

        // 访问底层数组
        Assert.assertTrue(charBuffer.hasArray());
        System.out.println(charBuffer.array());
        // 数组偏移量一直返回0; 而使用slice()切分使用备份数组的缓冲区时可能返回非0值
        Assert.assertEquals(0, charBuffer.arrayOffset());

        // 设置position=0, limit=capacity, 清除mark标记
        charBuffer.clear();
        System.out.println(BufferUtil.details(charBuffer));
        // position=0, capacity=5, limit=5: abcde
    }

    /**
     * CharBuffer独有的创建方法
     *
     * @see CharBuffer#wrap(CharSequence)
     * @see CharBuffer#wrap(CharSequence, int, int)
     */
    @Test
    public void wrapWithCharSequence() {
        String cs = "abcde";
        CharBuffer charBuffer = CharBuffer.wrap(cs);

        Assert.assertEquals(cs.length(), charBuffer.capacity());
    }
}
