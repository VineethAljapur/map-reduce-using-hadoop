package edu.gatech.cse6242;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q1 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, IntWritable, IntWritable>{

    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
	String stringdata = value.toString();
	String data[] = stringdata.split("\t");
	
	IntWritable tgt = new IntWritable(Integer.parseInt(data[1]));
	IntWritable weight = new IntWritable(Integer.parseInt(data[2]));
    
        context.write(tgt, weight);
    }
  }
  
  public static class IntSumReducer
       extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable> {
    
    public void reduce(IntWritable key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      
      context.write(key, new IntWritable(sum));
    }
  }  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");

    /* TODO: Needs to be implemented */
    job.setJarByClass(Q1.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
