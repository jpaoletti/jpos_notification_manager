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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jpos.ee.Constants;
import org.jpos.q2.QBeanSupport;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;

/**The notification service. The logger and realm properties defines where
 * the notifications will post their results.
 * 
 * The bean is defined like
 * <pre>
 * {@code
 * <notificator class="org.jpos.ee.notification.core.NotificationQBean" logger="Q2">
 *     <property name="logger"       value="notifications" />
 *     <property name="realm"        value="" />
 *     <property name="notification" value="cfg/notifications/test.xml" />
 * </notificator>
 * }
 * </pre>
 * 
 * @author jpaoletti jeronimo.paoletti@gmail.com
 * @see http://github.com/jpaoletti/jPOS-Notification-Manager
 * 
 * */
public class NotificationQBean extends QBeanSupport implements Constants {
	private Log notificationLogger;
	private List<Notification> notifications;
	private List<Thread> threads ;

	protected void initService() throws Exception {
		super.initService();
		getLog().info("Notification Manager Activated");
		NameRegistrar.register (getCustomName(), this);
		setNotificationLogger(Log.getLog(cfg.get("logger"), cfg.get("realm")));
		loadNotifications();
	}

	private void loadNotifications() {
		NotificationParser parser = new NotificationParser();
		notifications = new ArrayList<Notification>();
		threads = new ArrayList<Thread>();
        String[] ss = cfg.getAll ("notification");
        for (Integer i=0; i<ss.length; i++) {
            Notification n;
			try {
				n = parser.parseNotificationFile(ss[i]);
	            n.setService(this);
	            Thread thread = new Thread(n, n.getLocalname());
				notifications.add(n);
	            threads.add(thread);
	            getLog().info ("Initializing "+n.toString());
			} catch (FileNotFoundException e) {
				getLog().error(e);
			}
        }	
	}
	
	protected void startService() throws Exception {
		for (Thread notification : getThreads()) {
			getLog().info ("Starting "+notification);
			notification.start();
		}
	}

	protected void stopService() throws Exception {
		for (Thread notification : getThreads()) {
			getLog().info ("Stoping "+notification);
			notification.interrupt();
		}
	}

	private static String getCustomName() {
		return "notification-manager";
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotificationLogger(Log notificationLogger) {
		this.notificationLogger = notificationLogger;
	}

	public Log getNotificationLogger() {
		return notificationLogger;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}

	public List<Thread> getThreads() {
		return threads;
	}
}