package com.openshift.wildwest.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.openshift.wildwest.models.PlatformObject;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.openshift.api.model.Build;
import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class PlatformObjectHelper {

	private OpenShiftClient client;

	public PlatformObjectHelper() {
		client = new DefaultOpenShiftClient();
	}

	public List<PlatformObject> getPlatformObjects() {

		ArrayList<PlatformObject> platformObjects = new ArrayList<>();
		platformObjects.addAll(this.getPods());
		platformObjects.addAll(this.getPVCs());
		platformObjects.addAll(this.getServices());
		platformObjects.addAll(this.getRoutes());
		platformObjects.addAll(this.getConfigMaps());

		return platformObjects;

	}

	public PlatformObject getRandomPlatformObject() {
		List<PlatformObject> theObjects = this.getPlatformObjects();


		return theObjects.get(new Random().nextInt(theObjects.size()));
	}

	private List<PlatformObject> getPods() {
		ArrayList<PlatformObject> thePods = new ArrayList<>();

		List<Pod> pods = client.pods().list().getItems();
		for (Pod currPod : pods) {
			thePods.add(new PlatformObject(currPod.getMetadata().getUid(), currPod.getMetadata().getName(), "POD"));
		}
		return thePods;
	}

	private List<PlatformObject> getBuilds() {
		ArrayList<PlatformObject> theBuilds = new ArrayList<>();

		List<Build> builds = client.builds().list().getItems();
		for (Build currBuild : builds) {
			theBuilds.add(
					new PlatformObject(currBuild.getMetadata().getUid(), currBuild.getMetadata().getName(), "BUILD"));
		}

		return theBuilds;
	}

	private List<PlatformObject> getDeploymentConfigs() {
		ArrayList<PlatformObject> theDeployments = new ArrayList<>();
		List<DeploymentConfig> deploymentConfigs = client.deploymentConfigs().list().getItems();
		for (DeploymentConfig currConfig : deploymentConfigs) {
			theDeployments.add(new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(),
					"DEPLOYMENT_CONFIG"));
		}

		return theDeployments;
	}


	private List<PlatformObject> getBuildConfigs() {
		ArrayList<PlatformObject> resourceList = new ArrayList<>();
		List<BuildConfig> theItems = client.buildConfigs().list().getItems();
		for (BuildConfig currConfig : theItems) {
			resourceList.add(new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(),
					"BUILD_CONFIG"));
		}
		return resourceList;
	}

	private List<PlatformObject> getPVCs() {
		ArrayList<PlatformObject> resourceList = new ArrayList<>();
		List<PersistentVolumeClaim> theItems = client.persistentVolumeClaims().list().getItems();
		for (PersistentVolumeClaim currConfig : theItems) {
			resourceList.add(
					new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(), "PVC"));
		}
		return resourceList;
	}

	private List<PlatformObject> getServices() {
		ArrayList<PlatformObject> resourceList = new ArrayList<>();
		List<Service> theItems = client.services().list().getItems();
		for (Service currConfig : theItems) {
			resourceList.add(new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(),
					"SERVICE"));
		}
		return resourceList;
	}

	private List<PlatformObject> getRoutes() {
		ArrayList<PlatformObject> resourceList = new ArrayList<>();
		List<Route> theItems = client.routes().list().getItems();
		for (Route currConfig : theItems) {
			resourceList.add(
					new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(), "ROUTE"));
		}
		return resourceList;
	}

	private List<PlatformObject> getConfigMaps() {
		ArrayList<PlatformObject> resourceList = new ArrayList<>();
		List<ConfigMap> theItems = client.configMaps().list().getItems();
		for (ConfigMap currConfig : theItems) {
			resourceList.add(
					new PlatformObject(currConfig.getMetadata().getUid(), currConfig.getMetadata().getName(), "CONFIG_MAP"));
		}
		return resourceList;
	}

	public void deletePlatformObject(String gameID, String objectID, String objectType, String objectName) {

		// right now only delete things we can recover from
		switch (objectType) {
			case "POD":
				client.pods().withName(objectName).delete();
				break;
			case "BUILD":
				client.builds().withName(objectName).delete();
				break;
		}

	}

}