package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Registration extends Controller {
	static Form<User> userForm = Form.form(User.class);

	//新規会員登録画面
    public static Result registration() {
        return ok(
                views.html.registration.render(User.all(), userForm)
            );
    }

    //会員の作成
    public static Result createUser() {
        Form<User> filledForm = userForm.bindFromRequest();

        // Form内にエラーがあるかどうかチェック
        if (filledForm.hasErrors()) { // errorがあるならbadRequest
            return badRequest( views.html.registration.render(User.all(), filledForm));
        }

        User.create(filledForm.get());
        session("userName", filledForm.get().userName);
        session("userId", filledForm.get().userId.toString());

        return redirect(routes.Application.index());
    }
}
