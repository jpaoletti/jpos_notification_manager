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

import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.jpos.ee.DB;
import org.jpos.util.LogEvent;

/**
 * A notification that executes the script as plain SQL. It is posible to
 * define the column separator using "column-separator" property.
 * 
 * @author jpaoletti jeronimo.paoletti@gmail.com
 * @see http://github.com/jpaoletti/jPOS-Notification-Manager
 * 
 * */
public class SQLNotification extends Notification {

    @Override
    public LogEvent doNotify(LogEvent evt) {
        DB db = new DB();
        db.open();
        try {
            SQLQuery c = db.session().createSQLQuery(getScript().trim());
            List<?> l = c.list();
            for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
                Object item = iterator.next();
                StringBuilder sb = new StringBuilder();
                if (item instanceof Object[]) {
                    Object[] objects = (Object[]) item;
                    for (int i = 0; i < objects.length; i++) {
                        Object object = objects[i];
                        sb.append(object);
                        sb.append(getConfig("column-separator", "	|	"));
                    }
                } else {
                    //TODO Iterate over properties with introspection
                    sb.append(item);
                }
                evt.addMessage(sb.toString());
            }
        } finally {
            db.close();
        }
        return evt;
    }
}
