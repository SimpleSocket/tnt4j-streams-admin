/*
 * Copyright 2014-2019 JKOOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jkoolcloud.tnt4j.streams.registry.zoo.utils;

import com.jkoolcloud.tnt4j.streams.admin.utils.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * The type Curator utils.
 */
public class CuratorUtils {

	/**
	 * Does node exist boolean.
	 *
	 * @param path
	 *            the path
	 * @param curator
	 *            the curator
	 * @return the boolean
	 */
	public static boolean doesNodeExist(String path, CuratorFramework curator) {
		Stat stat = null;
		try {
			stat = curator.checkExists().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stat == null ? false : true;
	}

	/**
	 * Create node.
	 *
	 * @param path
	 *            the path
	 * @param curator
	 *            the curator
	 */
	public static void createNode(String path, CuratorFramework curator) {
		try {
			if (!doesNodeExist(path, curator)) {
				String result = curator.create().forPath(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets data.
	 *
	 * @param path
	 *            the path
	 * @param data
	 *            the data
	 * @param curator
	 *            the curator
	 * @return the data
	 */
	public static boolean setData(String path, String data, CuratorFramework curator) {
		Stat stat = null;
		try {
			stat = curator.setData().forPath(path, data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat != null;
	}


	/**
	 * Sets data.
	 *
	 * @param path
	 *            the path
	 * @param data
	 *            the data
	 * @param curator
	 *            the curator
	 * @return the data
	 */
	public static boolean CheckIfNodeExistsAndSetData(String path, String data, CuratorFramework curator) {
		Stat stat = null;
		try {
			if (doesNodeExist(path, curator)) {
				stat = curator.setData().forPath(path, data.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat != null;
	}



	/**
	 * Delete node.
	 *
	 * @param path
	 *            the path
	 * @param curator
	 *            the curator
	 */
	public static void deleteNode(String path, CuratorFramework curator) {
		try {
			curator.delete().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get data byte [ ].
	 *
	 * @param path
	 *            the path
	 * @param curator
	 *            the curator
	 * @return the byte [ ]
	 */
	public static byte[] getData(String path, CuratorFramework curator) {
		byte[] bytes = null;
		try {
			bytes = curator.getData().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * Gets offered service path.
	 *
	 * @param service
	 *            the service
	 * @param offeredServicesPath
	 *            the offered services path
	 * @param curator
	 *            the curator
	 */
	public static void getOfferedServicePath(String service, String offeredServicesPath, CuratorFramework curator) {
		ServiceDiscovery<String> serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class).client(curator)
				.basePath(offeredServicesPath).build();
		try {
			serviceDiscovery.start();
			Collection<String> serviceNames = serviceDiscovery.queryForNames();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create head string.
	 *
	 * @param head
	 *            the head
	 * @param curator
	 *            the curator
	 * @return the string
	 */
	public static String createHead(String head, CuratorFramework curator) {
		if (head.startsWith("/")) {
			CuratorUtils.createNode(head, curator);
			return head;
		} else {
			String headPath = "/" + head;
			CuratorUtils.createNode(headPath, curator);
			return headPath;
		}
	}

	/**
	 * Create child string.
	 *
	 * @param path
	 *            the path
	 * @param name
	 *            the name
	 * @param curator
	 *            the curator
	 * @return the string
	 */
	public static String createChild(String path, String name, CuratorFramework curator) {
		if (path.endsWith("/")) {
			CuratorUtils.createNode(path + name, curator);
			return path;
		} else {
			String childPath = path + "/" + name;
			CuratorUtils.createNode(childPath, curator);
			return childPath;
		}
	}


	public static void createEphemeralNode(String path, CuratorFramework curator ){
		try {
			if (!doesNodeExist(path, curator)) {
				String result = curator.create().withMode(CreateMode.EPHEMERAL).forPath(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create default node hierarchy.
	 *
	 * @param curator
	 *            the curator
	 */
	public static void createDefaultNodeHierarchy(CuratorFramework curator) {

		if (doesNodeExist("/streams2", curator)) {
			return;
		}

		String headPath = createHead("/streams2", curator);
		String childPath = createChild(headPath, "v1", curator);
		String activeServicesPath = createChild(childPath, "activeServices", curator);
		String activeServicesEth = createChild(activeServicesPath, "eth", curator);
		// createChild(activeServicesEth, "instances", curator);
		createChild(activeServicesEth, "request", curator);
		createChild(activeServicesEth, "response", curator);
		createChild(activeServicesEth, "logs", curator);
		createChild(activeServicesEth, "errLogs", curator);
		String activeServicesEthRepair = createChild(activeServicesPath, "ethRepair", curator);
		// createChild(activeServicesEthRepair, "instances", curator);
		createChild(activeServicesEthRepair, "request", curator);
		createChild(activeServicesEthRepair, "response", curator);
		createChild(activeServicesEthRepair, "logs", curator);
		createChild(activeServicesEthRepair, "errLogs", curator);
		String offeredServices = createChild(childPath, "offeredServices", curator);

		String servicesArrayJson = null;
		try {
			servicesArrayJson = FileUtils.readFile("./config/zookeeperConfigs/offeredServices/serviceArray.json",
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] servicesList = null;
		try {
			servicesList = StaticObjectMapper.mapper.readValue(servicesArrayJson, String[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < servicesList.length; i++) {
			String offeredServicePath = createChild(offeredServices, servicesList[i], curator);

			String staticServiceData = null;
			try {
				staticServiceData = FileUtils.readFile(
						"./config/zookeeperConfigs/offeredServices/" + servicesList[i] + ".json",
						Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
			setData(offeredServicePath, staticServiceData, curator);
		}

	}

}
