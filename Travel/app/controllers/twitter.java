package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class twitter {
	public static List<String> tweet() throws TwitterException {

		// 初期化
		 ConfigurationBuilder cb = new ConfigurationBuilder();
         cb.setDebugEnabled(true)
           .setOAuthConsumerKey("nDwDIHV6pZaaVe1RQc2VzTcu5")
           .setOAuthConsumerSecret("Burega1SBU1jhH1MjBAmmOjIAfGk0cwhxVxWcW35z3a9jFnUrc")
           .setOAuthAccessToken("930973003080282114-GoX8iwwAC9CL2JVXyxxMIUAXil65RXu")
           .setOAuthAccessTokenSecret("4lOYR6MtdvuASqjWoANXAXIcPNpUWv6gtZJFC5r9mhInp");
         TwitterFactory tf = new TwitterFactory(cb.build());
         Twitter twitter = tf.getInstance();
//		Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query();
		List<String> tweetList = new ArrayList<>();

		// 検索ワードをセット（試しにバルスを検索）
		query.setQuery("#東京");
		// 1度のリクエストで取得するTweetの数（100が最大）
		query.setCount(20);
		query.setSince("2017-11-10");
		query.setUntil("2017-11-16");

		// 検索実行
		QueryResult result = twitter.search(query);

		// 検索結果を見てみる
		for (Status tweet : result.getTweets()) {

			String u = "name:"+tweet.getUser().getName();
			tweetList.add(u);

			// 本文
			String str = tweet.getText();
			// ハッシュタグとURLの削除
			StringTokenizer sta = new StringTokenizer(str, " ");
			//トークンの出力
			while(sta.hasMoreTokens()) {
				String wk = sta.nextToken();
				if(wk.indexOf("#") == -1 && wk.indexOf("http") == -1 && wk.indexOf("RT") == -1 && wk.indexOf("@") == -1){
					tweetList.add(wk);
				}
			}
		}
		return tweetList;
	}
}
