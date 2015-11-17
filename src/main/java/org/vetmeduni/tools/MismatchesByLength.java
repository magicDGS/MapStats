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

import htsjdk.samtools.*;
import htsjdk.samtools.util.ProgressLogger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.vetmeduni.utils.RunningHistogram;

import java.io.File;
import java.io.IOException;

/**
 * Count the number of mismatches (NM tag) regarding the length of the read
 *
 * @author Daniel Gómez-Sánchez
 */
public class MismatchesByLength extends AbstractTool {

	@Override
	public int run(String[] args) {
		try {
			// parsing command line
			CommandLine cmd = programParser(args);
			File input = new File(cmd.getOptionValue("i"));
			File output = new File(cmd.getOptionValue("o"));
			// int nThreads = CommonOptions.numberOfThreads(logger, cmd);
			// FINISH PARSING: log the command line (not longer in the param file)
			logCmdLine(args);
			SamReader reader = SamReaderFactory.makeDefault().validationStringency(ValidationStringency.SILENT)
											   .open(input);
			RunningHistogram<Integer> histogram = mismatchesByLenght(reader);
			histogram.printToFile(output);
			reader.close();
		} catch (IOException | SAMException e) {
			// This exception comes from the files
			logger.error(e.getMessage());
			logger.debug(e);
			return 1;
		} catch (Exception e) {
			// unknow exception
			logger.debug(e);
			return 2;
		}
		return 0;
	}

	public RunningHistogram<Integer> mismatchesByLenght(SamReader reader) {
		final RunningHistogram<Integer> histogram = new RunningHistogram<>("ReadLength", "NM");
		ProgressLogger progress = new ProgressLogger(logger);
		for (SAMRecord record : reader) {
			final int readLength = record.getReadLength();
			Integer nm = record.getIntegerAttribute("NM");
			if(nm == null) {
				continue;
			}
			histogram.addValue(readLength, nm);
			progress.record(record);
		}
		logger.info("Processed ", progress.getCount(), " reads");
		return histogram;
	}

	@Override
	protected Options programOptions() {
		Option input = Option.builder("i").longOpt("input").desc("Input BAM/SAM file for compute statistics").hasArg()
							 .argName("INPUT.bam").numberOfArgs(1).required().build();
		Option output = Option.builder("o").longOpt("output").desc("Histogram output").hasArg().argName("OUTPUT")
							  .numberOfArgs(1).required().build();
		Options options = new Options();
		options.addOption(input);
		options.addOption(output);
		// add common options
		// options.addOption(CommonOptions.parallel);
		return options;
	}
}
