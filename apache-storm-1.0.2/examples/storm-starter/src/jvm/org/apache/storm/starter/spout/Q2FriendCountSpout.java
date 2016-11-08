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
public class Q2FriendCountSpout extends BaseRichSpout{

	SpoutOutputCollector _collector;
	Random _randGenerator;
	int interval;
	
	public Q2FriendCountSpout(int interval) {
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
	    int number = _randGenerator.nextInt(Integer.MAX_VALUE);
	    
	    //System.out.println("Emitting......" + number);
	    
	    _collector.emit(new Values(number));
	}
	
	@Override
	public void ack(Object id) {
	}

	@Override
	public void fail(Object id) {
	}


	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("friendCount"));
		
	}

}
