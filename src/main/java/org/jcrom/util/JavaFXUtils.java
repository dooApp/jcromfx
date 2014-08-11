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

import javafx.application.Platform;
import javafx.beans.property.*;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.jcrom.util.ReflectionUtils.getMethod;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 05/08/2014
 * Time: 21:53
 */
public class JavaFXUtils {

    public static Object getObject(Field field, Object obj) throws IllegalAccessException {
        if (javafx.beans.property.Property.class.isAssignableFrom(field.getType())) {
            return getPropertyValue(field, obj);
        } else {
            return field.get(obj);
        }
    }

    private static Object getPropertyValue(Field field, Object obj) throws IllegalAccessException {
        Property property = (Property) field.get(obj);
        if (property != null) {
            return property.getValue();
        } else {
            try {
                Method getter = getMethod(obj.getClass(), "get" + field.getName());
                return getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                try {
                    property = getPropertyByPropertyGetter(field, obj);
                    return property.getValue();
                } catch (NoSuchMethodException e1) {
                    return null;
                } catch (InvocationTargetException e1) {
                    return null;
                }
            } catch (InvocationTargetException e) {
                return null;
            }
        }
    }

    private static void setPropertyValue(Field field, final Object obj, final Object value) throws IllegalAccessException {
        Property property = (Property) field.get(obj);
        if (property != null) {
            updateProperty(value, property);
        } else {
            try {
                final Method setter = getMethod(obj.getClass(), "set" + field.getName(), value.getClass());
                invokeSetter(obj, value, setter);
            } catch (NoSuchMethodException e) {
                try {
                    property = getPropertyByPropertyGetter(field, obj);
                    property.setValue(value);
                } catch (NoSuchMethodException e1) {
                } catch (InvocationTargetException e1) {
                }
            }
        }
    }

    private static void invokeSetter(final Object obj, final Object value, final Method setter) {
        try {
            setter.invoke(obj, value);
        } catch (RuntimeException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                setter.invoke(obj, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void updateProperty(final Object value, final Property finalProperty) {
        try {
            finalProperty.setValue(value);
        } catch (RuntimeException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    finalProperty.setValue(value);
                }
            });
        }
    }

    private static Property getPropertyByPropertyGetter(Field field, Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Property property;
        Method propertyGetter = getMethod(obj.getClass(), field.getName() + "Property");
        property = (Property) propertyGetter.invoke(obj);
        return property;
    }

    public static void setObject(Field field, Object source, Object value) throws IllegalAccessException {
        if (value == null)
            return;
        if (MapProperty.class.isAssignableFrom(field.getType())) {
            Object mapProperty = field.get(source);
            if (mapProperty != null) {
                ((MapProperty) field.get(source)).putAll((Map) value);
            } else {
                try {
                    ((MapProperty) getPropertyByPropertyGetter(field, source)).putAll((Map) value);
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                }
            }
        } else if (ListProperty.class.isAssignableFrom(field.getType())) {
            Object listProperty = field.get(source);
            if (listProperty != null) {
                ((ListProperty) field.get(source)).setAll((Collection) value);
            } else {
                try {
                    ((ListProperty) getPropertyByPropertyGetter(field, source)).setAll((Collection) value);
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                }
            }
        } else if (javafx.beans.property.Property.class.isAssignableFrom(field.getType())) {
            setPropertyValue(field, source, value);
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
