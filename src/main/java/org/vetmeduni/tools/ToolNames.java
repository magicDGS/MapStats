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
package org.vetmeduni.tools;

/**
 * Enum with all the tools already developed
 *
 * @author Daniel Gómez-Sánchez
 */
public enum ToolNames {
	MismatchesByLength("Compute the number of mismatches by length of the read",
		"Obtain the mean and variance of the NM tag (number of mismatches) for reads regarding the read lenght. If the tag is not found, the record is ignored",
		new org.vetmeduni.tools.implemented.MismatchesByLength());

	/**
	 * The short description for the tool
	 */
	public final String shortDescription;

	/**
	 * The long description for the tool
	 */
	public final String fullDescription;

	private final Tool associatedTool;

	/**
	 * Constructor
	 *
	 * @param shortDescription the short description
	 * @param fullDescription  the full description
	 * @param associatedTool   the tool to run
	 */
	ToolNames(String shortDescription, String fullDescription, Tool associatedTool) {
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.associatedTool = associatedTool;
	}

	/**
	 * Get the tool class from enums
	 *
	 * @param tool the tool as a String
	 *
	 * @return a new instance of a tool
	 */
	public static Tool getTool(String tool) throws IllegalArgumentException {
		return ToolNames.valueOf(tool).getTool();
	}

	/**
	 * Get the associated tool for this toolname
	 *
	 * @return the associated tool
	 */
	public Tool getTool() {
		return associatedTool;
	}
}
