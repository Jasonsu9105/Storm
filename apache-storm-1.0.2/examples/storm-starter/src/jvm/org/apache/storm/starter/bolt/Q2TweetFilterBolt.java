package org.apache.storm.starter.bolt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.storm.starter.util.Logger;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.google.common.base.Stopwatch;

import twitter4j.HashtagEntity;
import twitter4j.Status;

public class Q2TweetFilterBolt extends BaseBasicBolt{

	int count = 1;
	long runCounter;
	final long logIntervalInSeconds;
	Stopwatch stopwatch = null;
	Date date = new Date();
	List<Status> tweetList;
	HashSet<String> HashTagSet;
	int thres = 100;

	public Q2TweetFilterBolt(final long logIntervalInSeconds) {
		this.logIntervalInSeconds = logIntervalInSeconds;

	}

	@Override
	public final void prepare(Map stormConf, TopologyContext context) {
		tweetList = new ArrayList<Status>();
		this.stopwatch = Stopwatch.createStarted();
	}


	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {


		if(tuple.getSourceComponent().equals("hashtags")) {
			HashTagSet = (HashSet<String>) tuple.getValueByField("hashtags");
		}

		if(tuple.getSourceComponent().equals("friendCount")) {
			thres = tuple.getIntegerByField("friendCount");
		}

		if(tuple.getSourceComponent().equals("twitter")) {
			Status curTweeet = (Status) tuple.getValueByField("tweet");

			//System.out.println(count++ + " " + curTweeet.getText());

			tweetList.add(curTweeet);

		}



		if (logIntervalInSeconds <= stopwatch.elapsed(TimeUnit.SECONDS)) {
			generateFile(collector);
			this.stopwatch.reset();
			this.stopwatch.start();
			tweetList.clear();
		}


		//		for(Status tweet : tweetList) {
		//
		//			System.out.println(count++ + " " + tweet.getText());
		//
		//			//if(tweet.getUser().getFriendsCount() < thres) {
		//			for(HashtagEntity tags : tweet.getHashtagEntities()) {
		//				//if(HashTagSet.contains(tags.getText())) {
		//				filterTweets.add(tweet.getText());
		//				break;
		//				//}
		//			}
		//			//}
		//		}//end of for

	}

	private void generateFile(BasicOutputCollector collector) {
		//System.out.println("HashTag Size: " + HashTagSet.size());
		//System.out.println("FriendCount Threshold: " + thres);
		System.out.println("Time Interval: " + count++ + " Processing "
				+ "" + tweetList.size() + " Tweets");
		List<String> filterTweets = new ArrayList<String>();

		for(Status tweet : tweetList) {
			if(tweet.getUser().getFriendsCount() < thres) {
				for(HashtagEntity tags : tweet.getHashtagEntities()) {
					//System.out.println(count++ + " " + tags.getText());
					if(HashTagSet.contains(tags.getText().toLowerCase())) {
						filterTweets.add(tweet.getText());
						break;
					}
				}
			}
		}
		
		if(!filterTweets.isEmpty()) {
			collector.emit(new Values(filterTweets));
			DateFormat df = new SimpleDateFormat("MMddHHmmss");
			Date today = Calendar.getInstance().getTime();
			String reportDate = df.format(today);
			Logger.log("Q2_Tweet.txt", reportDate);
			for(String cur : filterTweets) {
				Logger.log("Q2_Tweet.txt", cur);
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer ofd) {
		ofd.declare(new Fields("filterTweets"));

	}

}
