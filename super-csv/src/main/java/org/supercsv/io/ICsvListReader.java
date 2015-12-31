/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;

/**
 * Interface for readers that read into Lists.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Vyacheslav Pushkin
 */
public interface ICsvListReader extends ICsvReader {
	
	/**
	 * Reads a row of a CSV file and returns a List of Strings containing each column. If you are forced to use this
	 * method instead of {@link #read(CellProcessor...)} because your CSV file has a variable number of columns, then
	 * you can call the {@link #executeProcessors(CellProcessor...)} method after calling {@link #read()} to execute the
	 * cell processors manually (after determining the number of columns read in and which cell processors to use).
	 * 
	 * @return the List of columns, or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 1.0
	 */
	List<String> read() throws IOException;
	
	/**
	 * Reads a row of a CSV file and returns a List of Objects containing each column. The data can be further processed
	 * by cell processors (each element in the processors array corresponds with a CSV column). A <tt>null</tt> entry in
	 * the processors array indicates no further processing is required (the unprocessed String value will be added to
	 * the List). Prior to version 2.0.0 this method returned a List of Strings.
	 * 
	 * @param processors
	 *            an array of CellProcessors used to further process data before it is added to the List (each element
	 *            in the processors array corresponds with a CSV column - the number of processors should match the
	 *            number of columns). A <tt>null</tt> entry indicates no further processing is required (the unprocessed
	 *            String value will be added to the List).
	 * @return the List of columns, or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws NullPointerException
	 *             if processors is null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if there was a general exception while reading/processing
	 * @since 1.0
	 */
	List<Object> read(CellProcessor... processors) throws IOException;
	
	/**
	 * <p>Reads a row of a CSV file and returns a List of Objects containing each column, using the supplied Map where key
	 * is column name and value is CellProcessor instance which processes the data before adding it to the List.
	 * CellProcessor can be <tt>null</tt>, which indicates no further processing is required (the unprocessed String value will
	 * be added to the List). Column names which are not included to the Map will be ignored (not added to the List).</p>
	 *
	 * <p>This method is useful when not all columns in CSV file are needed to be added to the List.
	 * However, a header (column names) must be present in CSV file.</p>
	 *
	 * <p>Order of elements in the resulting List is based on order of map
	 * entries. Use ordered map (e.g. <tt>LinkedHashMap</tt>) if elements order is important.</p>
	 *
	 * @param columnMapping
	 *             a map where key is CSV column name and value is optional CellProcessor instance. Columns which names
	 *             are not included in the map are ignored (not added to the List).
	 *             Use ordered map (e.g. LinkedHashMap) if elements order is important.
	 * @return the List of columns, or null if EOF
	 * @throws IOException
	 *             if an I/O error occurred
	 * @throws NullPointerException
	 *             if columnMapping is null
	 * @throws IllegalArgumentException
	 * 			   if columnMapping contains column name that is not actually present in CSV header
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if current row does not contain a column specified by columnMapping, or CellProcessor execution failed
	 * @since 2.5.0
	 *
	 */
	List<Object> read(Map<String, CellProcessor> columnMapping) throws IOException;

	/**
	 * Executes the supplied cell processors on the last row of CSV that was read. This should only be used when the
	 * number of CSV columns is unknown before the row is read, and you are forced to use {@link #read()} instead of
	 * {@link #read(CellProcessor...)}.
	 * 
	 * @param processors
	 *            an array of CellProcessors used to further process the last row of CSV data that was read (each
	 *            element in the processors array corresponds with a CSV column - the number of processors should match
	 *            the number of columns). A <tt>null</tt> entry indicates no further processing is required (the
	 *            unprocessed String value will be added to the List).
	 * @return the List of processed columns
	 * @throws NullPointerException
	 *             if processors is null
	 * @throws SuperCsvConstraintViolationException
	 *             if a CellProcessor constraint failed
	 * @throws SuperCsvException
	 *             if the wrong number of processors are supplied, or CellProcessor execution failed
	 * @since 2.1.0
	 */
	List<Object> executeProcessors(CellProcessor... processors);
}
