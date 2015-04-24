/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.quickstarts.es;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * Starts up an embedded elasticsearch cluster.  This is useful when running
 * apiman in ES storage mode.  This takes the place of a standalone 
 * elasticsearch cluster installation.
 *
 * @author eric.wittmann@redhat.com
 */
@SuppressWarnings("nls")
public class Bootstrapper implements ServletContextListener {

    private static Node node = null;

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("------------------------------");
        System.out.println("Starting apiman-elasticsearch.");
        System.out.println("------------------------------");
        File esHome = new File(System.getProperty("jboss.server.data.dir"), "es");
        System.out.println("ES Home: " + esHome);
        if (!esHome.exists()) {
            esHome.mkdirs();
        }
        Builder settings = NodeBuilder.nodeBuilder().settings();
        settings.put("path.home", esHome.getAbsolutePath());
        String clusterName = "apiman";
        node = NodeBuilder.nodeBuilder().client(false).clusterName(clusterName).data(true).local(false).settings(settings).build();
        node.start();
        System.out.println("-----------------------------");
        System.out.println("apiman-elasticsearch started!");
        System.out.println("-----------------------------");
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        node.stop();
        System.out.println("-----------------------------");
        System.out.println("apiman-elasticsearch stopped!");
        System.out.println("-----------------------------");
    }

}
