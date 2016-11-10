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
package com.discovery.darchrow.util.predicate;

import org.apache.commons.collections.Predicate;

import com.discovery.darchrow.bean.PropertyUtil;
import com.discovery.darchrow.lang.ObjectUtil;


/**
 * 调用 {@link com.baozun.nebulaplus.bean.PropertyUtil#getProperty(Object, String)} 匹配属性值.
 *
 * @author feilong
 * @version 1.2.0 2015年4月27日 下午1:52:29
 * @since 1.2.0
 */
public class ObjectPropertyEqualsPredicate implements Predicate{

    /** The property name. */
    private final String propertyName;

    /** The value. */
    private final Object value;

    /**
     * The Constructor.
     *
     * @param propertyName
     *            the property name
     * @param value
     *            the value
     */
    public ObjectPropertyEqualsPredicate(String propertyName, Object value){
        this.propertyName = propertyName;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    @Override
    public boolean evaluate(Object object){
        Object property = PropertyUtil.getProperty(object, propertyName);
        return ObjectUtil.equals(property, value, true);
    }
}