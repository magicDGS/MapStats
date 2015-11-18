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
package org.vetmeduni.tools.defaults;

import htsjdk.samtools.util.Log;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.io.File;

/**
 * Class that contains static instances of common options and their checking
 *
 * @author Daniel Gómez-Sánchez
 */
public class CommonOptions {

	/**
	 * Default number of threads for multi-threaded input
	 */
	public static final int DEFAULT_THREADS = 1;

	/**
	 * Option for parallelization
	 */
	public static Option parallel = Option.builder("nt").longOpt("number-of-thread").desc(
		"Specified the number of threads to use. [Default=" + DEFAULT_THREADS + "]").hasArg().numberOfArgs(1)
										  .argName("INT").optionalArg(true).build();

	/**
	 * Get the default number of threads if the command line does not contain the parallel option; if it is contain,
	 * parse the command line and return the number of threads asked for
	 *
	 * @param cmd the command line where check if it the option is set
	 *
	 * @return the number of threads to use
	 */
	public static int numberOfThreads(Log logger, CommandLine cmd) {
		int nThreads = (cmd.hasOption(parallel.getOpt())) ?
			Integer.parseInt(cmd.getOptionValue(parallel.getOpt())) :
			DEFAULT_THREADS;
		if (nThreads != 1) {
			logger.warn(
				"Currently multi-threads does not control the number of threads in use, depends on the number of outputs");
		}
		return nThreads;
	}

	/**
	 * Option for the input to compute the statistics
	 */
	public static Option bamInput = Option.builder("i").longOpt("input").desc("Input BAM/SAM file for compute statistics").hasArg()
						 .argName("INPUT.bam").numberOfArgs(1).required().build();

	/**
	 * Get the input file
	 *
	 * @param cmd
	 * @return
	 */
	public static File getInputFile(CommandLine cmd) {
		return new File(cmd.getOptionValue(bamInput.getOpt()));
	}

	/**
	 * Option for the output for statistics
	 */
	public static Option statsOutput = Option.builder("o").longOpt("output").desc("Statistics output").hasArg().argName("OUTPUT")
						  .numberOfArgs(1).required().build();

	/**
	 * Get the output file
	 *
	 * @param cmd
	 * @return
	 */
	public static File getOutputFile(CommandLine cmd) {
		return new File(cmd.getOptionValue(statsOutput.getOpt()));
	}

}
