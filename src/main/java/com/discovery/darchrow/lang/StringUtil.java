/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.discovery.darchrow.lang;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.discovery.darchrow.bean.BeanUtil;
import com.discovery.darchrow.slf4j.Slf4jUtil;
import com.discovery.darchrow.util.CollectionsUtil;
import com.discovery.darchrow.util.Validator;

/**
 * StringUtil {@link String}工具类,可以 查询,截取,format,转成16进制码.
 * 
 * <h3>分隔(split)</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #splitToTArray(String, String, Class)}</li>
 * <li>{@link #splitToStringArray(String, String)}</li>
 * <li>{@link #splitToIntegerArray(String, String)}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>分隔(tokenize)</h3> <blockquote>
 * <ul>
 * <li>{@link #tokenizeToStringArray(String, String)}</li>
 * <li>{@link #tokenizeToStringArray(String, String, boolean, boolean)}</li>
 * </ul>
 * </blockquote>
 * 
 * 区别在于,split 使用的是 正则表达式 {@link Pattern#split(CharSequence)} 分隔(特别注意,一些特殊字符 $|()[{^?*+\\ 需要转义才能做分隔符),而 {@link StringTokenizer} 使用索引机制,在性能上
 * StringTokenizer更高<br>
 * 因此,在注重性能的场景,还是建议使用{@link StringTokenizer}
 * 
 * @author 金鑫 2010-2-9 上午09:53:37
 * @see "org.springframework.util.StringUtils#tokenizeToStringArray(String, String)"
 * @see "org.springframework.beans.factory.xml.BeanDefinitionParserDelegate#MULTI_VALUE_ATTRIBUTE_DELIMITERS"
 * @see java.util.StringTokenizer
 * @see org.apache.commons.lang3.StringUtils
 * @since 1.0.0
 */
public final class StringUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    /** Don't let anyone instantiate this class. */
    private StringUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    // [start] search

    /**
     * 查找子字符串在 字符串中出现的 次数
     * 
     * <pre>
     *  StringUtil.searchTimes("xin", "xin")
     *  return  1
     *  
     * StringUtil.searchTimes("xiiiin", "ii")
     *  return  2
     * 
     * </pre>
     *
     * @param source
     *            查找的源字符串
     * @param target
     *            目标子串
     * @return count of target string in source
     * @see org.apache.commons.lang3.StringUtils#countMatches(CharSequence, CharSequence)
     * @since 1.0.2
     * @deprecated 使用 {@link org.apache.commons.lang3.StringUtils#countMatches(CharSequence, CharSequence)}
     */
    @Deprecated
    public static int searchTimes(String source,String target){
        if (null == source) {
            throw new IllegalArgumentException("source can't be null!");
        }
        if (null == target) {
            throw new IllegalArgumentException("target can't be null!");
        }

        // times 计数器
        int count = 0;

        // while 循环 点
        int j = 0;

        // 开始搜索的索引位置
        int fromIndex = 0;

        int sourceLength = source.length();
        int targetLength = target.length();

        // 刚开始从 0的地方查找起
        while (j != sourceLength){

            // 从指定的索引开始 返回索引位置
            int i = source.indexOf(target, fromIndex);
            if (i != -1) {

                // 一旦发现 查找到,下次 循环从找到的地方开始循环
                // 查找 从 找到的地方 开始查找
                j = i + targetLength;

                fromIndex = i + targetLength;

                // 总数 ++
                count++;
            }else{
                // 如果发现找不到了 就退出循环
                break;
            }
        }
        return count;
    }

    // [end]
    /**
     * 给一串字符串前后增加两个引号<br>
     * 
     * <pre>
     * String text = &quot;jinxin.feilong&quot;;
     * LOGGER.info(StringUtil.addDoubleQuotes(text));
     * 
     * 结果:  "jinxin.feilong"
     * </pre>
     * 
     * @param text
     *            任意的字符串
     * @return "\"" + text + "\""
     */
    public static String addDoubleQuotes(String text){
        return "\"" + text + "\"";
    }

    /**
     * 单词首字母大写 比如jinxin
     * 
     * return Jinxin
     * 
     * <pre>
     * StringUtils.capitalize(null)  = null
     * StringUtils.capitalize(&quot;&quot;)    = &quot;&quot;
     * StringUtils.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
     * StringUtils.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
     * </pre>
     * 
     * @param word
     *            单词
     * @return 单词首字母大写
     * @see org.apache.commons.lang3.StringUtils#swapCase(String)
     * @see org.apache.commons.lang3.StringUtils#capitalize(String)
     */
    public static final String firstCharToUpperCase(String word){
        return StringUtils.capitalize(word);
    }

    /**
     * 单词首字母小写 比如Jinxin
     * 
     * return jinxin
     * 
     * <pre>
     * StringUtils.capitalize(null)  = null
     * StringUtils.capitalize(&quot;&quot;)    = &quot;&quot;
     * StringUtils.capitalize(&quot;Jinxin&quot;) = &quot;jinxin&quot;
     * StringUtils.capitalize(&quot;CAt&quot;) = &quot;cAt&quot;
     * </pre>
     * 
     * @param word
     *            单词
     * @return 单词首字母小写
     * @see org.apache.commons.lang3.StringUtils#uncapitalize(String)
     */
    public static final String firstCharToLowerCase(String word){
        return StringUtils.uncapitalize(word);
    }

    // [start]Contain

    /**
     * 判断一个字符串内是否包含另外的字符串.
     * 
     * @param text
     *            原始字符串 jinxin,自动转成string
     * @param beIncludedString
     *            被包含的字符串 in
     * @return 包含返回true,如果text 为null 返回false
     * @see String#indexOf(String)
     */
    public static final boolean isContain(Object text,String beIncludedString){
        if (null == text) {
            LOGGER.debug("the param \"text\" is null,default return false");
            return false;
        }
        int indexOf = text.toString().indexOf(beIncludedString);
        return indexOf != -1;
    }

    /**
     * 忽略 大小写 是否包含<br>
     * 
     * <pre>
     * StringUtil.isContainIgnoreCase(null, &quot;&quot;)  return false
     * StringUtil.isContainIgnoreCase(text, null) return false
     * StringUtil.isContainIgnoreCase(text, &quot;&quot;) return true
     * StringUtil.isContainIgnoreCase(text, &quot;feilong&quot;) return true
     * StringUtil.isContainIgnoreCase(text, &quot;feilong1&quot;)  return false
     * StringUtil.isContainIgnoreCase(text, &quot;feiLong&quot;)  return true
     * 
     * </pre>
     * 
     * @param text
     *            the text
     * @param beIncludedString
     *            the be included string
     * @return
     *         <ul>
     *         <li>如果 null==text, return false</li>
     *         <li>如果 null==beIncludedString, return false</li>
     *         <li>两个值 转成 小写 ,判断是否包含</li>
     *         </ul>
     */
    public static final boolean isContainIgnoreCase(Object text,String beIncludedString){
        if (null == text) {
            LOGGER.debug("the param \"text\" is null,default return false");
            return false;
        }
        if (null == beIncludedString) {
            LOGGER.debug("the param \"beIncludedString\" is null,default return false");
            return false;
        }
        return isContain(text.toString().toLowerCase(), beIncludedString.toLowerCase());
    }

    // [end] 

    // [start]replace

    // ********************************replace************************************************
    /**
     * 使用给定的 replacement 替换此字符串所有匹配给定的正则表达式的子字符串..
     * 
     * @param content
     *            需要被替换的字符串
     * @param regex
     *            用来匹配此字符串的正则表达式
     * @param replacement
     *            用来替换每个匹配项的字符串
     * @return 所得String,如果传过来的内容是空,则返回""
     */
    public static final String replaceAll(Object content,String regex,String replacement){
        if (null == content) {
            return "";
        }
        return content.toString().replaceAll(regex, replacement);
    }

    /**
     * 使用指定的字面值替换序列替换此字符串所有匹配字面值目标序列的子字符串.<br>
     * 该替换从字符串的开头朝末尾执行，例如，用 "b" 替换字符串 "aaa" 中的 "aa" 将生成 "ba" 而不是 "ab".
     * 
     * <pre>
     * 处理了replacement为空的情况
     * </pre>
     * 
     * .
     * 
     * @param content
     *            内容
     * @param target
     *            要被替换的 char 值序列
     * @param replacement
     *            char 值的替换序列
     * @return 所有匹配字面值目标序列的子字符串
     */
    public static final String replace(Object content,String target,Object replacement){
        if (null == content) {
            return "";
        }
        // 替换序列是null
        if (Validator.isNullOrEmpty(replacement)) {
            replacement = "";
        }
        return content.toString().replace(target, replacement.toString());
    }

    /**
     * 将内容 <code>content</code> 中的需要被替换的内容 <code>target</code> 替换成 <code>bean</code>里面的 <code>filedName</code> 属性值.
     *
     * @param content
     *            内容
     * @param target
     *            需要被替换的内容
     * @param bean
     *            bean
     * @param filedName
     *            字段名称
     * @return 替换,将内容content 中的需要被替换的内容target 替换成bean里面的filedName属性值
     */
    public static final String replace(Object content,String target,Object bean,String filedName){
        String replacement = "";
        // 替换序列是null
        if (Validator.isNotNullOrEmpty(bean)) {
            Object filedValue = BeanUtil.getProperty(bean, filedName);
            if (null != filedValue) {
                replacement = filedValue.toString();
            }
        }
        return replace(content, target, replacement);
    }

    /**
     * * The following example demonstrates this:
     * 
     * <pre>
     * Map valuesMap = HashMap();
     * valuesMap.put(&quot;animal&quot;, &quot;quick brown fox&quot;);
     * valuesMap.put(&quot;target&quot;, &quot;lazy dog&quot;);
     * String templateString = &quot;The ${animal} jumped over the ${target}.&quot;;
     * StrSubstitutor sub = new StrSubstitutor(valuesMap);
     * String resolvedString = sub.replace(templateString);
     * </pre>
     * 
     * yielding:
     * 
     * <pre>
     *      The quick brown fox jumped over the lazy dog.
     * </pre>
     *
     * @param <V>
     *            the value type
     * @param templateString
     *            the template string
     * @param valuesMap
     *            the values map
     * @return the string
     * @see org.apache.commons.lang3.text.StrSubstitutor
     * @since 1.1.1
     */
    public static final <V> String replace(String templateString,Map<String, V> valuesMap){
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        String resolvedString = strSubstitutor.replace(templateString);
        return resolvedString;
    }

    // [end]

    // [start]startsWith

    /**
     * 测试此字符串是否以指定的前缀开始..
     * 
     * @param value
     *            value
     * @param prefix
     *            前缀
     * @return 如果参数表示的字符序列是此字符串表示的字符序列的前缀，则返回 true；否则返回 false.还要注意，如果参数是空字符串，或者等于此 String 对象（用 equals(Object) 方法确定），则返回 true.
     */
    public static final boolean startsWith(Object value,String prefix){
        return ObjectUtil.toString(value).startsWith(prefix);
    }

    // [end]
    /**
     * 字符串和数字相加(一般生成流水号使用).
     * 
     * @param str
     *            字符串
     * @param i
     *            数字
     * @return 字符串和数字相加(一般生成流水号使用)
     */
    //TODO 测试+javadoc
    public static final String stringAddInt(String str,int i){
        int length = str.length();
        String pattern = "";
        for (int j = 0; j < length; ++j){
            pattern += "0";
        }
        return NumberUtil.toString(Integer.parseInt(str) + i, pattern);
    }

    // [start]substring

    // ********************************substring************************************************
    /**
     * [截取]从指定索引处(beginIndex)的字符开始，直到此字符串末尾. <br>
     * 调用text.substring(beginIndex)
     * 
     * <pre>
     * substring("jinxin.feilong",6) 
     * 
     * return .feilong
     * </pre>
     * 
     * @param text
     *            内容
     * @param beginIndex
     *            从指定索引处
     * @return
     *         <ul>
     *         <li>如果 Validator.isNull(t),return null</li>
     *         <li>else,return text.substring(beginIndex)</li>
     *         </ul>
     */
    public static final String substring(Object text,int beginIndex){
        String t = ObjectUtil.toString(text);
        if (Validator.isNullOrEmpty(t)) {
            return null;
        }
        return t.substring(beginIndex);
    }

    /**
     * [截取]从开始位置(startIndex),截取固定长度(length)字符串<br>
     * 
     * <pre>
     * StringUtil.substring("jinxin.feilong", 6, 2)
     * 
     * renturn .f
     * </pre>
     * 
     * @param textObject
     *            被截取文字
     * @param startIndex
     *            索引开始位置,0开始
     * @param length
     *            长度 {@code >=1} 1个 即本身 <br>
     *            正常情况下,即返回出来的字符串长度
     * @return
     * 
     *         <pre>
     * {@code
     * 		Validator.isNullOrEmpty(textValue),	return null
     * 		startIndex>textLength - 1,			return null
     * 		startIndex==textLength - 1,			return substringLast(textString, 1)
     * 		length<1,							return null
     * 		1 == length,							return textString.substring(startIndex, startIndex + 1)
     * 		remainLength > length,				return textString.substring(startIndex, startIndex + length)
     * 		remainLength <= length,				return textString.substring(startIndex)
     * }
     *         </pre>
     */
    public static final String substring(Object textObject,int startIndex,int length){
        String returnValue = null;
        if (Validator.isNullOrEmpty(textObject)) {
            return null;
        }
        String textString = ObjectUtil.toString(textObject);
        int textLength = textString.length();
        // 索引位置必须小于长度
        if (startIndex > textLength - 1) {
            return null;
        }else if (startIndex == textLength - 1) {// 最后一位
            return substringLast(textString, 1);
        }else if (length < 1) {// 截取长度必须>=1
            return null;
        }else if (1 == length) {// 截取1个 即本身
            // 截取本身索引的位置
            return textString.substring(startIndex, startIndex + 1);
        }else{
            // 剩余可以被截取的字符串长度
            int remainLength = textLength - startIndex;
            // 剩余字符长长度比截取的长度长
            if (remainLength > length) {
                // 结束的索引
                int endIndex = startIndex + length;
                // 此方法最后一个不包含
                returnValue = textString.substring(startIndex, endIndex);
            }else{
                // 没有需要被截取的长
                returnValue = textString.substring(startIndex);
            }
        }
        return returnValue;
    }

    /**
     * [截取]:调用{@link #substring(String, String, int)}, 默认 shift=0
     * 
     * <pre>
     * substring(&quot;jinxin.feilong&quot;,&quot;.&quot;)======&gt;&quot;.feilong&quot;
     * </pre>
     * 
     * @param text
     *            text
     * @param beginString
     *            beginString开始截取的字符串
     * @return 调用{@link #substring(String, String, int)}, 默认 shift=0
     */
    public static final String substring(String text,String beginString){
        return substring(text, beginString, 0);
    }

    /**
     * [截取]:从第一次出现字符串位置开始(包含)截取到最后,shift表示向前或者向后挪动位数,<br>
     * beginIndex= text.indexOf(beginString) + shift;<br>
     * return text.substring(beginIndex);
     * 
     * <pre>
     * substring(&quot;jinxin.feilong&quot;,&quot;.&quot;,0)======&gt;&quot;.feilong&quot;
     * substring(&quot;jinxin.feilong&quot;,&quot;.&quot;,1)======&gt;&quot;feilong&quot;
     * </pre>
     *
     * @param text
     *            text
     * @param beginString
     *            beginString
     * @param shift
     *            负数表示向前,整数表示向后,0表示依旧从自己的位置开始算起
     * @return
     *         <ul>
     *         <li>if isNullOrEmpty(text),return null</li>
     *         <li>if isNullOrEmpty(beginString),return null</li>
     *         <li>if text.indexOf(beginString)==-1,return null</li>
     *         <li>{@code  beginIndex + shift > text.length()},return null</li>
     *         <li>else,return text.substring(beginIndex + shift)</li>
     *         </ul>
     */
    public static final String substring(String text,String beginString,int shift){
        if (Validator.isNullOrEmpty(text)) {
            return null;
        }else if (Validator.isNullOrEmpty(beginString)) {
            return null;
        }
        //****************************************************
        int beginIndex = text.indexOf(beginString);
        // 查不到指定的字符串
        if (beginIndex == -1) {
            return null;
        }
        //****************************************************
        int startIndex = beginIndex + shift;
        if (startIndex < 0) {
            throw new IllegalArgumentException(
                            Slf4jUtil.formatMessage(
                                            "beginIndex + shift <0, beginIndex:{},shift:{},text:{},text.length:{}",
                                            beginIndex,
                                            shift,
                                            text,
                                            text.length()));
        }else if (startIndex > text.length()) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(
                                "beginIndex + shift > text.length(), beginIndex:{},shift:{},text:{},text.length:{}",
                                beginIndex,
                                shift,
                                text,
                                text.length());
            }

            return null;
        }
        // 索引从0 开始
        return text.substring(startIndex);
    }

    /**
     * [截取]:从开始的字符串到结束的字符串中间的字符串(包括开始的字符串startString),不包含结束的endString.
     * 
     * @param text
     *            文字
     * @param startString
     *            开始的字符串,null表示从开头开始截取
     * @param endString
     *            结束的字符串
     * @return
     * 
     *         <pre>
     * Validator.isNull(text),return null;
     * Validator.isNull(startString),return text.substring(0, text.indexOf(endString))
     * 
     *         </pre>
     */
    public static final String substring(String text,String startString,String endString){
        if (Validator.isNullOrEmpty(text)) {
            return null;
        }else if (Validator.isNullOrEmpty(startString)) {
            return text.substring(0, text.indexOf(endString));
        }
        int beginIndex = text.indexOf(startString);
        int endIndex = text.indexOf(endString);
        return text.substring(beginIndex, endIndex);
    }

    /**
     * [截取]:获取文字最后位数的字符串.
     * <p>
     * 调用了 {@link java.lang.String#substring(int)}
     * </p>
     * 
     * <pre>
     * Example 1:
     * {@code
     * StringUtil.substringLast("jinxin.feilong", 5)---->ilong
     * }
     * </pre>
     * 
     * @param text
     *            文字
     * @param lastLenth
     *            最后的位数
     * @return 截取文字最后几个字符串
     * @see java.lang.String#substring(int)
     */
    public static final String substringLast(String text,int lastLenth){
        return text.substring(text.length() - lastLenth);
    }

    /**
     * [截取]:去除最后几位.
     * 
     * <p>
     * 调用了 {@link java.lang.String#substring(int, int)}
     * </p>
     * 
     * <pre>
     * Example 1:
     * {@code
     * StringUtil.substringWithoutLast("jinxin.feilong", 5)---->jinxin.fe
     * }
     * </pre>
     * 
     * @param text
     *            文字
     * @param lastLenth
     *            最后的位数
     * @return 去除最后几位,如果text是空,则返回""
     * @see java.lang.String#substring(int, int)
     */
    public static final String substringWithoutLast(String text,int lastLenth){
        if (Validator.isNullOrEmpty(text)) {
            return null;
        }
        return text.substring(0, text.length() - lastLenth);
    }
    
    /**
     * [截取]:去除最后的字符串.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * StringUtil.substringWithoutLast(null, "222")                     = ""
     * StringUtil.substringWithoutLast("jinxin.feilong", "ng")          = "jinxin.feilo"
     * StringUtil.substringWithoutLast("jinxin.feilong     ", "     ")  = "jinxin.feilong"
     * </pre>
     * 
     * </blockquote>
     *
     * @param text
     *            the text
     * @param lastString
     *            the last string
     * @return 如果 <code>text</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>lastString</code> 是null,返回 <code>text.toString()</code><br>
     * @since 1.4.0
     */
    public static String substringWithoutLast(final CharSequence text,final String lastString){
        if (null == text){
            return StringUtils.EMPTY;
        }
        String textString = text.toString();
        return null == lastString ? textString
                        : textString.endsWith(lastString) ? substringWithoutLast(textString, lastString.length()) : textString;
    }

    // [end]

    // [start]toBytes

    // ********************************************************************************
    /**
     * 字符串转换成byte数组.
     * 
     * @param value
     *            字符串
     * @return byte数组
     */
    public static final byte[] toBytes(String value){
        return value.getBytes();
    }

    /**
     * 字符串转换成byte数组.
     * 
     * @param value
     *            字符串
     * @param charsetName
     *            受支持的 charset 名称,比如 utf-8, {@link CharsetType}
     * @return 所得 byte 数组
     * @see String#getBytes(String)
     * @see CharsetType
     */
    public static final byte[] toBytes(String value,String charsetName){
        try{
            return value.getBytes(charsetName);
        }catch (UnsupportedEncodingException e){
            LOGGER.error(e.getClass().getName(), e);
        }
        return null;
    }

    // [end]
    /**
     * 将string 类型值转成泛型,一般用于配置文件读取数据.
     * 
     * @param <T>
     *            the generic type
     * @param value
     *            值
     * @param class1
     *            the class1
     * @return
     * 
     *         <pre>
     *         if (class1 == String.class) {
     *             return (T) value;
     *         }else if (class1 == Boolean.class) {
     *             return (T) ObjectUtil.objectToBoolean(value);
     *         }else if (class1 == Integer.class) {
     *             return (T) ObjectUtil.objectToInteger(value);
     *         }
     *         </pre>
     */
    @SuppressWarnings("unchecked")
    public static final <T> T toT(String value,Class<?> class1){
        return (T) ObjectUtil.toT(value, class1);
    }
    
    /**
     * 将字符串 <code>value</code> 使用分隔符 <code>regexSpliter</code> 分隔成 字符串数组.
     * 
     * <p>
     * 建议使用 {@link #tokenizeToStringArray(String, String)} 或者 {@link StringUtils#split(String)}
     * </p>
     *
     * @param value
     *            value
     * @param regexSpliter
     *            此处不是简单的分隔符,是正则表达式,<b>.$|()[{^?*+\\</b> 有特殊的含义,因此我们使用.的时候必须进行转义,<span style="color:red">"\"转义时要写成"\\\\"</span> <br>
     *            最终调用了 {@link java.util.regex.Pattern#split(CharSequence)}
     * @return 如果 <code>value</code> 是null或者empty,返回 {@link ArrayUtils#EMPTY_STRING_ARRAY}<br>
     * @see String#split(String)
     * @see String#split(String, int)
     * @see StringUtils#split(String)
     * @see java.util.regex.Pattern#split(CharSequence)
     */
    public static String[] split(String value,String regexSpliter){
        return Validator.isNullOrEmpty(value) ? ArrayUtils.EMPTY_STRING_ARRAY : value.split(regexSpliter);
    }

    // [start]splitToT

    /**
     * 将字符串分隔成 字符串数组.
     * 
     * @param value
     *            value
     * @param regexSpliter
     *            分隔符,注意此处不是简单的分隔符是正则表达式, .$|()[{^?*+\\ 在正则表达式中有特殊的含义，因此我们使用.的时候必须进行转义,<br>
     *            要注意的是，"\"转义时要写成"\\\\"<br>
     *            最终调用了 {@link java.util.regex.Pattern#split(CharSequence)}
     * @return 如果value 是null,返回null
     * @see String#split(String)
     * @see String#split(String, int)
     * @see java.util.regex.Pattern#split(CharSequence)
     */
    public static final String[] splitToStringArray(String value,String regexSpliter){
        if (null != value) {
            String[] strings = value.split(regexSpliter);
            return strings;
        }
        return null;
    }

    /**
     * 将字符串分隔成 字符串数组.
     * 
     * @param value
     *            value
     * @param regexSpliter
     *            分隔符,注意此处不是简单的分隔符是正则表达式, .$|()[{^?*+\\ 在正则表达式中有特殊的含义，因此我们使用.的时候必须进行转义,<br>
     *            要注意的是，"\"转义时要写成"\\\\"<br>
     *            最终调用了 {@link java.util.regex.Pattern#split(CharSequence)}
     * @return 如果value 是null,返回null
     * @see String#split(String)
     * @see String#split(String, int)
     * @see ArrayUtil#toIntegers(Object[])
     * @see java.util.regex.Pattern#split(CharSequence)
     */
    public static final Integer[] splitToIntegerArray(String value,String regexSpliter){
        if (null != value) {
            String[] strings = value.split(regexSpliter);
            return ArrayUtil.toIntegers(strings);
        }
        return null;
    }

    /**
     * 转成T数组.
     *
     * @param <T>
     *            the generic type
     * @param value
     *            字符串
     * @param regexSpliter
     *            分隔符,注意此处不是简单的分隔符是正则表达式, .$|()[{^?*+\\ 在正则表达式中有特殊的含义，因此我们使用.的时候必须进行转义,<br>
     *            要注意的是，"\"转义时要写成"\\\\"<br>
     *            最终调用了 {@link java.util.regex.Pattern#split(CharSequence)}
     * @param typeClass
     *            类型,指明 T 类型<br>
     *            Temp support only:String.class and Integer.class
     * @return 泛型数组
     * @see java.util.regex.Pattern#split(CharSequence)
     * @see #splitToIntegerArray(String, String)
     * @see #splitToStringArray(String, String)
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] splitToTArray(String value,String regexSpliter,Class<?> typeClass){
        if (typeClass == String.class) {
            return (T[]) splitToStringArray(value, regexSpliter);
        }else if (typeClass == Integer.class) {
            return (T[]) splitToIntegerArray(value, regexSpliter);
        }
        throw new IllegalArgumentException("Param typeClass don't support,Temp support only:String.class and Integer.class");
    }

    // [end]

    // [start]tokenizeToStringArray

    /**
     * (此方法借鉴 {@link "org.springframework.util.StringUtils#tokenizeToStringArray"})<br>
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>
     * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those characters can be used to
     * separate tokens. A delimiter is always a single character; for multi-character delimiters, consider using
     * {@code delimitedListToStringArray}
     * 
     * @param str
     *            the String to tokenize
     * @param delimiters
     *            the delimiter characters, assembled as String<br>
     *            参数中的所有字符都是分隔标记的分隔符,比如这里可以设置成 ";, " ,spring就是使用这样的字符串来分隔数组/集合的
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * @since 1.0.7
     */
    public static String[] tokenizeToStringArray(String str,String delimiters){
        boolean trimTokens = true;
        boolean ignoreEmptyTokens = true;
        return tokenizeToStringArray(str, delimiters, trimTokens, ignoreEmptyTokens);
    }

    /**
     * (此方法借鉴 {@link "org.springframework.util.StringUtils#tokenizeToStringArray"})<br>
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>
     * The given delimiters string is supposed to consist of any number of delimiter characters. <br>
     * Each of those characters can be used to separate tokens. <br>
     * A delimiter is always a single character; <br>
     * for multi-character delimiters, consider using {@code delimitedListToStringArray}
     * 
     * @param str
     *            the String to tokenize
     * @param delimiters
     *            the delimiter characters, assembled as String<br>
     *            参数中的所有字符都是分隔标记的分隔符,比如这里可以设置成 ";, " ,spring就是使用这样的字符串来分隔数组/集合的
     * @param trimTokens
     *            是否使用 {@link String#trim()}操作token
     * @param ignoreEmptyTokens
     *            是否忽视空白的token,如果为true,那么token必须长度 {@code >} 0;如果为false会包含长度=0 空白的字符<br>
     *            omit empty tokens from the result array
     *            (only applies to tokens that are empty after trimming; StringTokenizer
     *            will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens ({@code null} if the input String
     *         was {@code null})
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see "org.springframework.util.StringUtils#delimitedListToStringArray"
     * @see "org.springframework.util.StringUtils#tokenizeToStringArray"
     * @since 1.0.7
     */
    public static String[] tokenizeToStringArray(String str,String delimiters,boolean trimTokens,boolean ignoreEmptyTokens){
        if (str == null) {
            return null;
        }

        //StringTokenizer implements Enumeration<Object>
        //其在 Enumeration接口的基础上,  定义了 hasMoreTokens nextToken两个方法
        //实现的Enumeration接口中的  hasMoreElements nextElement 调用了  hasMoreTokens nextToken
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()){
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return CollectionsUtil.toArray(tokens);
    }

    // [end]
    // [start]format

    /**
     * 格式化字符串
     * <ul>
     * <li>StringUtil.format("%03d", 1)不能写成 StringUtil.format("%03d", "1")</li>
     * </ul>
     * %index$开头，index从1开始取值，表示将第index个参数拿进来进行格式化.<br>
     * 对整数进行格式化:格式化字符串由4部分组成:%[index$][标识][最小宽度]转换方式<br>
     * 对浮点数进行格式化:%[index$][标识][最少宽度][.精度]转换方式<br>
     * 转换方式 转换符和标志的说明<br>
     * 
     * <h3>转换符</h3>
     * 
     * <pre>
     * %s	字符串类型	"mingrisoft"
     * %c	字符类型	'm'
     * %b	布尔类型	true
     * %d	整数类型（十进制）	99
     * %x	整数类型（十六进制）	FF
     * %o	整数类型（八进制）	77
     * %f	浮点类型	99.99
     * %a	十六进制浮点类型	FF.35AE
     * %e	指数类型	9.38e+5
     * %g	通用浮点类型（f和e类型中较短的）
     * %h	散列码
     * %%	百分比类型	％
     * %n	换行符
     * %tx	日期与时间类型（x代表不同的日期与时间转换符
     * </pre>
     * 
     * <h3>标志</h3>
     * 
     * <pre>
     * {@code
     * +	为正数或者负数添加符号	("%+d",15)	+15
     * -	左对齐	("%-5d",15)	|15   |  不可以与“用0填充”同时使用
     * 0	数字前面补0	("%04d", 99)	0099
     * 空格	在整数之前添加指定数量的空格	("% 4d", 99)	|  99|
     * ,	以“,”对数字分组	("%,f", 9999.99)	9,999.990000
     * (	使用括号包含负数	("%(f", -99.99)	(99.990000)
     * #	如果是浮点数则包含小数点，如果是16进制或8进制则添加0x或0
     * <	格式化前一个转换符所描述的参数	("%f和%<3.2f", 99.45)	99.450000和99.45
     * $	被格式化的参数索引	("%1$d,%2$s", 99,"abc")	99,abc
     * }
     * </pre>
     * 
     * @param format
     *            the format
     * @param args
     *            the args
     * @return A formatted string
     * @see java.util.Formatter
     * @see String#format(String, Object...)
     * @see String#format(java.util.Locale, String, Object...)
     * @since JDK 1.5
     */
    public static String format(String format,Object...args){
        return String.format(format, args);
    }

    // [end]

}
