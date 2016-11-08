package org.apache.storm.starter.spout;

import java.util.*;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

@SuppressWarnings("serial")
public class Q2HashtagsSpout extends BaseRichSpout{

	SpoutOutputCollector _collector;
	Random _randGenerator;
	int interval;


	String[] hashtags = {"photography", "travel", "vacation" , "OneDirection", 
			"NowPlaying", "exo", "ipad", "cloud", "android", "iphone", "apple",
			"amazon", "adventure", "hotel", "Obama", "trump", "clinton", "nfl",
			"usa", "america", "mexico", "cat", "dog", "startup", "twitter", "youtube",
			"love", "isis", "vote", "hillary", "president", "immigration", "education",
			"sick", "healthcare", "cool", "damn", "actor", "video", "lego", "food",
			"tinder", "dream", "Debates", "Pelicans", "warrior", "TV", "news",
			"pizza", "steak", "crime" , "gun"};

	public Q2HashtagsSpout(int interval) {
		this.interval = interval;
	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		_randGenerator = new Random();

	}

	@Override
	public void nextTuple() {

		Utils.sleep(interval); 

		int size = 35;

		HashSet<String> set = new HashSet<String>();

		for(int i = 0; i < size; i++) {
			set.add(hashtags[_randGenerator.nextInt(hashtags.length)].toLowerCase());
		}

		//System.out.println("Creating hashtags" + set.size());
		
		_collector.emit(new Values(set));
	}


	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hashtags"));

	}

}
