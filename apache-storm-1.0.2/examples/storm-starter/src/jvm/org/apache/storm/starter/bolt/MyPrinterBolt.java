package org.apache.storm.starter.bolt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import twitter4j.Status;

public class MyPrinterBolt extends BaseBasicBolt {

	String fileName = "Tweet_List.txt";
	int count = 1;

	public MyPrinterBolt(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		File file = new File(fileName);
		Status t = (Status) tuple.getValueByField("tweet");

		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file, true); 
			writer.write(t.getText() + "\n"); 
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(count++ + " " + t.getText());

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
