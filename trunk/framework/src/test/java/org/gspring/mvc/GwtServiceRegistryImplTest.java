package org.gspring.mvc;

import junit.framework.Assert;

import org.junit.Test;

public class GwtServiceRegistryImplTest {
	@Test
	public void testBeanCaching() {
		final String url1 = "url1";
		final Object bean1 = new Object();

		final String url2 = "url2";
		final Object bean2 = new Object();

		GwtServiceRegistryImpl gwtServiceRegistryImpl = new GwtServiceRegistryImpl();
		gwtServiceRegistryImpl.cacheBean(url1, bean1);
		gwtServiceRegistryImpl.cacheBean(url2, bean2);

		Assert.assertTrue(gwtServiceRegistryImpl.beanAlreadyExists(url1));
		Assert.assertTrue(gwtServiceRegistryImpl.beanAlreadyExists(url2));
	}

	@Test
	public void testBeanRetrieving() {
		final String url1 = "url1";
		final Object bean1 = new Object();

		final String url2 = "url2";
		final Object bean2 = new Object();

		GwtServiceRegistryImpl gwtServiceRegistryImpl = new GwtServiceRegistryImpl();
		gwtServiceRegistryImpl.cacheBean(url1, bean1);
		gwtServiceRegistryImpl.cacheBean(url2, bean2);

		Assert.assertEquals(bean1, gwtServiceRegistryImpl.retrieveDelegate(url1));
		Assert.assertEquals(bean2, gwtServiceRegistryImpl.retrieveDelegate(url2));
	}

	@Test
	public void testBeanRetrievingWithReplacing() {
		final String url1 = "url1";
		final Object bean1 = new Object();

		final String url2 = "url2";
		final Object bean2 = new Object();

		GwtServiceRegistryImpl gwtServiceRegistryImpl = new GwtServiceRegistryImpl();
		gwtServiceRegistryImpl.cacheBean(url1, bean1);
		gwtServiceRegistryImpl.cacheBean(url2, bean2);

		final String newUrl1 = url1 + "/" + "url1";
		final String newUrl2 = url2 + "/" + "url1";

		Assert.assertEquals(bean1, gwtServiceRegistryImpl.retrieveDelegate(newUrl1));
		Assert.assertEquals(bean2, gwtServiceRegistryImpl.retrieveDelegate(newUrl2));
	}
}
