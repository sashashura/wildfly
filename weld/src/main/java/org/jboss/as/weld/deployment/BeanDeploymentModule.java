/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.weld.deployment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection of Bean Deployment archives that share similar bean visibility.
 * <p/>
 * When the module is created all BDA's are given visibility to each other. They can then be given visibility to other bda's or
 * bean modules with one operation.
 *
 * @author Stuart Douglas
 *
 */
public class BeanDeploymentModule {

    private final Set<BeanDeploymentArchiveImpl> beanDeploymentArchives;

    public BeanDeploymentModule(Set<BeanDeploymentArchiveImpl> beanDeploymentArchives) {
        this.beanDeploymentArchives = Collections
                .unmodifiableSet(new HashSet<BeanDeploymentArchiveImpl>(beanDeploymentArchives));
        for (BeanDeploymentArchiveImpl bda : beanDeploymentArchives) {
            bda.addBeanDeploymentArchives(beanDeploymentArchives);
        }
    }

    /**
     * Makes the {@link BeanDeploymentArchiveImpl} accessible to all bdas in the module
     *
     * @param archive The archive to make accessible
     */
    public synchronized void addBeanDeploymentArchive(BeanDeploymentArchiveImpl archive) {
        for (BeanDeploymentArchiveImpl bda : beanDeploymentArchives) {
            bda.addBeanDeploymentArchive(archive);
        }
    }

    /**
     * Makes all {@link BeanDeploymentArchiveImpl}s in the given module accessible to all bdas in this module
     *
     * @param module The module to make accessible
     */
    public synchronized void addBeanDeploymentModule(BeanDeploymentModule module) {
        for (BeanDeploymentArchiveImpl bda : beanDeploymentArchives) {
            bda.addBeanDeploymentArchives(module.beanDeploymentArchives);
        }
    }

    /**
     * Makes all {@link BeanDeploymentArchiveImpl}s in the given modules accessible to all bdas in this module
     *
     * @param modules The modules to make accessible
     */
    public synchronized void addBeanDeploymentModules(Collection<BeanDeploymentModule> modules) {
        for (BeanDeploymentArchiveImpl bda : beanDeploymentArchives) {
            for (BeanDeploymentModule bdm : modules) {
                bda.addBeanDeploymentArchives(bdm.beanDeploymentArchives);
            }
        }
    }

    public Set<BeanDeploymentArchiveImpl> getBeanDeploymentArchives() {
        return beanDeploymentArchives;
    }

}
