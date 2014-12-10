import java.io.*;
import java.util.*;
import java.lang.Iterable;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
//Reducer to merge all types of graphlet count
public class myreducer extends MapReduceBase implements Reducer<IntWritable, DoubleWritable, Text, DoubleWritable> {

    public void reduce(IntWritable key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter arg3) throws IOException {

        double coef = 0.0; // coefficient
        double count = 0.0; // number of maps with same key
        while (values.hasNext()){
            coef += values.next().get(); // calculate sum of coefficients of each partition
            count += 1.0; // add count
        }

        Text output_key  = new Text("Test"); // output key of reducer
        DoubleWritable output_value = new DoubleWritable(coef/count); // output coefficient as average
        output.collect(output_key, output_value);
    }
}
