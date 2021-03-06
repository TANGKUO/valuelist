/**
 * Copyright (c) 2003 held jointly by the individual authors.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA. >
 * http://www.gnu.org/copyleft/lesser.html >
 * http://www.opensource.org/licenses/lgpl-license.php
 */
package net.mlw.vlh.adapter.jdbc.objectWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.mlw.vlh.ValueListInfo;
import net.mlw.vlh.adapter.jdbc.AbstractJdbcAdapter;
import net.mlw.vlh.adapter.util.ObjectWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DefaultIdWrappedAdapter wrap original record by calling interface
 * <code>ObjectWrapper</code> method
 * <code>getWrappedRecord(Object objectToBeWrapped)</code> and with this
 * result populate the final valueList.. objectToBeWrapped could be whole
 * resultSet or specific column from resultSet.
 * 
 * @see net.mlw.vlh.adapter.util.ObjectWrapper
 * @see net.mlw.vlh.adapter.util.ObjectValidator
 * @see net.mlw.vlh.adapter.jdbc.objectWrapper.ResultSetDecorator
 * @see net.mlw.vlh.adapter.jdbc.AbstractJdbcAdapter
 * 
 * @author Andrej Zachar
 * @version $Revision: 1.9 $ $Date: 2005/10/17 11:40:25 $
 */
public class DefaultWrapperAdapter extends AbstractJdbcAdapter
{
   /**
    * Logger for this class
    */
   private static final Log LOGGER = LogFactory.getLog(DefaultWrapperAdapter.class);

   private String _columnName = null;

   private int _columnNumber = 1;

   private ObjectWrapper _wrapper;

   private boolean wrapResultSet = false;

   /**
    * @param result
    * @return
    * @throws SQLException
    */
   private Object getOrignalRecord(ResultSet result) throws SQLException
   {
      if (wrapResultSet)
      {
         return result;
      }
      else
      {
         if (_columnName != null && _columnName.length() > 0)
         {
            return result.getObject(_columnName);
         }
         else
         {
            return result.getObject(_columnNumber);
         }
      }
   }

   public List processResultSet(String name, ResultSet result, int numberPerPage, ValueListInfo info) throws SQLException
   {
      if (LOGGER.isDebugEnabled())
      {
         LOGGER.debug("Start wrapping using column '"
               + (_columnName != null && _columnName.length() > 0 ? _columnName : (_columnNumber + "")) + "'.");
      }

      List list = new ArrayList();
      if (_wrapper == null)
      {
         LOGGER.error("Required _wrapper is null!");
      }
      else
      {
         _wrapper.setValueListInfo(info);
         for (int i = 0; result.next() && i < numberPerPage; i++)
         {
            list.add(_wrapper.getWrappedRecord(getOrignalRecord(result)));
         }
      }

      LOGGER.debug("End wrapping.");
      return list;
   }

   /**
    * @return Returns the columnName.
    */
   public String getColumnName()
   {
      return _columnName;
   }

   /**
    * Specify which column will be objectToBeWrapped. Default value is null. If
    * is <b>null </b>, is used column <b>Number </b> instead.
    * <h4>Example</h4>
    * <ul>
    * result.getObject(columnName);
    * </ul>
    * 
    * @param columnName The columnName to set.
    */
   public void setColumnName(String columnName)
   {
      _columnName = columnName;
   }

   /**
    * @return Returns the columnNumber.
    */
   public int getColumnNumber()
   {
      return _columnNumber;
   }

   /**
    * Specify which column will be objectToBeWrapped. Default is 1;
    * <h4>Example</h4>
    * <ul>
    * result.getObject(columnNumber);
    * </ul>
    * 
    * @param columnNumber The columnNumber to set.
    */
   public void setColumnNumber(int columnNumber)
   {
      _columnNumber = columnNumber;
   }

   /**
    * @return Returns the objectWrapper.
    */
   public ObjectWrapper getWrapper()
   {
      return _wrapper;
   }

   /**
    * This param is required.
    * 
    * @param objectWrapper The objectWrapper to set.
    * @see net.mlw.vlh.adapter.util.ObjectWrapper
    */
   public void setWrapper(ObjectWrapper objectWrapper)
   {
      this._wrapper = objectWrapper;
   }

   /**
    * @return Returns the wrapResultSet.
    */
   public boolean isWrapResultSet()
   {
      return wrapResultSet;
   }

   /**
    * Determine object to be wrapped - resultSet or column from result set. *
    * If true, wrapper will call getWrappedRecord with resultSet parameter as
    * objectToBeWrapped. If false, wrapper will call getWrappedRecord with
    * object from resultSet. Use false if you need to pass only ids. If you
    * need to access more than one object, use true. Default is false;
    * 
    * @param wrapResultSet
    * @see ObjectWrapper#getWrappedRecord(Object)
    */
   public void setWrapResultSet(boolean wrapResultSet)
   {
      this.wrapResultSet = wrapResultSet;
   }
}