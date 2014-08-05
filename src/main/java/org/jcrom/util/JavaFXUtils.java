/**
 * This file is part of the JCROM project.
 * Copyright (C) 2008-2014 - All rights reserved.
 * Authors: Olafur Gauti Gudmundsson, Nicolas Dos Santos
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
package org.jcrom.util;

import javafx.beans.property.*;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 05/08/2014
 * Time: 21:53
 */
public class JavaFXUtils {

    public static Object getObject(Field field, Object obj) throws IllegalAccessException {
        Object value = field.get(obj);
        if (value != null) {
            if (javafx.beans.property.Property.class.isAssignableFrom(field.getType())) {
                return ((Property) value).getValue();
            } else {
                return field.get(obj);
            }
        } else {
            return null;
        }
    }

    public static void setObject(Field field, Object source, Object value) throws IllegalAccessException {
        if (MapProperty.class.isAssignableFrom(field.getType())) {
            ((MapProperty) field.get(source)).putAll((Map) value);
        } else if (ListProperty.class.isAssignableFrom(field.getType())) {
            ((ListProperty) field.get(source)).setAll((Collection) value);
        } else if (javafx.beans.property.Property.class.isAssignableFrom(field.getType())) {
            ((Property) field.get(source)).setValue(value);
        } else {
            field.set(source, value);
        }
    }

    public static boolean isMap(Field field) {
        return ReflectionUtils.implementsInterface(field.getType(), Map.class)
                || MapProperty.class.isAssignableFrom(field.getType());
    }

    public static boolean isList(Field field) {
        return isList(field.getType());
    }

    public static boolean isList(Class<?> clazz) {
        return ReflectionUtils.implementsInterface(clazz, List.class)
                || ListProperty.class.isAssignableFrom(clazz);
    }

    public static boolean isNotString(Field field) {
        return field.getType() != String.class && field.getType() != StringProperty.class;
    }


    public static Object getValue(Field field, Object obj, javax.jcr.Property p) throws IllegalAccessException, RepositoryException, IOException {
        if (ObjectProperty.class.isAssignableFrom(field.getType())) {
            return JcrUtils.getValue(ReflectionUtils.getObjectPropertyGeneric(obj, field), p.getValue());
        } else {
            return JcrUtils.getValue(field.getType(), p.getValue());
        }
    }

    public static Class<?> getType(Field field, Object source) {
        if (ObjectProperty.class.isAssignableFrom(field.getType())) {
            return ReflectionUtils.getObjectPropertyGeneric(source, field);
        } else {
            return field.getType();
        }
    }

}
