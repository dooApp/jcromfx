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
package org.jcrom;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jcr.Node;
import javax.jcr.Session;

import org.jcrom.annotations.JcrChildNode;
import org.jcrom.util.NodeFilter;
import org.jcrom.util.PathUtils;

/**
 * Handles lazy loading of child node lists.
 * 
 * @author Olafur Gauti Gudmundsson
 * @author Nicolas Dos Santos
 */
class ChildNodeListLoader extends AbstractLazyLoader {

    private static final Logger logger = Logger.getLogger(ChildNodeListLoader.class.getName());

    private final Class<?> objectClass;
    private final Object parentObject;
    private final String containerPath;
    private final int depth;
    private final NodeFilter nodeFilter;
    private final JcrChildNode jcrChildNode;

    ChildNodeListLoader(Class<?> objectClass, Object parentObject, String containerPath, Session session, Mapper mapper, int depth, NodeFilter nodeFilter, JcrChildNode jcrChildNode) {
        super(session, mapper);
        this.objectClass = objectClass;
        this.parentObject = parentObject;
        this.containerPath = containerPath;
        this.depth = depth;
        this.nodeFilter = nodeFilter;
        this.jcrChildNode = jcrChildNode;
    }

    @Override
    protected Object doLoadObject(Session session, Mapper mapper) throws Exception {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Lazy loading children list for " + containerPath);
        }
        Node childrenContainer = PathUtils.getNode(containerPath, session);
        return mapper.getChildNodeMapper().getChildrenList(objectClass, childrenContainer, parentObject, mapper, depth, nodeFilter, jcrChildNode);
    }

}
