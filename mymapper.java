import java.io.*;
import java.util.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class mymapper extends MapReduceBase implements Mapper<NullWritable, Text, IntWritable, DoubleWritable>	{

    private JobConf conf;

    @Override
    public void configure(JobConf conf) {
        this.conf = conf;
    }
    
    public void map(NullWritable key, Text value, OutputCollector<IntWritable, DoubleWritable> output, Reporter reporter) throws IOException {
        FSDataInputStream currentStream;
        BufferedReader currentReader;
        FileSystem fs;
        Path path = new Path(value.toString()); // get file path
        fs = path.getFileSystem(conf);			// initiate filesystem
        currentStream = fs.open(path);			// Open FSDataInputStream
        currentReader = new BufferedReader(new InputStreamReader(currentStream)); // Get bufferreader to start reading

        // The Map object stores the adjacency matrix of the graph.
        // The key of the map object is current vertex and the ArrayList is its adjacency vertices.
        Map<Integer, ArrayList<Integer> > graph = new HashMap<Integer, ArrayList<Integer> >();

        String line; // store each line in input file

        while ( (line=currentReader.readLine()) != null){ // read every line until the end of input file
            String[] tokens = line.split("\t"); // split each line into two vertices
            Integer vrtx1 = new Integer(tokens[0]); // convert string to integer object
            Integer vrtx2 = new Integer(tokens[1]); // convert string to integer object
            if (graph.get(vrtx1) == null){ // check if the map object contains the current vertex
                                           // if not contain current vertex, put its adjacent vertex into
                                           // a new ArrayList and put vertex and adjacent list into map (graph)
                ArrayList<Integer> alist1 = new ArrayList<Integer>();
                alist1.add(vrtx2);
                graph.put(vrtx1, alist1);
            }else{
                graph.get(vrtx1).add(vrtx2); // add the new vertex into the adjacent list
            }
            if (graph.get(vrtx2) == null){ // same as vertex 1. The graph is un-directed so put an edge twice in graph
                ArrayList<Integer> alist2 = new ArrayList<Integer>();
                alist2.add(vrtx1);
                graph.put(vrtx2, alist2);
            }else{
                graph.get(vrtx2).add(vrtx1);
            }
        }


        double ntriangle = 0.0; // number of triangle
        double ntriplets = 0.0; // number of triplets

        for (Map.Entry<Integer, ArrayList<Integer> > adjX : graph.entrySet()){ // loop through the map by looping vertex X
            Integer vX = adjX.getKey(); // get adjacency list of X
            for (int iy = 0; iy < adjX.getValue().size(); ++iy){ // loop through adjacency list of X
                Integer vY = adjX.getValue().get(iy); // get vertex Y in adjacency list X
                ArrayList<Integer> listY = graph.get(vY); // get adjacency list of Y
                for (int iz = 0; iz < listY.size(); ++iz){ // loop through adjacency list of Y
                    Integer vZ = listY.get(iz); // get vertex Z in adjacency list of Y
                    if (vZ.intValue() != vX.intValue()){ // determine if Z is X since X is in Y's adjacency list
                        ArrayList<Integer> listZ = graph.get(vZ); // if not, check if X is in Z's adjacency list
                                                                  // if yes, it is a triangle
                                                                  // if not, it is a triplet
                        if (listZ.contains(vX)){ntriangle = ntriangle+1.0;}
                        else {ntriplets = ntriplets+1.0;}
                    }
                }
            }
        }


        IntWritable key1 = new IntWritable(1); // output key in map function. it is a dummy for this homework.
        DoubleWritable value1 = new DoubleWritable(ntriangle/2.0/(ntriangle/2.0+ntriplets/2.0)); // calculate coefficient
        // in my algorithm, each triangle is counted for 6 times, starting from X, X-Y-Z and X-Z-Y, and it can start 
        // with three vertices, so it is counted 6 times. So the triangle number should be ntriangle/6. Since it is multiplied
        // by 3, the term in coefficient calculation should be ntriangle/2. Similarly, ntriples should be divided by 2 since
        // each triplet is counted twice.
        output.collect(key1, value1);
    }

}
