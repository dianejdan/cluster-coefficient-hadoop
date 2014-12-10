cluster-coefficient-hadoop
==========================

Implement cluster-coefficient calculation on Hadoop with MapReduce.
The cluster coefficient is defined as the ratio of the number of triangles to the number of triplets. This coefficient indicates how likely two friends of a person are also friends, especially useful in social network analysis.

To compile, run

    ./compile_make_jar.sh

Before run the program, copy jar files to slave nodes

    ./copy_jar.sh

Before run the program, copy data files into HDFS
    
    /home/scratch/hadoop_core/bin/hadoop dfs -put datafile /user/mygroup/intput

Run the program

    ./execute.sh

This program calculates the cluster coefficient for one large network. The edges of the network are divided into small files. Each line in the small file represents an edge. The two vertices are separated by blank or tab. The names of these small files are the input of the program. After program finishes, the output coefficient is in the output directory on HDFS.

This is mainly one MapReduce. The program first distributed the edges of a large network to different mappers. Each mapper then calculated the cluster coefficient for its sub-network and emite (key, value) pairs to sort and shuffle. The input file contains edges of only one large network. So there is only one key. The value is the cluster coefficient of the each sub-network. The reducer will calculate the average of cluster coefficients of sub-networks from each mapper. This is only one way to calculate cluster coefficient for whole network. The code can be easily extended to take the weighted average or other calculation methods. In addition, this code can be extended to have multiple reducers for multiple network calculations.
