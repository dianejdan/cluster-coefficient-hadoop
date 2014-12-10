javac -classpath /home/scratch/hadoop_core/hadoop-core-1.1.2.jar MyInputFormat.java mymapper.java myreducer.java Clustering_Coefficient.java

jar -cvf clus-coef.jar *.class

rm *.class
