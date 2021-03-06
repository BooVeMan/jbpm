/**
 * Copyright 2010 JBoss Inc
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

package org.jbpm.integration.console.graph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.BPMN2ProcessFactory;
import org.drools.compiler.ProcessBuilderFactory;
import org.drools.definition.process.Node;
import org.drools.definition.process.NodeContainer;
import org.drools.definition.process.Process;
import org.drools.definition.process.WorkflowProcess;
import org.drools.io.ResourceFactory;
import org.drools.marshalling.impl.ProcessMarshallerFactory;
import org.drools.runtime.process.ProcessRuntimeFactory;
import org.jboss.bpm.console.client.model.ActiveNodeInfo;
import org.jboss.bpm.console.client.model.DiagramInfo;
import org.jboss.bpm.console.client.model.DiagramNodeInfo;
import org.jboss.bpm.console.server.plugin.GraphViewerPlugin;
import org.jbpm.bpmn2.BPMN2ProcessProviderImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris Verlaenen
 */
public class GraphViewerPluginImpl implements GraphViewerPlugin {
	
    private static Logger logger = LoggerFactory.getLogger(GraphViewerPluginImpl.class);
    
	private KnowledgeBase kbase;

	public List<ActiveNodeInfo> getActiveNodeInfo(String instanceId) {
		ProcessInstanceLog processInstance = ProcessInstanceDbLog.findProcessInstance(new Long(instanceId));
		if (processInstance == null) {
			throw new IllegalArgumentException("Could not find process instance " + instanceId);
		}
		Map<String, NodeInstanceLog> nodeInstances = new HashMap<String, NodeInstanceLog>();
		for (NodeInstanceLog nodeInstance: ProcessInstanceDbLog.findNodeInstances(new Long(instanceId))) {
			if (nodeInstance.getType() == NodeInstanceLog.TYPE_ENTER) {
				nodeInstances.put(nodeInstance.getNodeInstanceId(), nodeInstance);
			} else {
				nodeInstances.remove(nodeInstance.getNodeInstanceId());
			}
		}
		if (!nodeInstances.isEmpty()) {
			List<ActiveNodeInfo> result = new ArrayList<ActiveNodeInfo>();
			for (NodeInstanceLog nodeInstance: nodeInstances.values()) {
				boolean found = false;
				DiagramInfo diagramInfo = getDiagramInfo(processInstance.getProcessId());
				for (DiagramNodeInfo nodeInfo: diagramInfo.getNodeList()) {
					if (nodeInfo.getName().equals("id=" + nodeInstance.getNodeId())) {
						result.add(new ActiveNodeInfo(diagramInfo.getWidth(), diagramInfo.getHeight(), nodeInfo));
						found = true;
						break;
					}
				}
				if (!found) {
					throw new IllegalArgumentException("Could not find info for node "
						+ nodeInstance.getNodeId() + " of process " + processInstance.getProcessId());
				}
			}
			return result;
		}
		return null;
	}

	public DiagramInfo getDiagramInfo(String processId) {
		if (kbase == null) {
			try {
				KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("Guvnor default");
				kagent.applyChangeSet(ResourceFactory.newClassPathResource("ChangeSet.xml"));
				kagent.monitorResourceChangeEvents(false);
				kbase = kagent.getKnowledgeBase();
			} catch (Throwable t) {
				if (t instanceof RuntimeException
						&& "KnowledgeAgent exception while trying to deserialize".equals(t.getMessage())) {
					logger.warn("Could not connect to guvnor");
					if (t.getCause() != null) {
						logger.error(t.getCause().getMessage(), t);
					}
				}
				logger.error("Could not load processes from guvnor: " + t.getMessage(), t);
			}
			if (kbase == null) {
				kbase = KnowledgeBaseFactory.newKnowledgeBase();
			}
			String directory = System.getProperty("jbpm.console.directory");
			if (directory == null) { 
				logger.warn("jbpm.console.directory property not found");
			} else {
				File file = new File(directory);
				if (!file.exists()) {
					throw new IllegalArgumentException("Could not find " + directory);
				}
				if (!file.isDirectory()) {
					throw new IllegalArgumentException(directory + " is not a directory");
				}
				ProcessBuilderFactory.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
				ProcessMarshallerFactory.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
				ProcessRuntimeFactory.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
				BPMN2ProcessFactory.setBPMN2ProcessProvider(new BPMN2ProcessProviderImpl());
				KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
				for (File subfile: file.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return name.endsWith(".bpmn") || name.endsWith("bpmn2");
						}})) {
					kbuilder.add(ResourceFactory.newFileResource(subfile), ResourceType.BPMN2);
				}
				kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
			}
		}
		Process process = kbase.getProcess(processId);
		if (process == null) {
			return null;
		}

		DiagramInfo result = new DiagramInfo();
		// TODO: diagram width and height?
		result.setWidth(932);
		result.setHeight(541);
		List<DiagramNodeInfo> nodeList = new ArrayList<DiagramNodeInfo>();
		if (process instanceof WorkflowProcess) {
			addNodesInfo(nodeList, ((WorkflowProcess) process).getNodes(), "id=");
		}
		result.setNodeList(nodeList);
		return result;
	}
	
	private void addNodesInfo(List<DiagramNodeInfo> nodeInfos, Node[] nodes, String prefix) {
		for (Node node: nodes) {
			nodeInfos.add(new DiagramNodeInfo(
				prefix + node.getId(),
				(Integer) node.getMetaData().get("x"),
				(Integer) node.getMetaData().get("y"),
				(Integer) node.getMetaData().get("width"),
				(Integer) node.getMetaData().get("height")));
			if (node instanceof NodeContainer) {
				addNodesInfo(nodeInfos, ((NodeContainer) node).getNodes(), prefix + node.getId() + ":");
			}
		}
	}

	public byte[] getProcessImage(String processId) {
		InputStream is = GraphViewerPluginImpl.class.getResourceAsStream("/" + processId + ".png");
		if (is != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				transfer(is, os);
			} catch (IOException e) {
				throw new RuntimeException("Could not read process image: " + e.getMessage());
			}
			return os.toByteArray();
		}
		StringBuffer sb = new StringBuffer();
		Properties properties = new Properties();
		try {
			properties.load(GraphViewerPluginImpl.class.getResourceAsStream("/jbpm.console.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load jbpm.console.properties", e);
		}
		try {
			sb.append("http://");
			sb.append(properties.get("jbpm.console.server.host"));
			sb.append(":").append(new Integer(properties.getProperty("jbpm.console.server.port")));
			sb.append("/drools-guvnor/org.drools.guvnor.Guvnor/package/defaultPackage/LATEST/");
			sb.append(URLEncoder.encode(processId, "UTF-8"));
			sb.append("-image.png");
            String droolsUsername = properties.getProperty("drools.guvnor.auth.username");
            String droolsPassword = properties.getProperty("drools.guvnor.auth.password");
            if(droolsUsername!=null && !"".equals(droolsUsername)) {
                DroolsAuthenticator droolsAuthenticator = new DroolsAuthenticator(droolsUsername, droolsPassword);
                Authenticator.setDefault(droolsAuthenticator);
            }
			is = new URL(sb.toString()).openStream();
			if (is != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					transfer(is, os);
				} catch (IOException e) {
					throw new RuntimeException("Could not read process image: " + e.getMessage());
				}
				return os.toByteArray();
			}
		} catch (Throwable t) {
            if(t instanceof IOException && ((IOException)t).getMessage().startsWith("Server returned HTTP response code: 500 for URL")) {
                logger.warn(t.getMessage());
            } else {
                logger.error(t.getMessage(), t);
            }
		}
		return null;
	}
	
	private static final int BUFFER_SIZE = 512;

	public static int transfer(InputStream in, OutputStream out) throws IOException {
		int total = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = in.read(buffer);
		while (bytesRead != -1) {
			out.write(buffer, 0, bytesRead);
			total += bytesRead;
			bytesRead = in.read(buffer);
		}
		return total;
	}

	public URL getDiagramURL(String id) {
		URL result = GraphViewerPluginImpl.class.getResource("/" + id + ".png");
		if (result != null) {
			return result;
		}
		StringBuffer sb = new StringBuffer();
		Properties properties = new Properties();
		try {
			properties.load(GraphViewerPluginImpl.class.getResourceAsStream("/jbpm.console.properties"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load jbpm.console.properties", e);
		}
		try {
			sb.append("http://");
			sb.append(properties.get("jbpm.console.server.host"));
			sb.append(":").append(new Integer(properties.getProperty("jbpm.console.server.port")));
			sb.append("/drools-guvnor/org.drools.guvnor.Guvnor/package/defaultPackage/LATEST/");
			sb.append(URLEncoder.encode(id, "UTF-8"));
			sb.append("-image.png");
			String droolsUsername = properties.getProperty("drools.guvnor.auth.username");
			String droolsPassword = properties.getProperty("drools.guvnor.auth.password");
			if(droolsUsername!=null && !"".equals(droolsUsername)) {
			    DroolsAuthenticator droolsAuthenticator = new DroolsAuthenticator(droolsUsername, droolsPassword);
			    Authenticator.setDefault(droolsAuthenticator);
			}
			URL url = new URL(sb.toString());
			InputStream is = url.openStream();
			if (is != null) {
				return url;
			}
		} catch (Throwable t) {
            if(t instanceof IOException && ((IOException)t).getMessage().startsWith("Server returned HTTP response code: 500 for URL")) {
                logger.warn(t.getMessage());
            } else {
                logger.error(t.getMessage(), t);
            }
		}
		return null;
	}

	public List<ActiveNodeInfo> getNodeInfoForActivities(
			String processDefinitionId, List<String> activities) {
		// TODO Auto-generated method stub
		return new ArrayList<ActiveNodeInfo>();
	}

}

class DroolsAuthenticator extends Authenticator {

    private String username;
    private String password;
    
    public DroolsAuthenticator(String username, String password) {
        this.username = username;
        this.password = password; 
    }

    /* (non-Javadoc)
     * @see java.net.Authenticator#getPasswordAuthentication()
     */
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }

}
