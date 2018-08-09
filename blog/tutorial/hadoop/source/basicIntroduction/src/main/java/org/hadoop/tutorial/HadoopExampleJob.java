package org.hadoop.tutorial;

//import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.RegexMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

public class HadoopExampleJob extends Configured implements Tool 	{
	private static Logger logger = LoggerFactory.getLogger(HadoopExampleJob.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	

	public int run(String[] arg0) throws Exception {
		java.nio.file.Path path = Paths.get(".").toAbsolutePath().normalize();
		String inDir = path.toFile().getAbsolutePath() + "/input/";
		Path outDir = new Path(path.toFile().getAbsolutePath() + "/output/");

		Configuration conf = getConf();
	    conf.set(RegexMapper.PATTERN, "owl:Class");
	    
	    Job grepJob = Job.getInstance(conf);
	        
	        grepJob.setJobName("grep-search");
	        grepJob.setJarByClass(HadoopExampleJob.class);

	        FileInputFormat.setInputPaths(grepJob, inDir);

	        grepJob.setMapperClass(HadoopExampleMapper.class);

	        grepJob.setCombinerClass(LongSumReducer.class);
	        grepJob.setReducerClass(LongSumReducer.class);

	        FileOutputFormat.setOutputPath(grepJob, outDir);
	        grepJob.setOutputFormatClass(SequenceFileOutputFormat.class);
	        grepJob.setOutputKeyClass(Text.class);
	        grepJob.setOutputValueClass(LongWritable.class);

	        grepJob.waitForCompletion(true);

//	        Job sortJob = Job.getInstance(conf);
//	        sortJob.setJobName("grep-sort");
//	        sortJob.setJarByClass(HadoopExampleMain.class);
//
//	        FileInputFormat.setInputPaths(sortJob, outDir);
//	        sortJob.setInputFormatClass(SequenceFileInputFormat.class);
//
//	        sortJob.setMapperClass(InverseMapper.class);
//
//	        sortJob.setNumReduceTasks(1);                 // write a single file
//	        FileOutputFormat.setOutputPath(sortJob, outDir;
//	        sortJob.setSortComparatorClass(          // sort by decreasing freq
//	          LongWritable.DecreasingComparator.class);
//
//	        sortJob.waitForCompletion(true);
		return 0;
	}
	
	public static void main(String[] args) {
		try {		
		    int res = ToolRunner.run(new Configuration(), new HadoopExampleJob(), args);
		    System.exit(res);
			
		} catch (Throwable t) {
			logger.error(WTF_MARKER, t.getMessage(), t);
		}		
	}	
}
