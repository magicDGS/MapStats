/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Daniel Gómez-Sánchez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.vetmeduni.io;

import com.opencsv.CSVWriter;
import htsjdk.samtools.util.Histogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class with static methods to print histogram informations
 *
 * @author Daniel Gómez-Sánchez
 */
public class HistogramOutputs {

	/**
	 * Print the histogram in a text file
	 *
	 * @param histogram histogram to print
	 * @param output    the output file
	 */
	public static void printHistogram(Histogram<?> histogram, File output) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(output), '\t', CSVWriter.NO_ESCAPE_CHARACTER);
		// print the header
		writer.writeNext(new String[] {histogram.getBinLabel(), histogram.getValueLabel()});
		for (Histogram.Bin bin : histogram.values()) {
			writer.writeNext(new String[] {bin.getId().toString(), Double.toString(bin.getValue())});
		}
		writer.close();
	}
}
