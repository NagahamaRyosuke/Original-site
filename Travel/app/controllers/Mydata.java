package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import models.Distance;
import models.Travel_box;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class Mydata extends Controller {

	static Form<Distance> disForm = Form.form(Distance.class);
	static Form<User> userForm = Form.form(User.class);

	//会員情報
	public static Result mydata() {
		User user = User.finder.byId(Long.parseLong(session("userId")));
		userForm = Form.form(User.class).fill(user);
		return ok(
				views.html.mydata.render(User.byId(session("userId")), userForm)
			);
	}

	//会員情報の更新
	public static Result update(Long id) {
        Form<User> filledForm = userForm.bindFromRequest();

        // Form内にエラーがあるかどうかチェック
        if (filledForm.hasErrors()) { // errorがあるならbadRequest
        	flash("open", "open");
            return badRequest( views.html.mydata.render(User.byId(session("userId")), filledForm));
        }

        User.update(filledForm.get(), id);
        return redirect(routes.Mydata.mydata());
	}

	//user削除？
	public static Result delete(Long id) {
		User.finder.ref(id).delete();
		return redirect(routes.Mydata.mydata());
	}

	//履歴
	public static Result history() {
		Long id = Long.parseLong(session("userId"));
		List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
		return ok(
				views.html.myhistory.render(dis)
			);
	}

	//履歴の編集
    public static Result editHistory(Long id) {
    	String disId = String.valueOf(id);
    	session("historyId", disId);

    	return redirect(routes.Application.showTravel());
    }

    //履歴の削除
    public static Result deleteHistory(Long id) {
    	List<Travel_box> travelBox = Travel_box.finder.where().eq("distanceId", id).findList();
    	for(Travel_box box: travelBox) {
    		box.delete();
    	}
    	Distance.finder.ref(id).delete();
    	return redirect(routes.Mydata.history());
    }

    //履歴の編集の完了
    public static Result clear() {
    	session().remove("historyId");
    	return ok(
                views.html.index.render()
            );
    }

    //しおりの表示
    public static Result myTravel(Long id) {
    	String[] params = { "key" };
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String key = input.data().get("key");
        List<String> tweet = new ArrayList<>();

        try {
	        if (key == null || key.trim().equals("")) {
	        	tweet = tweetGet(Travel_box.finder.where().eq("distanceId", id).findList().get(0).place);
	        } else {
	        	tweet = tweetGet(key);
	        }
        } catch (TwitterException e) {

    	}

        if(tweet == null) {
        	tweet.add("情報の取得ができません。");
        }

    	return ok(
    			views.html.mytravel.render(Travel_box.finder.where().eq("distanceId", id).findList(),
    					Distance.finder.where().eq("distanceId", id).findList(),
    					tweet
    					)
    			);
    }

    //サンプルの表示
    public static Result sampleTravel() {
    	String[] params = { "key" };
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String key = input.data().get("key");
        List<String> tweet = new ArrayList<>();

        User user = User.finder.where().eq("userName","sample12345").eq("password","sample12345").findUnique();
        Long id = (long) 0;
        if(user == null){
        	id = sample();
        } else {
        	id = Distance.finder.where().eq("userId", user.userId).findList().get(0).distanceId;
        }

        try {
	        if (key == null || key.trim().equals("")) {
	        	tweet = tweetGet(Travel_box.finder.where().eq("distanceId", id).findList().get(0).place);
	        } else {
	        	tweet = tweetGet(key);
	        }

        } catch (TwitterException e) {

    	}

        if(tweet.size() == 0) {
        	tweet.add("情報の取得ができません。");
        }
        flash("sample","サンプル");

    	return ok(
    			views.html.mytravel.render(Travel_box.finder.where().eq("distanceId", id).findList(),
    					Distance.finder.where().eq("distanceId", id).findList(),
    					tweet
    					)
    			);
    }

    //サンプルの作成
    public static long sample() {
        User user = new User();
        user.userName = "sample12345";
        user.password = "sample12345";
        user.mail = "sample@mail";
        user.save();

    	Distance distance = new Distance();
    	distance.distanceTitle = "謹賀新年  ～サンプル～";
        user.distance.add(distance);
        user.save();

        User userID = User.finder.where().eq("userName","sample12345").eq("password","sample12345").findUnique();

    	Distance dis = Distance.finder.where().eq("userId", userID.userId).findList().get(0);

        Travel_box newbox = new Travel_box();
        newbox.date = "1/1";
        newbox.title = "初詣集合";
        newbox.start_time = "9時";
        newbox.end_time = "";
        newbox.place = "JR山手線原宿駅";
        newbox.distanceId = dis.distanceId;

        newbox.save();

        Travel_box newbox1 = new Travel_box();
        newbox1.date = "1/1";
        newbox1.title = "初詣";
        newbox1.start_time = "9時30分";
        newbox1.end_time = "11時";
        newbox1.place = "明治神宮";
        newbox1.distanceId = dis.distanceId;

        newbox1.save();

        return dis.distanceId;
    }

    //twitterの取得
	public static List<String> tweetGet(String place) throws TwitterException {

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


		// 検索ワードをセット
		query.setQuery(place);
		// 1度のリクエストで取得するTweetの数（100が最大）
		query.setCount(20);

		// 検索実行
		QueryResult result = twitter.search(query);

		// 検索結果を見てみる
		for (twitter4j.Status tweet : result.getTweets()) {

//			String u = "name:" + tweet.getUser().getName();
//			tweetList.add(u);

			// 本文
			String str = tweet.getText();
			String word = "";
			// ハッシュタグとURLの削除
			StringTokenizer sta = new StringTokenizer(str, " ");
			//トークンの出力
			while(sta.hasMoreTokens()) {
				String wk = sta.nextToken();
				if(wk.indexOf("RT") == -1 && wk.indexOf("@") == -1){
					word += wk + " ";
				}
			}
			if(!word.equals("")) {
				tweetList.add(word);
			}
		}

		return tweetList;
	}
}
