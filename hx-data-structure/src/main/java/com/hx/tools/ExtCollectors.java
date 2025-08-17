package com.hx.tools;



import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author zhangjiagu
 */
public class ExtCollectors {

    public static final int INIT_VAL_ONE = 1;

    public static final int INIT_VAL_ZERO = 0;

    public static final int INIT_VAL_RANDOM = -1;

    public static Collector<Map.Entry<Integer, Integer>, byte[], byte[]> toBitmap(int resultSize, int initVal) {
        return new BitmapCollector(resultSize, initVal);
    }

    public static <T extends TreeNode<T>> Collector<T, Map<String, T>, TreeSet<T>> toTrees() {
        return toTrees(Collectors.<T, TreeSet<T>>toCollection(TreeSet::new));
    }

    public static <T extends TreeNode<T>, R> Collector<T, Map<String, T>, R> toTrees(Collector<T, ?, R> downstream) {
        return new TreeNodeCollector<>(downstream);
    }

    public static <T extends TreeNode<T>, R> Collector<T, Map<String, T>, R> toTrees(Predicate<T> filter, Collector<T, ?, R> downstream) {
        return new TreeNodeCollector<>(filter, downstream);
    }

    static class BitmapCollector implements Collector<Map.Entry<Integer, Integer>, byte[], byte[]> {
        /**
         * 1byte = 8bit
         */
        private static final int BYTE_BIT_SIZE = 8;

        /**
         * 数据类型类型字节数
         */
        private final int byteSize;

        /**
         * 各位初始值是0还是1
         */
        private final int initVal;

        /**
         * 构造器，传入bitmap实际占用的字节数
         *
         * @param dataByteSize byte size
         */
        public BitmapCollector(int dataByteSize) {
            this(dataByteSize, INIT_VAL_RANDOM);
        }

        public BitmapCollector(int dataByteSize, int initVal) {
            this.byteSize = dataByteSize;
            this.initVal = initVal;
        }

        /**
         * 中间byte的的容器，长度为 8(bit)*字节长度
         *
         * @return 容器创建函数
         */
        @Override
        public Supplier<byte[]> supplier() {
            if (initVal == INIT_VAL_ZERO) {
                return () -> new byte[byteSize * BYTE_BIT_SIZE];
            }
            if (initVal == INIT_VAL_ONE) {
                return () -> {
                    byte[] bytes = new byte[byteSize * BYTE_BIT_SIZE];
                    Arrays.fill(bytes, (byte) 1);
                    return bytes;
                };
            }
            return () -> {
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[byteSize * BYTE_BIT_SIZE];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) (random.nextBoolean() ? 1 : 0);
                }
                return bytes;
            };
        }

        /**
         * 将每一个元素放置到中间容器中
         *
         * @return 放置逻辑处理函数
         */
        @Override
        public BiConsumer<byte[], Map.Entry<Integer, Integer>> accumulator() {
//        return (bytes, integer) -> bytes[bytes.length - integer] = 1;
            return (bytes, pair) -> {
                int index = pair.getKey();
                int val = pair.getValue();
                bytes[bytes.length - index] = (byte) val;
            };
        }

        /**
         * 如果是并行流，会同时创建多个容器，这里处理容器合并（两个byte[]中同位置但凡有一个1，取1，否则取0。故做或运算）
         *
         * @return 容器合并
         */
        @Override
        public BinaryOperator<byte[]> combiner() {
            return (bytes, bytes2) -> {
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) (bytes[i] | bytes2[i]);
                }
                return bytes;
            };
        }

        /**
         * 将8bit*字节长度的byte转换为真正的byte数组，每8个byte合并成一个真正的byte
         *
         * @return 收尾函数
         */
        @Override
        public Function<byte[], byte[]> finisher() {
            return bytes -> {
                //res: 数据字节数
                ByteBuffer buffer = ByteBuffer.allocate(byteSize);
                //小端存储，逆序，配合c#
                for (int i = byteSize - 1; i >= 0; i--) {
                    //1字节为8bit
                    byte[] b = new byte[BYTE_BIT_SIZE];
                    //获取当前读到的8位数据到临时的8位bit数组里
                    System.arraycopy(bytes, BYTE_BIT_SIZE * i, b, 0, BYTE_BIT_SIZE);
                    //8位byte数组以每位byte为bit生成byte
                    byte temp = 0;
                    for (int j = 0; j < BYTE_BIT_SIZE; j++) {
                        temp = (byte) ((temp & 0xff) | b[BYTE_BIT_SIZE - 1 - j] << j);
                    }
                    buffer.put(temp);
                }
                return buffer.array();
            };
        }

        /**
         * 无特殊特性
         *
         * @return 特性集
         */
        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>();
        }
    }

    static class TreeNodeCollector<T extends TreeNode<T>, R> implements Collector<T, Map<String, T>, R> {

        private final Collector<T, ?, R> downstream;

        private final Predicate<T> filter;

        public TreeNodeCollector(Collector<T, ?, R> downstream) {
            this.downstream = downstream;
            this.filter = t -> true;
        }

        public TreeNodeCollector(Predicate<T> filter, Collector<T, ?, R> downstream) {
            this.downstream = downstream;
            this.filter = filter;
        }


        @Override
        public Supplier<Map<String, T>> supplier() {
            return HashMap::new;
        }

        @Override
        public BiConsumer<Map<String, T>, T> accumulator() {
            Map<String, Set<T>> parentChildrenMap = new HashMap<>();
            return (valMap, t) -> {
                //id 为空 返回
                if (StringUtils.isBlank(t.getId())) {
                    return;
                }
                //收集自身
                valMap.put(t.getId(), t);
                //找自身的子元素并设置
                Set<T> children = parentChildrenMap.computeIfAbsent(t.getId(), id -> new TreeSet<>());
                t.setChildren(children);
                children.forEach(c -> c.setParent(t));

                //父元素不为空
                if (StringUtils.isNotBlank(t.getParentId())) {
                    //将自己设到父元素的子集中
                    Set<T> pChildren = parentChildrenMap.computeIfAbsent(t.getParentId(), pId -> new TreeSet<>());
                    pChildren.add(t);

                    //如果父元素存在，父元素的子元素集合重新设置一遍
                    T parent = valMap.get(t.getParentId());
                    if (parent != null) {
                        parent.setChildren(pChildren);
                        pChildren.forEach(c -> c.setParent(parent));
                    }
                }
            };
        }

        @Override
        public BinaryOperator<Map<String, T>> combiner() {
            return (map, map2) -> {
                map.putAll(map2);
                return map;
            };
        }

        @Override
        public Function<Map<String, T>, R> finisher() {
            return map -> map.values().stream()
                    //过滤掉父元素不为空的集合（结果在子集中）
                    .filter(filter)
                    .collect(downstream);
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>();
        }
    }
}
