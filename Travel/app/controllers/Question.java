package controllers;

import models.QuestionDTO;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Question extends Controller {
	static Form<QuestionDTO> questionForm = Form.form(QuestionDTO.class);

	//お問い合わせ画面
    public static Result question() {
        return ok(
                views.html.question.render(QuestionDTO.all(), questionForm)
            );
    }

    //お問い合わせの内容の取得
    public static Result createQuestion() {
        Form<QuestionDTO> filledForm = questionForm.bindFromRequest();
        QuestionDTO question = filledForm.get();

        // Form内にエラーがあるかどうかチェック
        if (filledForm.hasErrors()) { // errorがあるならbadRequest
            return badRequest( views.html.question.render(QuestionDTO.all(), filledForm));
        }

        QuestionDTO.setName(question.name);
        QuestionDTO.setMail(question.mail);
        QuestionDTO.setContent(question.content);
//        QuestionDTO.update(filledForm.get());
        return redirect(routes.Question.question());
    }
}
