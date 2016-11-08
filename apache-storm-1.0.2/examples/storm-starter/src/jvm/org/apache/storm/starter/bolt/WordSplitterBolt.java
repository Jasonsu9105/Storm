package org.apache.storm.starter.bolt;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class WordSplitterBolt extends BaseBasicBolt{

	Map<String, Long> counts;

	Set<String> IGNORE_LIST = new HashSet<String>(Arrays.asList(new String[] {
			"http", "https", "the", "you", "and", "for", "that", "their", "have", "this", "just", "with", "doing", "each",
			"about", "can", "was", "not", "your", "but", "are", "one", "what", "out", "when", "get", "now", "too", "under",
			"isn't", "will", "those", "you've", "from", "while", "don", "when", "very", "such", "con", "by", "would", "is", "i"
	}));

	public WordSplitterBolt() {
		counts = new HashMap<String, Long>();
	}
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		List<String> tweets = (List<String>) input.getValueByField("filterTweets");

		List<String> words = null;
		for (String curTweet : tweets){
			words = Split(curTweet);
			for (String w : words){
				if (!IGNORE_LIST.contains(w.toLowerCase())) {
					collector.emit(new Values(w));
				}
			}
		}

		//System.out.println(tweets + " Splitted Words: " + words.toString());

	}

	private List<String> Split(String curTweet) {
		ArrayList<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("\\w+");
		Matcher m = p.matcher(curTweet);

		while (m.find()) {
			result.add(m.group());
		}
		return result;
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));

	}

}
