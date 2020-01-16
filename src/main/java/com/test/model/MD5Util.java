package com.test.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class MD5Util {
    private final static HashFunction HF = Hashing.md5();
    /**
     * 编码
     */
    private final static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");


    public static String md5(Object object, Charset charset) {
        return md5(JSON.toJSONString(object, SerializerFeature.SortField), charset);
    }
    /**
     * 将其转换为json后，再进行md5
     *
     * @param str     数据源可以是任何类型的对象
     * @return
     */
    public static HashCode md5HashCode(Object str) {
        Hasher hasher = HF.newHasher();
        HashCode hash = hasher.putString(String.valueOf(str), DEFAULT_CHARSET).hash();
        return hash;
    }

    /**
     * 将其转换为json后，再进行md5
     *
     * @param str     数据源可以是任何类型的对象
     * @param charset 涉及到字符串时的操作编码，默认是utf-8
     * @return
     */
    public static String md5(String str, Charset charset) {
        Hasher hasher = HF.newHasher();
        HashCode hash = hasher.putString(str, charset).hash();
        return hash.toString();
    }


    /**
     * 将其转换为json后，再进行md5
     *
     * @param str     数据源可以是任何类型的对象
     * @return
     */
    public static String md5(String str) {
        return md5(str,DEFAULT_CHARSET);
    }

    /**
     *
     * @param object
     * @return
     */
    public static String md5(Object object) {
        return md5(object, DEFAULT_CHARSET);
    }

    /**
     * hash文件
     * @param sourceFile
     * @return
     */
    public static String md5(File sourceFile) {
        HashCode hash = HF.newHasher().putObject(sourceFile, new Funnel<File>() {
            private static final long serialVersionUID = 2757585325527511209L;

            @Override
            public void funnel(File from, PrimitiveSink into) {
                try {
                    into.putBytes(Files.toByteArray(from));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).hash();
        return hash.toString();
    }
}
