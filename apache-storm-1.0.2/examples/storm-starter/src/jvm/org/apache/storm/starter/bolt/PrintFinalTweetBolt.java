package org.apache.storm.starter.bolt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.storm.starter.util.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class PrintFinalTweetBolt extends BaseBasicBolt{

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		DateFormat df = new SimpleDateFormat("MMddHHmmss");
		Date today = Calendar.getInstance().getTime();
		String reportDate = df.format(today);
		Logger.log("Q2_CommonWords.txt", reportDate);		
		Logger.log("Q2_CommonWords.txt", tuple.toString()); 
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

}
