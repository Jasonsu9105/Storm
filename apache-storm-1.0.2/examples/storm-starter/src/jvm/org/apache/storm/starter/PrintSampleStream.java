/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.starter;

import java.text.*;
import java.util.*;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import org.apache.storm.starter.bolt.MyPrinterBolt;
import org.apache.storm.starter.bolt.PrinterBolt;
import org.apache.storm.starter.spout.TwitterSampleSpout;

public class PrintSampleStream {        
	public static void main(String[] args) {
		String consumerKey = "J01D3Rvby7h4rycymUQRjodmZ"; 
		String consumerSecret = "5fFsD7dAB0tUKUSSW7B3O5dxqSdeJLgLYLXuw2s2UrRSpMLgWE"; 
		String accessToken = "1705045771-drvXVxJpeT1kF7nhbsb6byvOH5V8z9g3bxJ6ugW"; 
		String accessTokenSecret = "E2C5l4yHCF7kXglnKiBtbiUGkIKmrXWJ2vb70mfVxPWOq";
		//String[] arguments = args.clone();
		String[] arguments = {consumerKey, consumerSecret, accessToken, accessTokenSecret};
		String[] keyWords = {"trump", "clinton", "dota", "warrior", "lol", "cub", 
				"movie", "love", "apple", "samsung", "vote", "nba", "party", "football",
				"cat", "dog"};
		//Arrays.copyOfRange(arguments, 4, arguments.length);

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("twitter", new TwitterSampleSpout(consumerKey, consumerSecret,
				accessToken, accessTokenSecret, keyWords));
		//        builder.setBolt("print", new PrinterBolt())
		//                .shuffleGrouping("twitter");
		//                

		DateFormat df = new SimpleDateFormat("MMddHHmm");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);

		builder.setBolt("print", new MyPrinterBolt("Tweets_" + reportDate + ".txt"))
		.shuffleGrouping("twitter");


		Config conf = new Config();


		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("test", conf, builder.createTopology());

		Utils.sleep(10800000);
		cluster.shutdown();
	}
}
