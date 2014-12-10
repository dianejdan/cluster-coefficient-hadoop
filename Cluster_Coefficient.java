import java.io.*;
import java.util.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class Clustering_Coefficient {

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.err.println("Usage: "+" <inpath> <outpath>");
            System.exit(1);
        }

        // first stage mapper reducer to make partition
        JobConf conf1  = new JobConf(Clustering_Coefficient.class);
        conf1.setInputFormat(MyInputFormat.class);
        FileInputFormat.addInputPaths(conf1, args[0]);
        FileOutputFormat.setOutputPath(conf1, new Path(args[1]));

        conf1.setOutputKeyClass(IntWritable.class); // set output key class
        conf1.setOutputValueClass(DoubleWritable.class); // set output value class
        
        conf1.setMapperClass(mymapper.class); // set mapper
        conf1.setReducerClass(myreducer.class); // set reducer
        
        JobClient.runJob(conf1); // run jobs
    }
}
