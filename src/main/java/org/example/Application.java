package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;


import java.util.List;

public class Application {
    public static void main(String[] args) {
        SparkConf sparkConf=new SparkConf().setAppName("Tp Ventes").setMaster("local[*]");
        JavaSparkContext sc= new JavaSparkContext(sparkConf);
        JavaRDD<String> rddVente = sc.textFile("ventes.txt");
        //vente par ville
        JavaPairRDD<String, Double> rddVenteVille= rddVente.mapToPair(vente ->{
            String[] tokens = vente.split(" ");
            String ville = tokens[1];
            Double prix = Double.parseDouble(tokens[3]);
            return new Tuple2<>(ville, prix);
        });
        JavaPairRDD<String, Double> rddVenteVilleCount=rddVenteVille.reduceByKey((a,b)->a+b);
        List<Tuple2<String, Double>> venteVilleCount = (List<Tuple2<String, Double>>) rddVenteVilleCount.collect();
        for (Tuple2<String, Double> iterator:venteVilleCount) {
            System.out.println(iterator._1()+" "+iterator._2);
        }
        //vente par ville par annnee
        JavaPairRDD<String, Double> rddVenteVilleAnnee= rddVente.mapToPair(vente ->{
            String[] tokens = vente.split(" ");
            String villeAnnee = tokens[0].substring(6)+" "+tokens[1];
            Double prix = Double.parseDouble(tokens[3]);
            return new Tuple2<>(villeAnnee, prix);
        });
        JavaPairRDD<String, Double> rddVenteVilleAnneeCount=rddVenteVilleAnnee.reduceByKey((a,b)->a+b);
        List<Tuple2<String, Double>> venteVilleAnneeCount = (List<Tuple2<String, Double>>) rddVenteVilleAnneeCount.collect();
        for (Tuple2<String, Double> iterator:venteVilleAnneeCount) {
            System.out.println(iterator._1()+" "+iterator._2);
        }
    }
}