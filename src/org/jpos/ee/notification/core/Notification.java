/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2010 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jpos.ee.notification.core;

import java.util.Properties;

import org.jpos.util.Log;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;

/**This is a notification, that is a thread running and posting in the configured
 * logger at the bean the result of the excecution of it scripts. This is an abstract
 * class and must implement the doNotify method. 
 * 
 * Each notification is defined in an xml file that looks like this:
 * <pre>
 * {@code
 * <?xml version="1.0"?>
 * <notification class="org.jpos.ee.notification.core.XXXNotification">
 *     <localname>Some Test Notification</localname>
 *     <delay>20000</delay>
 *     <script>SOME NICE SCRIPT</script>
 *     <properties>
 *         <property name="property1" value="a_value" />
 *     </properties>
 * </notification>
 * }
 * </pre>
 * 
 * @author jpaoletti jeronimo.paoletti@gmail.com
 * @see http://github.com/jpaoletti/jPOS-Notification-Manager
 *  
 * */
public abstract class Notification implements Runnable {
	/**Notification name*/
	private String localname;
	/**Script to be executed*/
	private String script;
	/**Time interval in milliseconds */
	private Long delay;
	/**Extra properties for specific notifications*/
	private Properties properties;
	/**The service bean */
	private NotificationQBean service;
	
	public Notification() {
		super();
	}

	public abstract LogEvent doNotify(LogEvent evt);
	
	protected String getConfig(String name , String def){
		return getProperties().getProperty(name, def);
	}
	
	protected Log getLog(){
		return getService().getNotificationLogger();
	}

	public void run() {
		boolean alive = true;
		while(alive){
			try {
				LogEvent evt = getLog().createTrace();
				//TODO Add a header to the event
				register(doNotify(evt));
				Thread.sleep(getDelay());
			} catch (InterruptedException e) {
				alive = false;
			} catch (Exception e) {
				getLog().error(e);
			}
		}
	}
	
	protected void register(LogEvent evt){
		Logger.log(evt);
	}
	
	public String toString() {
		return "Notification [localname=" + localname + "]";
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public void setService(NotificationQBean service) {
		this.service = service;
	}

	public NotificationQBean getService() {
		return service;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setLocalname(String localname) {
		this.localname = localname;
	}

	public String getLocalname() {
		return localname;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public Long getDelay() {
		return delay;
	}
}