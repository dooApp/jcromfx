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
package org.jcrom.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcrom.AbstractJcrEntity;
import org.jcrom.JcrFile;
import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrFileNode;
import org.jcrom.annotations.JcrReference;

/**
 *
 * @author Olafur Gauti Gudmundsson
 * @author Nicolas Dos Santos
 */
public class DynamicObject extends AbstractJcrEntity {

    private static final long serialVersionUID = 1L;

    @JcrChildNode(lazy = true)
    Map<String, Object> singleValueChildren;
    @JcrChildNode(lazy = true)
    Map<String, List<Object>> multiValueChildren;

    @JcrReference(lazy = true, byPath = true)
    Map<String, Object> singleReferences;
    @JcrReference(lazy = true)
    Map<String, List<Object>> multiReferences;

    @JcrFileNode
    Map<String, JcrFile> singleFiles;
    @JcrFileNode(loadType = JcrFileNode.LoadType.BYTES)
    Map<String, List<JcrFile>> multiFiles;

    public DynamicObject() {
        this.singleValueChildren = new HashMap<String, Object>();
        this.multiValueChildren = new HashMap<String, List<Object>>();
        this.singleReferences = new HashMap<String, Object>();
        this.multiReferences = new HashMap<String, List<Object>>();
        this.singleFiles = new HashMap<String, JcrFile>();
        this.multiFiles = new HashMap<String, List<JcrFile>>();
    }

    public Map<String, List<Object>> getMultiValueChildren() {
        return multiValueChildren;
    }

    public void setMultiValueChildren(Map<String, List<Object>> multiValueChildren) {
        this.multiValueChildren = multiValueChildren;
    }

    public void putMultiValueChild(String key, List<Object> values) {
        multiValueChildren.put(key, values);
    }

    public Map<String, Object> getSingleValueChildren() {
        return singleValueChildren;
    }

    public void setSingleValueChildren(Map<String, Object> singleValueChildren) {
        this.singleValueChildren = singleValueChildren;
    }

    public void putSingleValueChild(String key, Object value) {
        singleValueChildren.put(key, value);
    }

    public Map<String, List<Object>> getMultiReferences() {
        return multiReferences;
    }

    public void setMultiReferences(Map<String, List<Object>> multiReferences) {
        this.multiReferences = multiReferences;
    }

    public void putMultiReference(String key, List<Object> values) {
        multiReferences.put(key, values);
    }

    public Map<String, Object> getSingleReferences() {
        return singleReferences;
    }

    public void setSingleReferences(Map<String, Object> singleReferences) {
        this.singleReferences = singleReferences;
    }

    public void putSingleReference(String key, Object value) {
        singleReferences.put(key, value);
    }

    public Map<String, List<JcrFile>> getMultiFiles() {
        return multiFiles;
    }

    public void setMultiFiles(Map<String, List<JcrFile>> multiFiles) {
        this.multiFiles = multiFiles;
    }

    public void putMultiFile(String key, List<JcrFile> values) {
        multiFiles.put(key, values);
    }

    public Map<String, JcrFile> getSingleFiles() {
        return singleFiles;
    }

    public void setSingleFiles(Map<String, JcrFile> singleFiles) {
        this.singleFiles = singleFiles;
    }

    public void putSingleFile(String key, JcrFile value) {
        singleFiles.put(key, value);
    }

}
