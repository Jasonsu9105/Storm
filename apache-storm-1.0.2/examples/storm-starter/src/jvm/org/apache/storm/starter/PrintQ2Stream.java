package org.apache.storm.starter;

import java.text.*;
import java.util.*;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;
import org.apache.storm.starter.bolt.IntermediateRankingsBolt;
import org.apache.storm.starter.bolt.MyPrinterBolt;
import org.apache.storm.starter.bolt.PrintFinalTweetBolt;
import org.apache.storm.starter.bolt.PrinterBolt;
import org.apache.storm.starter.bolt.Q2TweetFilterBolt;
import org.apache.storm.starter.bolt.TotalRankingsBolt;
import org.apache.storm.starter.bolt.WordCounterBolt;
import org.apache.storm.starter.bolt.WordSplitterBolt;
import org.apache.storm.starter.spout.Q2EmitTweetSpout;
import org.apache.storm.starter.spout.Q2FriendCountSpout;
import org.apache.storm.starter.spout.Q2HashtagsSpout;

public class PrintQ2Stream {

	public static void main(String[] args) {
		String consumerKey = "J01D3Rvby7h4rycymUQRjodmZ"; 
		String consumerSecret = "5fFsD7dAB0tUKUSSW7B3O5dxqSdeJLgLYLXuw2s2UrRSpMLgWE"; 
		String accessToken = "1705045771-drvXVxJpeT1kF7nhbsb6byvOH5V8z9g3bxJ6ugW"; 
		String accessTokenSecret = "E2C5l4yHCF7kXglnKiBtbiUGkIKmrXWJ2vb70mfVxPWOq";
		//String[] arguments = args.clone();
		String[] arguments = {consumerKey, consumerSecret, accessToken, accessTokenSecret};
		String[] keyWords = {"trump", "clinton", "dota", "warrior", "lol", "cub", 
				"movie", "love", "apple", "samsung", "vote", "nba", "party", "football",
				"cat", "dog", "birthday", "cop", "shit", "travel", "college", "drug", "work",
				"selfie", "reunion", "nfl", "cold", "hot"};
		//Arrays.copyOfRange(arguments, 4, arguments.length);

		TopologyBuilder builder = new TopologyBuilder();

		int interval = 1000;


		builder.setSpout("hashtags", new Q2HashtagsSpout(interval));
		builder.setSpout("friendCount", new Q2FriendCountSpout(interval));	
		builder.setSpout("twitter", new Q2EmitTweetSpout(consumerKey, consumerSecret,
				accessToken, accessTokenSecret, keyWords));


		//DateFormat df = new SimpleDateFormat("MMddHHmmss");

		// Get the date today using Calendar object.
		//Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		//String reportDate = df.format(today);

		//		builder.setBolt("print", new MyPrinterBolt("Tweets_" + reportDate + ".txt"))
		//		.shuffleGrouping("twitter");

		int collectInterval = 30;

		builder.setBolt("filter", new Q2TweetFilterBolt(collectInterval))
		.shuffleGrouping("friendCount")
		.shuffleGrouping("hashtags")
		.shuffleGrouping("twitter");

		builder.setBolt("splitWord", new WordSplitterBolt())
		.shuffleGrouping("filter");

		builder.setBolt("wordCount", new WordCounterBolt(), 10)
		.fieldsGrouping("splitWord", new Fields("word"));

		builder.setBolt("intermediateRanking", new IntermediateRankingsBolt())
		.shuffleGrouping("wordCount");
		builder.setBolt("totalRanking",  new TotalRankingsBolt())
		.globalGrouping("intermediateRanking");
		 
		builder.setBolt("printWordCount", new PrintFinalTweetBolt())
     	.shuffleGrouping("totalRanking");
		

		Config conf = new Config();


		final LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("Assignment2", conf, builder.createTopology());


		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				cluster.killTopology("Assignment2");
				cluster.shutdown();
			}
		});

		//		Utils.sleep(60000);
		//		cluster.shutdown();

	}

}
