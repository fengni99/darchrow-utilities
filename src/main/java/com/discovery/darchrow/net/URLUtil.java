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
package com.discovery.darchrow.net;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.discovery.darchrow.util.CollectionsUtil;
import com.discovery.darchrow.util.Validator;


/**
 * The Class URLUtil.
 *
 * @author feilong
 * @version 1.2.1 2015年6月21日 上午12:54:15
 * @since 1.2.1
 */
public final class URLUtil{

    /** Don't let anyone instantiate this class. */
    private URLUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * To string array.
     *
     * @param urls
     *            the urls
     * @return the string[]
     * @since 1.2.1
     */
    public static String[] toStringArray(URL[] urls){
        if (Validator.isNullOrEmpty(urls)){
            throw new NullPointerException("urls can't be null/empty!");
        }

        String[] stringArray = new String[urls.length];

        int i = 0;
        for (URL url : urls){
            stringArray[i] = url.toString();
            i++;
        }
        return stringArray;
    }

    /**
     * To ur ls.
     *
     * @param paths
     *            the paths
     * @return the UR l[]
     */
    public static URL[] toURLs(String[] paths){
        if (Validator.isNullOrEmpty(paths)){
            throw new NullPointerException("paths can't be null/empty!");
        }

        int length = paths.length;

        URL[] urls = new URL[length];

        int i = 0;
        try{
            for (String path : paths){
                urls[i] = new File(path).toURI().toURL();
                i++;
            }
        }catch (MalformedURLException e){
            throw new URIParseException(e);
        }

        return urls;
    }

    /**
     * To ur ls.
     *
     * @param paths
     *            the paths
     * @return the UR l[]
     */
    public static URL[] toURLs(List<String> paths){
        if (Validator.isNullOrEmpty(paths)){
            throw new NullPointerException("paths can't be null/empty!");
        }
        String[] array = CollectionsUtil.toArray(paths);
        return toURLs(array);
    }
}
