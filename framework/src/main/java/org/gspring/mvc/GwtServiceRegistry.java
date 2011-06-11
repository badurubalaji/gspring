package org.gspring.mvc;

/**
 * Special interface for aware about beans registered by relative URL objects
 * 
 * @author a.buzmakoff
 * 
 */
interface GwtServiceRegistry {
	Object retrieveDelegate(String url);
}
