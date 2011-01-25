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
import java.io.FileReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.JDomDriver;

/**
 * The parser for the notifications
 * 
 * @author jpaoletti jeronimo.paoletti@gmail.com
 * @see http://github.com/jpaoletti/jPOS-Notification-Manager
 * @see Notification
 * @see NotificationQBean
 * 
 * */
public class NotificationParser {
	/**The parser*/
	private XStream xstream;
	
	/**Default constructor*/
	public NotificationParser() {
		super();
        xstream = new XStream(new JDomDriver());
        xstream.alias ("notification", Notification.class);        
	}

	public Notification parseNotificationFile(String filename) throws FileNotFoundException {
		return (Notification) xstream.fromXML (new FileReader (filename));
	}
}
