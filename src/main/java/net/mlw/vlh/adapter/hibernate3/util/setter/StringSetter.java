/**
 * Copyright (c) 2003 held jointly by the individual authors.            
 *                                                                          
 * This library is free software; you can redistribute it and/or modify it    
 * under the terms of the GNU Lesser General Public License as published      
 * by the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.                                            
 *                                                                            
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; with out even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.                                                  
 *                                                                           
 * You should have received a copy of the GNU Lesser General Public License   
 * along with this library;  if not, write to the Free Software Foundation,   
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.              
 *                                                                            
 * > http://www.gnu.org/copyleft/lesser.html                                  
 * > http://www.opensource.org/licenses/lgpl-license.php
 */
package net.mlw.vlh.adapter.hibernate3.util.setter;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/** Sets a <code>java.lang.String</code> on a <code>Query</code>.
 *
 * @author Matthew L. Wilson, Andrej Zachar, Jakub Holly
 * @version $Revision: 1.3 $ $Date: 2005/12/19 12:22:26 $
 */
public class StringSetter extends AbstractSetter
{

   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(StringSetter.class);

   /**
    * @see net.mlw.vlh.adapter.hibernate3.util.Setter#set(Query, String, Object)
    */
   public void set(Query query, String key, Object value) throws HibernateException, ParseException
   {
      if (LOGGER.isInfoEnabled())
      {
         LOGGER.info("The key='" + key + "' was set to the query as the String='" + value + "'.");
      }
      try
      {
         query.setString(key, (String) value);
      }
      catch (ClassCastException e)
      {
         throw new IllegalArgumentException("Failed setting a bind variable to a statement because "
               + "it's of another class than the setter is made for - the variable is " + value.getClass() + " while the setter is "
               + getClass().getName() + ". Set the correct setter in value list config file - see for example "
               + "net.mlw.vlh.adapter.jdbc.util.setter.AbstractSetter.");
      }
   }
}