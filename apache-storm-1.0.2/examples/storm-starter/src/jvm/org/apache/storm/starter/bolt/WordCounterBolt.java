package org.apache.storm.starter.bolt;

import java.util.*;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class WordCounterBolt extends BaseBasicBolt{


	Map<String, Long> counts;

	public WordCounterBolt(){
		counts = new HashMap<String, Long>();
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		
		//System.out.println("Counting words");
		
		String word = tuple.getString(0);
		Long count = counts.get(word);
		count = count == null ? 1L : count + 1;
		counts.put(word, count);
		collector.emit(new Values(word, count));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word", "count"));

	}

}
