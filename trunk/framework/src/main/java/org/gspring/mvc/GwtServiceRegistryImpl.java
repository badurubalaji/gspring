package org.gspring.mvc;

import java.util.HashMap;
import java.util.Map;

class GwtServiceRegistryImpl implements GwtServiceRegistry {
	private Object lockForReplacingWithNewUrl = new Object();

	private Map<String, Object> remoteServicesMapping = new HashMap<String, Object>();

	@Override
	public Object retrieveDelegate(String url) {
		if (beanAlreadyExists(url)) {
			return remoteServicesMapping.get(url);
		} else {
			replaceWithNewUrl(url);
			return retrieveDelegate(url);
		}
	}

	boolean beanAlreadyExists(String relativeUrl) {
		return remoteServicesMapping.containsKey(relativeUrl);
	}

	void cacheBean(String url, Object bean) {
		remoteServicesMapping.put(url, bean);
	}

	private void replaceWithNewUrl(String url) {
		synchronized (lockForReplacingWithNewUrl) {
			for (String relativeUrl : remoteServicesMapping.keySet()) {
				boolean foundInRegistryAndNeedsReplacing = url.contains(relativeUrl);
				if (foundInRegistryAndNeedsReplacing) {
					Object existingBean = remoteServicesMapping.remove(relativeUrl);
					remoteServicesMapping.put(url, existingBean);
					return;
				}
			}
		}
		throw new RuntimeException("Could not find for service for url: " + url);
	}
}