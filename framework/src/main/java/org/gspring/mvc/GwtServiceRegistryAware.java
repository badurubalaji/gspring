package org.gspring.mvc;

/**
 * Special interface is intended to be inherited to have access to beans registry
 * 
 * @author a.buzmakoff
 * 
 */
interface GwtServiceRegistryAware {
	public void setServiceRegistry(GwtServiceRegistry serviceRegistry);
}
