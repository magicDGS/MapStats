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
package org.vetmeduni.utils;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

import static htsjdk.tribble.util.MathUtils.RunningStat;
import static org.vetmeduni.utils.Formats.roundToSevenFmt;

/**
 * Histogram with a running statistic; based on {@link htsjdk.samtools.util.Histogram}
 *
 * @author Daniel Gómez-Sánchez
 */
public class RunningHistogram<K extends Comparable> extends TreeMap<K, RunningHistogram.Bin> {

	private String binLabel = "BIN";

	private String valueLabel = "VALUE";

	/**
	 * Constructs a new Histogram with default bin and value labels.
	 */
	public RunningHistogram() {
	}

	/**
	 * Constructs a new Histogram with supplied bin and value labels.
	 */
	public RunningHistogram(final String binLabel, final String valueLabel) {
		this.binLabel = binLabel;
		this.valueLabel = valueLabel;
	}

	/**
	 * Constructs a new Histogram that'll use the supplied comparator to sort keys.
	 */
	public RunningHistogram(final Comparator<K> comparator) {
		super(comparator);
	}

	/**
	 * Constructor that takes labels for the bin and values and a comparator to sort the bins.
	 */
	public RunningHistogram(final String binLabel, final String valueLabel, final Comparator<K> comparator) {
		this(comparator);
		this.binLabel = binLabel;
		this.valueLabel = valueLabel;
	}

	/**
	 * Copy constructor for a histogram.
	 */
	public RunningHistogram(final RunningHistogram<K> in) {
		super(in);
		this.binLabel = in.binLabel;
		this.valueLabel = in.valueLabel;
	}

	/**
	 * Prefill the histogram with the supplied set of bins.
	 */
	public void prefillBins(final K... ids) {
		for (final K id : ids) {
			put(id, new Bin(id));
		}
	}

	/**
	 * Increments the value in the designated bin by the supplied increment.
	 */
	public void addValue(final K id, final double value) {
		Bin bin = get(id);
		if (bin == null) {
			bin = new Bin(id);
			put(id, bin);
		}
		bin.addValue(value);
	}

	public String getBinLabel() {
		return binLabel;
	}

	public void setBinLabel(final String binLabel) {
		this.binLabel = binLabel;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(final String valueLabel) {
		this.valueLabel = valueLabel;
	}

	/**
	 * Checks that the labels and values in the two histograms are identical.
	 */
	public boolean equals(final Object o) {
		return o != null &&
			(o instanceof RunningHistogram) &&
			((RunningHistogram) o).binLabel.equals(this.binLabel) &&
			((RunningHistogram) o).valueLabel.equals(this.valueLabel) &&
			super.equals(o);
	}

	/**
	 * Print the current state of the histogram
	 *
	 * @param output the output file
	 */
	public void printToFile(File output) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(output), '\t', CSVWriter.NO_ESCAPE_CHARACTER);
		writer.writeNext(new String[] {binLabel, valueLabel+"_counts", valueLabel + "_mean", valueLabel + "_variance"});
		for (Bin bin : values()) {
			writer.writeNext(bin.getArrayToOutput());
		}
		writer.close();
	}

	/**
	 * Represents a bin in the Histogram.
	 */
	public class Bin implements Serializable {

		private final K id;

		private final RunningStat stat = new RunningStat();

		/**
		 * Constructs a new bin with the given ID.
		 */
		private Bin(final K id) {
			this.id = id;
		}

		/**
		 * Gets the ID of this bin.
		 */
		public K getId() {
			return id;
		}

		/**
		 * Gets the stats for the bin
		 */
		public RunningStat getStat() {
			return stat;
		}

		public void addValue(double value) {
			stat.push(value);
		}

		private String[] getArrayToOutput() {
			return new String[] {id.toString(), Long.toString(stat.numDataValues()), roundToSevenFmt.format(stat.mean()),
				roundToSevenFmt.format(stat.variance())};
		}

		/**
		 * Return the string format of the bin (ID:numDataValues)
		 */
		public String toString() {
			return String.format("%s:%s", id, stat.numDataValues());
		}

		/**
		 * Checks the equality of the bin only by ID
		 */
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			final Bin bin = (Bin) o;
			return id.equals(bin.id);
		}

		public double getIdValue() {
			if (id instanceof Number) {
				return ((Number) id).doubleValue();
			} else {
				throw new UnsupportedOperationException("getIdValue only supported for Histogram<? extends Number>");
			}
		}
	}
}
