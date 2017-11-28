package controllers;

import models.LoginRequest;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Login extends Controller {

	//ログイン画面
    public static Result login() {
        return ok(
                views.html.login.render()
            );
    }

    //ログイン処理
    public static Result doLogin() {
    	LoginRequest request = Form.form(LoginRequest.class).bindFromRequest().get();
    	User user = User.finder.where().eq("userName",request.userName).eq("password",request.password).findUnique();
        if(user == null){
            flash("message","入力が間違っています");
            return redirect(routes.Login.login());
        }
        session("userName",user.userName);
        session("userId",user.userId.toString());
        return redirect(routes.Application.index());
    }

    //ログアウト処理
    public static Result logOut() {
    	session().clear();
        return redirect(routes.Application.index());
    }
}
