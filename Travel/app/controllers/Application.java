package controllers;

import java.util.List;

import models.Distance;
import models.Travel_box;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	static Form<Travel_box> travelForm = Form.form(Travel_box.class).fill(new Travel_box());
	static Form<Distance> disForm = Form.form(Distance.class);
	static Long id = (long) 0;

	//topへ
    public static Result index() {
        return ok(
                views.html.index.render()
            );
    }

    //editの画面
    public static Result showTravel() {

    	if(session("historyId") != null) {
    		id = Long.parseLong(session("historyId"));
    		return ok(
                    views.html.distanceEdit.render(
                    		Travel_box.finder.where().eq("distanceId", id).findList(),
                    		travelForm,
                    		Distance.finder.where().eq("distanceId", id).findList().get(0).distanceTitle,
                    		disForm)
                );
    	} else {
    		return ok(
                    views.html.distanceEdit.render(Travel_box.all(session("userId")), travelForm, Distance.title(session("userId")), disForm)
                );
    	}
    }

    //一つの項目を作る
    public static Result boxCreate() {
        Form<Travel_box> filledForm = travelForm.bindFromRequest();

        // Form内にエラーがあるかどうかチェック
        if (filledForm.hasErrors()) { // errorがあるならbadRequest
            return badRequest( views.html.distanceEdit.render(Travel_box.all(session("userId")), filledForm, Distance.title(session("userId")), disForm));
        }

    	if(session("historyId") != null) {
    		Travel_box box = filledForm.get();
    		box.distanceId = Long.parseLong(session("historyId"));
    		box.save();
    	} else {
    		Travel_box.create(filledForm.get(), Long.parseLong(session("userId")));
    	}

        return redirect(routes.Application.showTravel());
    }

    //しおりを作成する
    public static Result travelCreate() {
    	session().remove("nowCreate");
    	return redirect(routes.Application.index());
    }

    public static Result editTravel_box(Long id) {
        Form<Travel_box> filledForm = travelForm.bindFromRequest();

        // Form内にエラーがあるかどうかチェック
        if (filledForm.hasErrors()) { // errorがあるならbadRequest
            return badRequest( views.html.distanceEdit.render(Travel_box.all(session("userId")), filledForm, Distance.title(session("userId")), disForm));
        }

        if(session("historyId") != null) {
    		Travel_box box = filledForm.get();
    		Travel_box newbox = Travel_box.finder.byId(id);
        	newbox.date = box.date;
        	newbox.title = box.title;
        	newbox.start_time = box.start_time;
        	newbox.end_time = box.end_time;
        	newbox.place = box.place;
            newbox.update();
    	} else {
    		Travel_box.update(filledForm.get(), id);
    	}
        return redirect(routes.Application.showTravel());
    }

    //項目の削除
    public static Result deleteTravel_box(Long id) {
        Travel_box.delete(id); // delete

        return redirect(routes.Application.showTravel());
    }

    //タイトルの変更
    public static Result createTitle() {
    	Form<Distance> filledForm = disForm.bindFromRequest();
    	Distance distance = filledForm.get();

    	if(session("historyId") != null) {
    		distance.distanceId = Long.parseLong(session("historyId"));
    	} else {
	    	Long id = Long.parseLong(session("userId"));
	    	List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
	    	Long max = (long) 0;
	    	for(Distance num: dis) {
	    		if(max <= num.distanceId) {
	    			max = num.distanceId;
	    		}
	    	}
	    	distance.distanceId = max;
    	}

    	distance.update();
    	return redirect(routes.Application.showTravel());
    }

    //新規作成
    public static Result first() {
    	User user = User.byId(session("userId"));
		Distance distance = new Distance();
    	user.distance.add(distance);
    	user.save();

    	Travel_box newbox = new Travel_box();
    	newbox.date = "";
    	newbox.title = "";
    	newbox.start_time = "";
    	newbox.end_time = "";
    	newbox.place = "";

    	Long id = Long.parseLong(session("userId"));
    	List<Distance> dis = Distance.finder.where().eq("userId", id).findList();
    	Long max = (long) 0;
    	for(Distance num: dis) {
    		if(max <= num.distanceId) {
    			max = num.distanceId;
    		}
    	}
    	newbox.distanceId = max;

        newbox.save();

        session("nowCreate", String.valueOf(newbox.distanceId));
        return redirect(routes.Application.showTravel());
    }
}
